package com.madirex.funkosspringrest.rest.entities.order.models;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@ToString
public class Order {
    @Id
    @Builder.Default
    private ObjectId id = new ObjectId();

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

    @CreationTimestamp
    @Builder.Default()
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp
    @Builder.Default()
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Builder.Default()
    private Boolean isDeleted = false;

    /**
     * Getter id
     *
     * @return id
     */
    @JsonProperty("id")
    public String getId() {
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
