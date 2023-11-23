package com.madirex.funkosspringrest.rest.entities.order.services;

import com.madirex.funkosspringrest.rest.entities.funko.exceptions.FunkoNotFoundException;
import com.madirex.funkosspringrest.rest.entities.funko.exceptions.FunkoNotValidUUIDException;
import com.madirex.funkosspringrest.rest.entities.funko.repository.FunkoRepository;
import com.madirex.funkosspringrest.rest.entities.order.dto.CreateOrder;
import com.madirex.funkosspringrest.rest.entities.order.dto.UpdateOrder;
import com.madirex.funkosspringrest.rest.entities.order.exceptions.*;
import com.madirex.funkosspringrest.rest.entities.order.mappers.OrderMapper;
import com.madirex.funkosspringrest.rest.entities.order.models.Order;
import com.madirex.funkosspringrest.rest.entities.order.models.OrderLine;
import com.madirex.funkosspringrest.rest.entities.order.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.madirex.funkosspringrest.rest.entities.funko.services.FunkoServiceImpl.NOT_VALID_FORMAT_UUID_MSG;

/**
 * Implementación de la interfaz OrderService
 */
@Service
@Slf4j
@CacheConfig(cacheNames = {"orders"})
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final FunkoRepository funkoRepository;
    private final OrderMapper orderMapper;

    /**
     * Constructor de la clase
     *
     * @param orderRepository Repositorio Order
     * @param funkoRepository Repositorio Funko
     * @param orderMapper     Mapper Order
     */
    public OrderServiceImpl(OrderRepository orderRepository, FunkoRepository funkoRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.funkoRepository = funkoRepository;
        this.orderMapper = orderMapper;
    }

    /**
     * Obtiene todos los pedidos
     *
     * @param pageable Pageable para paginar y ordenar
     * @return Page con los pedidos
     */
    @Override
    public Page<Order> findAll(Pageable pageable) {
        log.info("Obteniendo todos los orders paginados y ordenados con {}", pageable);
        return orderRepository.findAll(pageable);
    }

    /**
     * Obtiene un pedido por su ID
     *
     * @param idOrder id del pedido
     * @return Pedido
     */
    @Override
    @Cacheable(key = "#idOrder")
    public Order findById(ObjectId idOrder) {
        log.info("Obteniendo order con id: " + idOrder);
        return orderRepository.findById(idOrder)
                .orElseThrow(() -> new OrderNotFound(idOrder.toHexString()));
    }

    /**
     * Guarda un pedido
     *
     * @param createOrder pedido a crear
     * @return Pedido guardado
     */
    @Override
    @Transactional
    @CachePut(key = "#result.id")
    public Order save(CreateOrder createOrder) {
        log.info("Guardando order: {}", createOrder);
        var order = orderMapper.createOrderToOrder(createOrder);
        checkValidOrder(order);
        var orderToSave = reserveStockOrder(order);
        orderToSave.setCreatedAt(LocalDateTime.now());
        orderToSave.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(orderToSave);
    }

    /**
     * Borra un pedido
     *
     * @param idOrder id del pedido
     */
    @Override
    @Transactional
    @CacheEvict(key = "#idOrder")
    public void delete(ObjectId idOrder) {
        log.info("Borrando order: " + idOrder);
        var orderToDelete = orderRepository.findById(idOrder)
                .orElseThrow(() -> new OrderNotFound(idOrder.toHexString()));
        updateStockOrders(orderToDelete);
        orderRepository.deleteById(idOrder);
    }

    /**
     * Actualiza un pedido
     *
     * @param idOrder     id del pedido
     * @param updateOrder pedido
     * @return Pedido actualizado
     */
    @Override
    @Transactional
    @CachePut(key = "#idOrder")
    public Order update(ObjectId idOrder, UpdateOrder updateOrder) {
        log.info("Actualizando order con id: " + idOrder);
        var order = orderRepository.findById(idOrder).orElseThrow(() -> new OrderNotFound(idOrder.toHexString()));
        var updatedOrder = orderMapper.updateOrderToOrder(order, updateOrder);
        updateStockOrders(updatedOrder);
        checkValidOrder(updatedOrder);
        var orderToSave = reserveStockOrder(updatedOrder);
        orderToSave.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(orderToSave);
    }

    /**
     * Obtiene todos los pedidos de un usuario
     *
     * @param id       UUID
     * @param pageable Pageable
     * @return Page of orders
     */
    @Override
    public Page<Order> findByUserId(String id, Pageable pageable) {
        log.info("Obteniendo orders del usuario con ID: " + id);
        List<Order> userOrders = orderRepository.findOrdersByUserId(id);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), userOrders.size());
        List<Order> paginatedOrders = userOrders.subList(start, end);
        return new PageImpl<>(paginatedOrders, pageable, userOrders.size());
    }

    /**
     * Reserva el stock de los pedidos
     *
     * @param order pedido
     * @return pedido
     */
    public Order reserveStockOrder(Order order) {
        log.info("Reservando stock del order: {}", order);
        if (order.getOrderLineList() == null || order.getOrderLineList().isEmpty()) {
            throw new OrderNoItems(order.getId());
        }
        order.getOrderLineList().forEach(orderLine -> {
            try {
                var funkoOpt = funkoRepository.findById(UUID.fromString(orderLine.getProductId()));
                if (funkoOpt.isEmpty()) {
                    throw new FunkoNotFoundException("id: " + orderLine.getProductId());
                }
                var funko = funkoOpt.get();
                funko.setQuantity(funko.getQuantity() - orderLine.getQuantity());
                funkoRepository.save(funko);
                orderLine.setTotal(orderLine.getQuantity() * orderLine.getProductPrice());
            } catch (IllegalArgumentException e) {
                throw new FunkoNotValidUUIDException(NOT_VALID_FORMAT_UUID_MSG);
            }
        });
        var total = order.getOrderLineList().stream()
                .map(orderLine -> orderLine.getQuantity() * orderLine.getProductPrice())
                .reduce(0.0, Double::sum);
        var totalItems = order.getOrderLineList().stream()
                .map(OrderLine::getQuantity)
                .reduce(0, Integer::sum);
        order.setTotal(total);
        order.setQuantity(totalItems);
        return order;
    }

    /**
     * Actualiza el stock de los pedidos
     *
     * @param order pedido
     */
    private void updateStockOrders(Order order) {
        log.info("Retornando stock del order: {}", order);
        if (order.getOrderLineList() != null) {
            order.getOrderLineList().forEach(orderLine -> {
                try {
                    var funkoOpt = funkoRepository.findById(UUID.fromString(orderLine.getProductId()));
                    if (funkoOpt.isEmpty()) {
                        throw new FunkoNotFoundException("id: " + orderLine.getProductId());
                    }
                    var funko = funkoOpt.get();
                    funko.setQuantity(funko.getQuantity() + orderLine.getQuantity());
                    funkoRepository.save(funko);
                } catch (IllegalArgumentException e) {
                    throw new FunkoNotValidUUIDException(NOT_VALID_FORMAT_UUID_MSG);
                }
            });
        }
    }

    /**
     * Comprueba si el pedido es válido
     *
     * @param order pedido
     */
    public Order checkValidOrder(Order order) {
        log.info("Comprobando order: {}", order);
        if (order.getOrderLineList() == null || order.getOrderLineList().isEmpty()) {
            throw new OrderNoItems(order.getId());
        }
        order.getOrderLineList().forEach(orderLine -> {
            try {
                var funko = funkoRepository.findById(UUID.fromString(orderLine.getProductId()))
                        .orElseThrow(() -> new ProductNotFound(orderLine.getProductId()));
                if (funko.getQuantity() < orderLine.getQuantity() && orderLine.getQuantity() > 0) {
                    throw new ProductWithoutStock(orderLine.getProductId());
                }
                if (!funko.getPrice().equals(orderLine.getProductPrice())) {
                    throw new ProductBadPrice(orderLine.getProductId());
                }
            } catch (IllegalArgumentException e) {
                throw new FunkoNotValidUUIDException(NOT_VALID_FORMAT_UUID_MSG);
            }
        });
        return order;
    }
}