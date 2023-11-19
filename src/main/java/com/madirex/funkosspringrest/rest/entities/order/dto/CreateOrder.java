package com.madirex.funkosspringrest.rest.entities.order.dto;

import com.madirex.funkosspringrest.rest.entities.order.models.Client;
import com.madirex.funkosspringrest.rest.entities.order.models.OrderLine;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

/**
 * CreateOrder
 */
public class CreateOrder {
    @NotNull(message = "El ID del usuario no puede ser nulo")
    private Long userId;

    @NotNull(message = "El cliente no puede ser nulo")
    private Client client;

    @NotNull(message = "La lista de líneas de pedido no puede ser nula")
    private List<OrderLine> orderLineList;

    @Builder.Default()
    private Integer quantity = 0;

    @Builder.Default()
    private Double total = 0.0;
}
