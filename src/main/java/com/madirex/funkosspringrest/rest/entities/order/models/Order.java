package com.madirex.funkosspringrest.rest.entities.order.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Clase Order
 */
@Document("orders")
@TypeAlias("order")
@Getter
@Setter
@Builder
@ToString
public class Order {
    @Id
    @Builder.Default
    private ObjectId id = new ObjectId();

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

    @CreationTimestamp
    @Builder.Default()
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp
    @Builder.Default()
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Builder.Default()
    @NotNull(message = "isDeleted no puede ser nulo")
    private Boolean isDeleted = false;

    /**
     * Getter id
     *
     * @return id
     */
    @JsonProperty("id")
    public String getId() {
        if (id == null) {
            id = new ObjectId();
        }
        return id.toHexString();
    }

    /**
     * Setter orderLineList
     *
     * @param orderLineList Lista de líneas de pedido
     */
    public void setOrderLineList(List<OrderLine> orderLineList) {
        this.orderLineList = orderLineList;
        this.quantity = orderLineList != null ? orderLineList.size() : 0;
        this.total = orderLineList != null ? orderLineList.stream().mapToDouble(OrderLine::getTotal).sum() : 0.0;
    }
}
