package com.madirex.funkosspringrest.rest.entities.order.dto;

import com.madirex.funkosspringrest.rest.entities.order.models.Client;
import com.madirex.funkosspringrest.rest.entities.order.models.OrderLine;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * UpdateOrder
 */
@Getter
@Setter
@Builder
@ToString
public class UpdateOrder {
    @NotNull(message = "El ID del usuario no puede ser nulo")
    private Long userId;

    @NotNull(message = "El cliente no puede ser nulo")
    @Valid
    private Client client;

    @NotNull(message = "La lista de líneas de pedido no puede ser nula")
    @Valid
    private List<OrderLine> orderLineList;

    @Builder.Default()
    @Min(value = 1, message = "La cantidad debe de ser igual o mayor que 1")
    private Integer quantity = 0;

    @Builder.Default()
    @Min(value = 0, message = "El total no puede ser negativo")
    private Double total = 0.0;
}
