package com.madirex.funkosspringrest.dto.category;

import com.madirex.funkosspringrest.models.Category;
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