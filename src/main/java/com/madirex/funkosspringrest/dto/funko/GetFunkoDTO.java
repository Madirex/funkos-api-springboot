package com.madirex.funkosspringrest.dto.funko;

import com.madirex.funkosspringrest.models.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Class GetFunkoDTO
 */
@Getter
@Builder
@AllArgsConstructor
public class GetFunkoDTO {
    private UUID id;
    private String name;
    private Double price;
    private Integer quantity;
    private String image;
    private Category category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}