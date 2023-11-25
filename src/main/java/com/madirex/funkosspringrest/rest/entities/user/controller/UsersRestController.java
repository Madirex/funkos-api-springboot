package com.madirex.funkosspringrest.rest.entities.user.controller;

import com.madirex.funkosspringrest.rest.entities.order.dto.CreateOrder;
import com.madirex.funkosspringrest.rest.entities.order.dto.UpdateOrder;
import com.madirex.funkosspringrest.rest.entities.order.models.Order;
import com.madirex.funkosspringrest.rest.entities.order.services.OrderService;
import com.madirex.funkosspringrest.rest.entities.user.dto.UserInfoResponse;
import com.madirex.funkosspringrest.rest.entities.user.dto.UserRequest;
import com.madirex.funkosspringrest.rest.entities.user.dto.UserResponse;
import com.madirex.funkosspringrest.rest.entities.user.exceptions.UserDiffers;
import com.madirex.funkosspringrest.rest.entities.user.exceptions.UserNotLogged;
import com.madirex.funkosspringrest.rest.entities.user.exceptions.UserNotValidUUIDException;
import com.madirex.funkosspringrest.rest.entities.user.models.User;
import com.madirex.funkosspringrest.rest.entities.user.services.UsersService;
import com.madirex.funkosspringrest.rest.pagination.model.PageResponse;
import com.madirex.funkosspringrest.rest.pagination.util.PaginationLinksUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;
import java.util.Optional;

/**
 * Controlador de Users
 */
@RestController
@Slf4j
@RequestMapping("api/users")
@PreAuthorize("hasRole('USER')")
public class UsersRestController {
    private final UsersService usersService;
    private final OrderService orderService;
    private final PaginationLinksUtils paginationLinksUtils;

    /**
     * Controlador
     *
     * @param usersService         Servicio de usuarios
     * @param orderService         Servicio de orders
     * @param paginationLinksUtils Utilidades de PaginationLinks
     */
    @Autowired
    public UsersRestController(UsersService usersService, OrderService orderService, PaginationLinksUtils paginationLinksUtils) {
        this.usersService = usersService;
        this.orderService = orderService;
        this.paginationLinksUtils = paginationLinksUtils;
    }

    /**
     * Obtiene todos los usuarios
     *
     * @param username  username del usuario
     * @param email     email del usuario
     * @param isDeleted si está borrado o no
     * @param page      página
     * @param size      tamaño
     * @param sortBy    campo de ordenación
     * @param direction dirección de ordenación
     * @param request   petición
     * @return Respuesta con la página de usuarios
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<UserResponse>> findAll(
            @RequestParam(required = false) Optional<String> username,
            @RequestParam(required = false) Optional<String> email,
            @RequestParam(required = false) Optional<Boolean> isDeleted,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ) {
        log.info("findAll: username: {}, email: {}, isDeleted: {}, page: {}, size: {}, sortBy: {}, direction: {}",
                username, email, isDeleted, page, size, sortBy, direction);
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<UserResponse> pageResult = usersService.findAll(username, email, isDeleted,
                PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    /**
     * Obtiene un usuario por su ID
     *
     * @param id del usuario, se pasa como parámetro de la URL /{id}
     * @return Usuario si existe
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserInfoResponse> findById(@PathVariable String id) {
        log.info("findById: id: {}", id);
        return ResponseEntity.ok(usersService.findById(id));
    }

    /**
     * Crea un nuevo usuario
     *
     * @param userRequest usuario a crear
     * @return Usuario creado
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("save: userRequest: {}", userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(usersService.save(userRequest));
    }

    /**
     * Actualiza un usuario
     *
     * @param id          ID del usuario
     * @param userRequest Usuario a actualizar
     * @return Usuario actualizado
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> updateUser(@PathVariable String id, @Valid @RequestBody UserRequest userRequest) {
        log.info("update: id: {}, userRequest: {}", id, userRequest);
        return ResponseEntity.ok(usersService.update(id, userRequest));
    }

    /**
     * Borra un usuario
     *
     * @param id ID del usuario
     * @return Respuesta vacía
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        log.info("delete: id: {}", id);
        usersService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene el usuario actual
     *
     * @param user usuario autenticado
     * @return Datos del usuario
     */
    @GetMapping("/me/profile")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserInfoResponse> me(@AuthenticationPrincipal User user) {
        log.info("Obteniendo usuario");
        return ResponseEntity.ok(usersService.findById(checkValidIdAndReturn(user)));
    }

    /**
     * Actualiza el usuario actual
     *
     * @param user        Usuario autenticado
     * @param userRequest Usuario a actualizar
     * @return Usuario actualizado
     */
    @PutMapping("/me/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserResponse> updateMe(@AuthenticationPrincipal User user,
                                                 @Valid @RequestBody UserRequest userRequest) {
        log.info("updateMe: user: {}, userRequest: {}", user, userRequest);
        return ResponseEntity.ok(usersService.update(checkValidIdAndReturn(user), userRequest));
    }

    /**
     * Borra el usuario actual
     *
     * @param user Usuario autenticado
     * @return Respuesta vacía
     */
    @DeleteMapping("/me/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteMe(@AuthenticationPrincipal User user) {
        log.info("deleteMe: user: {}", user);
        usersService.deleteById(checkValidIdAndReturn(user));
        return ResponseEntity.noContent().build();
    }

    /**
     * Comprueba que el ID del usuario es válido
     *
     * @param user Usuario autenticado
     * @return ID del usuario
     */
    private String checkValidIdAndReturn(User user) {
        try {
            if (user == null) {
                throw new UserNotLogged("Usuario no autenticado.");
            }
            return user.getId().toString();
        } catch (IllegalArgumentException e) {
            throw new UserNotValidUUIDException(Objects.requireNonNull(user).getId().toString());
        }
    }

    /**
     * Obtiene los pedidos del usuario actual
     *
     * @param user      Usuario autenticado
     * @param page      Página
     * @param size      Tamaño
     * @param sortBy    Campo de ordenación
     * @param direction Dirección de ordenación
     * @return Respuesta con la página de pedidos
     */
    @GetMapping("/me/orders")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PageResponse<Order>> getOrderByUser(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        log.info("Obteniendo pedidos del usuario con ID: " + user.getId());
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(PageResponse.of(orderService.findByUserId(checkValidIdAndReturn(user), pageable)
                , sortBy, direction));
    }

    /**
     * Obtiene un pedido del usuario actual
     *
     * @param user    usuario autenticado
     * @param idOrder id del pedido
     * @return Pedido
     */
    @GetMapping("/me/orders/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Order> getOrder(
            @AuthenticationPrincipal User user,
            @PathVariable("id") ObjectId idOrder
    ) {
        log.info("Obteniendo pedido con id: " + idOrder);
        return ResponseEntity.ok(orderService.findById(idOrder));
    }

    /**
     * Crea un pedido para el usuario actual
     *
     * @param user  Usuario autenticado
     * @param order Pedido a crear
     * @return Pedido creado
     */
    @PostMapping("/me/orders")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Order> saveOrder(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateOrder order
    ) {
        log.info("Creando pedido: " + order);
        if (!order.getUserId().equals(checkValidIdAndReturn(user))) {
            throw new UserDiffers("El usuario del pedido no coincide con el usuario autenticado");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.save(order));
    }

    /**
     * Actualiza un pedido del usuario actual
     *
     * @param user    usuario autenticado
     * @param idOrder id del pedido
     * @param order   pedido a actualizar
     * @return Pedido actualizado
     */
    @PutMapping("/me/orders/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Order> updateOrder(
            @AuthenticationPrincipal User user,
            @PathVariable("id") ObjectId idOrder,
            @Valid @RequestBody UpdateOrder order) {
        log.info("Actualizando pedido con ID: " + idOrder);
        if (!order.getUserId().equals(checkValidIdAndReturn(user))) {
            throw new UserDiffers("El usuario del pedido no coincide con el usuario autenticado");
        }
        return ResponseEntity.ok(orderService.update(idOrder, order));
    }

    /**
     * Borra un pedido del usuario actual
     *
     * @param user    usuario autenticado
     * @param idOrder id del pedido
     * @return Pedido borrado
     */
    @DeleteMapping("/me/orders/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteOrder(
            @AuthenticationPrincipal User user,
            @PathVariable("id") ObjectId idOrder
    ) {
        log.info("Borrando pedido con ID: " + idOrder);
        orderService.delete(idOrder);
        return ResponseEntity.noContent().build();
    }
}