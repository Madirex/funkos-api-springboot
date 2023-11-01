package com.madirex.funkosspringrest.dto.funko;

import com.madirex.funkosspringrest.models.Category;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;

/**
 * Class PatchFunkoDTO
 */
@Getter
@Builder
public class PatchFunkoDTO {
    private String name;
    @Min(value = 0, message = "El precio no puede estar en negativo")
    private Double price;
    @Min(value = 0, message = "La cantidad no puede estar en negativo")
    private Integer quantity;
    private String image;
    private Long categoryId;
}