package com.madirex.funkosspringrest.rest.entities.category.mappers;

import com.madirex.funkosspringrest.rest.entities.category.dto.CreateCategoryDTO;
import com.madirex.funkosspringrest.rest.entities.category.dto.UpdateCategoryDTO;
import com.madirex.funkosspringrest.rest.entities.category.models.Category;

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
