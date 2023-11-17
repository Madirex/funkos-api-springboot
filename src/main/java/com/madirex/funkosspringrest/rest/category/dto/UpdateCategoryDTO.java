package com.madirex.funkosspringrest.rest.category.dto;


import com.madirex.funkosspringrest.rest.category.models.Category;
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
    private Category.Type type;

    @NonNull
    private Boolean active;
}