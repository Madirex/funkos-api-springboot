package com.madirex.funkosspringrest.rest.entities.category.dto;


import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.NonNull;

/**
 * Class UpdateFunkoDTO
 */
@Getter
@Builder
public class UpdateCategoryDTO {
    @NonNull
    private String type;

    @NonNull
    private Boolean active;
}