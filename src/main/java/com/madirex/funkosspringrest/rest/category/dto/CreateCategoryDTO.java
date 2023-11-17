package com.madirex.funkosspringrest.rest.category.dto;

import com.madirex.funkosspringrest.rest.category.models.Category;
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
    private Category.Type type;

    @NonNull
    private Boolean active;
}