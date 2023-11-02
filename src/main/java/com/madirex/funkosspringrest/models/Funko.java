package com.madirex.funkosspringrest.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Clase Funko
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Funko {
    public static final String IMAGE_DEFAULT = "https://www.madirex.com/favicon.ico";
    @NotNull
    @Id
    private UUID id;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    @Min(value = 0, message = "El precio no puede estar en negativo")
    @NotNull(message = "price no puede ser nulo")
    private Double price;

    @Min(value = 0, message = "La cantidad no puede estar en negativo")
    @NotNull(message = "quantity no puede ser nulo")
    private Integer quantity;

    @NotBlank(message = "La imagen no puede estar vacía")
    private String image;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}