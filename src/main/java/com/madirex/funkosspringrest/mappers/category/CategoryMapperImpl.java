package com.madirex.funkosspringrest.mappers.category;

import com.madirex.funkosspringrest.dto.category.CreateCategoryDTO;
import com.madirex.funkosspringrest.dto.category.UpdateCategoryDTO;
import com.madirex.funkosspringrest.models.Category;
import org.springframework.stereotype.Component;

/**
 * Clase CategoryMapper
 */
@Component
public class CategoryMapperImpl implements CategoryMapper {
    /**
     * Mapea un CreateCategoryDTO en Category
     *
     * @param dto CreateCategoryDTO a mapear
     * @return Category mapeado
     */
    public Category toCategory(CreateCategoryDTO dto) {
        return Category.builder()
                .type(dto.getType())
                .active(dto.getActive())
                .build();
    }

    /**
     * Mapea un UpdateCategoryDTO en Category
     *
     * @param existingCategory Category existente
     * @param dto              UpdateCategoryDTO a mapear
     * @return Category mapeado
     */
    public Category toCategory(Category existingCategory, UpdateCategoryDTO dto) {
        return Category.builder()
                .id(existingCategory.getId())
                .createdAt(existingCategory.getCreatedAt())
                .type(dto.getType())
                .active(dto.getActive())
                .build();
    }
}
