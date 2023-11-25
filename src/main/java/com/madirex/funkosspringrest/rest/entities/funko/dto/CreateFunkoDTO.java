package com.madirex.funkosspringrest.rest.entities.funko.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

/**
 * Class CreateFunkoDTO
 */
@Getter
@Builder
public class CreateFunkoDTO {
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
    @NotNull(message = "La categoría no puede estar vacía")
    @Schema(description = "Identificador de la categoría", example = "1")
    private Long categoryId;
}