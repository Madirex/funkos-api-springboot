package com.madirex.funkosspringrest.dto.funko;

import com.madirex.funkosspringrest.models.Category;
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