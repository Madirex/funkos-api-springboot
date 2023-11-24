package com.madirex.funkosspringrest.rest.entities.category.dto;


import lombok.Builder;
import lombok.Getter;

/**
 * Class PatchFunkoDTO
 */
@Getter
@Builder
public class PatchCategoryDTO {
    private String type;

    private Boolean active;
}