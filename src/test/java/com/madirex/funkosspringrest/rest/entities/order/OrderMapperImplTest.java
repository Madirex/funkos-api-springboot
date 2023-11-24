package com.madirex.funkosspringrest.rest.entities.order;

import com.madirex.funkosspringrest.rest.entities.order.dto.CreateOrder;
import com.madirex.funkosspringrest.rest.entities.order.dto.UpdateOrder;
import com.madirex.funkosspringrest.rest.entities.order.mappers.OrderMapperImpl;
import com.madirex.funkosspringrest.rest.entities.order.models.Address;
import com.madirex.funkosspringrest.rest.entities.order.models.Client;
import com.madirex.funkosspringrest.rest.entities.order.models.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Clase OrderMapperImplTest
 */
class OrderMapperImplTest {
    private OrderMapperImpl orderMapperImpl;

    /**
     * Método setUp para inicializar los objetos
     */
    @BeforeEach
    void setUp() {
        orderMapperImpl = new OrderMapperImpl();
    }

    /**
     * Test para comprobar que el mapeo de CreateOrder a Order es correcto
     */
    @Test
    void testCreateOrderToOrder() {
        var order = CreateOrder.builder()
                .userId(UUID.randomUUID().toString())
                .client(new Client("nombre", "mail@mail.com",
                        "34543534",
                        Address.builder().build()))
                .orderLineList(new ArrayList<>())
                .quantity(2)
                .total(2.2)
                .build();
        var mapped = orderMapperImpl.createOrderToOrder(order);
        assertAll("Order properties",
                () -> assertNotNull(mapped.getId(), "El ID no debe ser nulo"),
                () -> assertEquals(order.getUserId(), mapped.getUserId(), "El ID del usuario debe coincidir"),
                () -> assertEquals(order.getClient(), mapped.getClient(), "El cliente debe coincidir"),
                () -> assertEquals(order.getOrderLineList(), mapped.getOrderLineList(), "La lista de líneas de pedido debe coincidir"),
                () -> assertEquals(order.getQuantity(), mapped.getQuantity(), "La cantidad debe coincidir"),
                () -> assertEquals(order.getTotal(), mapped.getTotal(), "El total debe coincidir")
        );
    }

    /**
     * Test para comprobar que el mapeo de UpdateOrder a Order es correcto
     */
    @Test
    void testUpdateOrderToOrder() {
        var order = Order.builder()
                .userId(UUID.randomUUID().toString())
                .client(new Client("nombre", "mail@mail.com",
                        "34543534",
                        Address.builder().build()))
                .orderLineList(new ArrayList<>())
                .quantity(2)
                .total(2.2)
                .build();
        var orderUpdated = UpdateOrder.builder()
                .userId(UUID.randomUUID().toString())
                .client(new Client("name", "mail2@mail2.com",
                        "34234543534",
                        Address.builder().build()))
                .orderLineList(new ArrayList<>())
                .quantity(4)
                .total(23.2)
                .build();
        var mapped = orderMapperImpl.updateOrderToOrder(order, orderUpdated);
        assertAll("Order properties",
                () -> assertNotNull(mapped.getId(), "El ID no debe ser nulo"),
                () -> assertEquals(orderUpdated.getUserId(), mapped.getUserId(), "El ID del usuario debe coincidir"),
                () -> assertEquals(orderUpdated.getClient(), mapped.getClient(), "El cliente debe coincidir"),
                () -> assertEquals(orderUpdated.getOrderLineList(), mapped.getOrderLineList(), "La lista de líneas de pedido debe coincidir"),
                () -> assertEquals(orderUpdated.getQuantity(), mapped.getQuantity(), "La cantidad debe coincidir"),
                () -> assertEquals(orderUpdated.getTotal(), mapped.getTotal(), "El total debe coincidir")
        );
    }
}
