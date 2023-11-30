package com.madirex.funkosspringrest.rest.entities.funko.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;

/**
 * Class PatchFunkoDTO
 */
@Getter
@Builder
public class PatchFunkoDTO {
    @Schema(description = "Nombre", example = "Funko de Batman")
    private String name;
    @Min(value = 0, message = "El precio no puede estar en negativo")
    @Schema(description = "Precio", example = "12.99")
    private Double price;
    @Min(value = 0, message = "La cantidad no puede estar en negativo")
    @Schema(description = "Cantidad", example = "10")
    private Integer quantity;
    @Schema(description = "Imagen", example = "https://www.madirex.com/images/batman.jpg")
    private String image;
    @Schema(description = "Identificador de la categoría", example = "1")
    private Long categoryId;
}