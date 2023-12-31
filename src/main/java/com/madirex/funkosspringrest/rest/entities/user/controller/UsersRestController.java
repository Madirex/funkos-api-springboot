package com.madirex.funkosspringrest.rest.entities.user.controller;

import com.madirex.funkosspringrest.rest.entities.order.dto.CreateOrder;
import com.madirex.funkosspringrest.rest.entities.order.dto.UpdateOrder;
import com.madirex.funkosspringrest.rest.entities.order.models.Order;
import com.madirex.funkosspringrest.rest.entities.order.services.OrderService;
import com.madirex.funkosspringrest.rest.entities.user.dto.UserInfoResponse;
import com.madirex.funkosspringrest.rest.entities.user.dto.UserRequest;
import com.madirex.funkosspringrest.rest.entities.user.dto.UserResponse;
import com.madirex.funkosspringrest.rest.entities.user.dto.UserUpdate;
import com.madirex.funkosspringrest.rest.entities.user.exceptions.UserDiffers;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

/**
 * Controlador de Users
 */
@RestController
@Slf4j
@RequestMapping("api/users")
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public class UsersRestController {
    private final UsersService usersService;
    private final PasswordEncoder passwordEncoder;
    private final OrderService orderService;
    private final PaginationLinksUtils paginationLinksUtils;

    /**
     * Controlador
     *
     * @param usersService         Servicio de usuarios
     * @param passwordEncoder      Encoder de contraseñas
     * @param orderService         Servicio de orders
     * @param paginationLinksUtils Utilidades de PaginationLinks
     */
    @Autowired
    public UsersRestController(UsersService usersService, PasswordEncoder passwordEncoder, OrderService orderService,
                               PaginationLinksUtils paginationLinksUtils) {
        this.usersService = usersService;
        this.passwordEncoder = passwordEncoder;
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
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        return ResponseEntity.status(HttpStatus.CREATED).body(usersService.save(userRequest));
    }

    /**
     * Actualiza un usuario
     *
     * @param id         ID del usuario
     * @param userUpdate Usuario a actualizar
     * @return Usuario actualizado
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> updateUser(@PathVariable String id, @Valid @RequestBody UserUpdate userUpdate) {
        log.info("update: id: {}, userRequest: {}", id, userUpdate);
        userUpdate.setPassword(passwordEncoder.encode(userUpdate.getPassword()));
        return ResponseEntity.ok(usersService.update(id, userUpdate));
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
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<UserInfoResponse> me(@AuthenticationPrincipal User user) {
        log.info("Obteniendo usuario");
        return ResponseEntity.ok(usersService.findById(checkValidIdAndReturn(user)));
    }

    /**
     * Actualiza el usuario actual
     *
     * @param user       Usuario autenticado
     * @param userUpdate Usuario a actualizar
     * @return Usuario actualizado
     */
    @PutMapping("/me/profile")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<UserResponse> updateMe(@AuthenticationPrincipal User user,
                                                 @Valid @RequestBody UserUpdate userUpdate) {
        log.info("updateMe: user: {}, userRequest: {}", user, userUpdate);
        userUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
        return ResponseEntity.ok(usersService.update(checkValidIdAndReturn(user), userUpdate));
    }

    /**
     * Borra el usuario actual
     *
     * @param user Usuario autenticado
     * @return Respuesta vacía
     */
    @DeleteMapping("/me/profile")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
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
        return user.getId().toString();
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
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
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
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
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
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Order> saveOrder(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateOrder order
    ) {
        log.info("Creando pedido: " + order);
        checkValidUser(order.getUserId(), user);
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
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Order> updateOrder(
            @AuthenticationPrincipal User user,
            @PathVariable("id") ObjectId idOrder,
            @Valid @RequestBody UpdateOrder order) {
        log.info("Actualizando pedido con ID: " + idOrder);
        checkValidUser(order.getUserId(), user);
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
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Void> deleteOrder(
            @AuthenticationPrincipal User user,
            @PathVariable("id") ObjectId idOrder
    ) {
        log.info("Borrando pedido con ID: " + idOrder);
        orderService.delete(idOrder);
        return ResponseEntity.noContent().build();
    }

    /**
     * Comprueba que el usuario del pedido coincide con el usuario autenticado
     *
     * @param order id del usuario del pedido
     * @param user  usuario autenticado
     */
    private void checkValidUser(String order, User user) {
        if (!order.equals(checkValidIdAndReturn(user))) {
            throw new UserDiffers("El usuario del pedido no coincide con el usuario autenticado");
        }
    }
}