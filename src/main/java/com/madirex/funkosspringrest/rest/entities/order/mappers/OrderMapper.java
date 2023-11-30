package com.madirex.funkosspringrest.rest.entities.order.mappers;

import com.madirex.funkosspringrest.rest.entities.order.dto.CreateOrder;
import com.madirex.funkosspringrest.rest.entities.order.dto.UpdateOrder;
import com.madirex.funkosspringrest.rest.entities.order.models.Order;

/**
 * OrderMapper
 */
public interface OrderMapper {

    /**
     * Mapea un objeto CreateOrder a un objeto Order
     *
     * @param createOrder Objeto CreateOrder
     * @return Objeto Order
     */
    Order createOrderToOrder(CreateOrder createOrder);

    /**
     * Mapea un objeto UpdateOrder a un objeto Order
     *
     * @param order       Objeto Order
     * @param updateOrder Objeto UpdateOrder
     * @return Objeto Order
     */
    Order updateOrderToOrder(Order order, UpdateOrder updateOrder);
}
