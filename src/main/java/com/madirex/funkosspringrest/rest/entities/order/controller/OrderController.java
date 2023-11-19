package com.madirex.funkosspringrest.rest.entities.order.controller;

import com.madirex.funkosspringrest.rest.entities.order.dto.CreateOrder;
import com.madirex.funkosspringrest.rest.entities.order.dto.UpdateOrder;
import com.madirex.funkosspringrest.rest.entities.order.models.Order;
import com.madirex.funkosspringrest.rest.entities.order.services.OrderService;
import com.madirex.funkosspringrest.rest.pagination.model.PageResponse;
import com.madirex.funkosspringrest.rest.pagination.util.PaginationLinksUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Controlador de pedidos
 */
@RestController
@RequestMapping("/api/orders")
@Slf4j
public class OrderController {
    private final OrderService orderService;
    private final PaginationLinksUtils paginationLinksUtils;

    @Autowired
    public OrderController(OrderService orderService, PaginationLinksUtils paginationLinksUtils) {
        this.orderService = orderService;
        this.paginationLinksUtils = paginationLinksUtils;
    }

    /**
     * Obtiene todos los pedidos
     *
     * @param page      Número de página
     * @param size      Tamaño de la página
     * @param sortBy    Campo de ordenación
     * @param direction Dirección de ordenación
     * @param request   Petición
     * @return Lista de pedidos paginada
     */
    @GetMapping()
    public ResponseEntity<PageResponse<Order>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ) {
        log.info("Obteniendo todos los pedidos");
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<Order> pageResult = orderService.findAll(PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    /**
     * Obtiene un pedido por su ID
     *
     * @param orderId id del pedido
     * @return Pedido
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable("id") ObjectId orderId) {
        log.info("Obteniendo pedido con id: " + orderId);
        return ResponseEntity.ok(orderService.findById(orderId));
    }

    /**
     * Crea un pedido para el usuario actual
     *
     * @param order pedido a crear
     * @return Pedido creado
     */
    @PostMapping()
    public ResponseEntity<Order> createOrder(@Valid @RequestBody CreateOrder order) {
        log.info("Creando pedido: " + order);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.save(order));
    }

    /**
     * Actualiza un pedido
     *
     * @param orderId id del pedido
     * @param order   pedido a actualizar
     * @return Pedido actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable("id") ObjectId orderId, @Valid @RequestBody UpdateOrder order) {
        log.info("Actualizando pedido con id: " + orderId);
        return ResponseEntity.ok(orderService.update(orderId, order));
    }

    /**
     * Borra un pedido
     *
     * @param orderId id del pedido
     * @return Pedido borrado
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Order> deleteOrder(@PathVariable("id") ObjectId orderId) {
        log.info("Borrando pedido con id: " + orderId);
        orderService.delete(orderId);
        return ResponseEntity.noContent().build();
    }
}