package com.madirex.funkosspringrest.rest.entities.funko.dto;

import com.madirex.funkosspringrest.rest.entities.category.models.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Class GetFunkoDTO
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetFunkoDTO {
    @Schema(description = "Identificador UUID", example = "f7a3d5e0-9b9a-4c9a-8b1a-9a0a1a2b3b4c")
    private UUID id;
    @Schema(description = "Nombre", example = "Funko de Batman")
    private String name;
    @Schema(description = "Precio", example = "12.99")
    private Double price;
    @Schema(description = "Cantidad", example = "10")
    private Integer quantity;
    @Setter
    @Schema(description = "Imagen", example = "https://www.madirex.com/images/batman.jpg")
    private String image;
    @Schema(description = "Categoría")
    private Category category;
    @Schema(description = "Fecha de creación", example = "2021-08-01T00:00:00.000Z")
    private LocalDateTime createdAt;
    @Schema(description = "Fecha de actualización", example = "2021-08-01T00:00:00.000Z")
    private LocalDateTime updatedAt;
}