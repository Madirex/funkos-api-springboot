package com.madirex.funkosspringrest.rest.entities.order.mappers;

import com.madirex.funkosspringrest.rest.entities.order.dto.CreateOrder;
import com.madirex.funkosspringrest.rest.entities.order.dto.UpdateOrder;
import com.madirex.funkosspringrest.rest.entities.order.models.Order;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * OrderMapper Implementación
 */
@Component
public class OrderMapperImpl implements OrderMapper {

    /**
     * Mapea un objeto CreateOrder a un objeto Order
     *
     * @param createOrder Objeto CreateOrder a mapear
     * @return Objeto Order
     */
    @Override
    public Order createOrderToOrder(CreateOrder createOrder) {
        return Order.builder()
                .id(new ObjectId())
                .userId(createOrder.getUserId())
                .client(createOrder.getClient())
                .orderLineList(createOrder.getOrderLineList())
                .quantity(createOrder.getQuantity())
                .total(createOrder.getTotal())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .build();
    }

    /**
     * Mapea un objeto UpdateOrder a un objeto Order
     *
     * @param order       Objeto Order a mapear
     * @param updateOrder Objeto UpdateOrder a mapear
     * @return Objeto Order
     */
    @Override
    public Order updateOrderToOrder(Order order, UpdateOrder updateOrder) {
        return Order.builder()
                .id(new ObjectId(order.getId()))
                .userId(updateOrder.getUserId())
                .client(updateOrder.getClient())
                .orderLineList(updateOrder.getOrderLineList())
                .quantity(updateOrder.getQuantity())
                .total(updateOrder.getTotal())
                .createdAt(order.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .isDeleted(order.getIsDeleted())
                .build();
    }
}
