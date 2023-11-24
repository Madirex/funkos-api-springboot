package com.madirex.funkosspringrest.rest.entities.category.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.NonNull;

/**
 * Class PatchFunkoDTO
 */
@Getter
@Builder
public class CreateCategoryDTO {
    @NonNull
    private String type;

    @NonNull
    private Boolean active;
}