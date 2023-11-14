package com.madirex.funkosspringrest.mappers.category;

import com.madirex.funkosspringrest.dto.category.CreateCategoryDTO;
import com.madirex.funkosspringrest.dto.category.UpdateCategoryDTO;
import com.madirex.funkosspringrest.models.Category;

/**
 * Clase CategoryMapper
 */
public interface CategoryMapper {
    /**
     * Mapper de CreateCategoryDTO
     *
     * @param dto DTO con los datos de la categoría
     * @return Categoría creada
     */
    Category toCategory(CreateCategoryDTO dto);

    /**
     * Mapper de UpdateCategoryDTO dada una categoría existente
     *
     * @param existingCategory Categoría existente
     * @param dto              DTO con los datos de la categoría
     * @return Categoría actualizada
     */
    Category toCategory(Category existingCategory, UpdateCategoryDTO dto);
}
