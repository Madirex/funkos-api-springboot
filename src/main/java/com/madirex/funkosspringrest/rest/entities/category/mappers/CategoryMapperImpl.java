package com.madirex.funkosspringrest.rest.entities.category.mappers;

import com.madirex.funkosspringrest.rest.entities.category.dto.CreateCategoryDTO;
import com.madirex.funkosspringrest.rest.entities.category.dto.UpdateCategoryDTO;
import com.madirex.funkosspringrest.rest.entities.category.models.Category;
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
     * Mapea un UpdateCategoryDTO en Category dada una categoría existente
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
