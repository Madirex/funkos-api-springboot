package com.madirex.funkosspringrest.rest.entities.order.services;

import com.madirex.funkosspringrest.rest.entities.order.models.Order;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * OrderService
 */
public interface OrderService {
    /**
     * Find all orders
     *
     * @param pageable Pageable
     * @return Page of orders
     */
    Page<Order> findAll(Pageable pageable);

    /**
     * Find order by id
     *
     * @param orderId ID of order
     * @return Order found
     */
    Order findById(ObjectId orderId);

    /**
     * Save order
     *
     * @param order Order
     * @return Order saved
     */
    Order save(Order order);

    /**
     * Delete order
     *
     * @param orderId ID of order
     */
    void delete(ObjectId orderId);

    /**
     * Update order
     *
     * @param orderId ID of order
     * @param order   Order
     * @return Order updated
     */
    Order update(ObjectId orderId, Order order);
}