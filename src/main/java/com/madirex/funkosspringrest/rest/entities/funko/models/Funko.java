package com.madirex.funkosspringrest.rest.entities.funko.models;

import com.madirex.funkosspringrest.rest.entities.category.models.Category;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Funko {
    public static final String IMAGE_DEFAULT = "https://www.madirex.com/favicon.ico";

    @NotNull
    @Id
    @Schema(description = "Identificador UUID", example = "f7a3d5e0-9b9a-4c9a-8b1a-9a0a1a2b3b4c")
    private UUID id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Schema(description = "Nombre", example = "Funko de Batman")
    private String name;

    @Min(value = 0, message = "El precio no puede estar en negativo")
    @NotNull(message = "price no puede ser nulo")
    @Schema(description = "Precio", example = "12.99")
    private Double price;

    @Min(value = 0, message = "La cantidad no puede estar en negativo")
    @NotNull(message = "quantity no puede ser nulo")
    @Schema(description = "Cantidad", example = "10")
    private Integer quantity;

    @NotBlank(message = "La imagen no puede estar vacía")
    @Schema(description = "Imagen", example = "https://www.madirex.com/images/batman.jpg")
    private String image;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @Schema(description = "Categoría")
    private Category category;

    @CreatedDate
    @Schema(description = "Fecha de creación", example = "2021-08-01T00:00:00.000Z")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Schema(description = "Fecha de actualización", example = "2021-08-01T00:00:00.000Z")
    private LocalDateTime updatedAt;
}