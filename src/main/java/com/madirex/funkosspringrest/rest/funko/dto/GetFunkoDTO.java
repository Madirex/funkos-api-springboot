package com.madirex.funkosspringrest.rest.funko.dto;

import com.madirex.funkosspringrest.rest.category.models.Category;
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
    private UUID id;
    private String name;
    private Double price;
    private Integer quantity;
    @Setter
    private String image;
    private Category category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}