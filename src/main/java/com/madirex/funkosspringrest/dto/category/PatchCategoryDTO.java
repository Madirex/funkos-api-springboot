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
public class PatchCategoryDTO {
    private Category.Type type;

    private Boolean active;
}