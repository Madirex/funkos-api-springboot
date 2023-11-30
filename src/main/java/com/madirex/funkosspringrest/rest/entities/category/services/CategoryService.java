package com.madirex.funkosspringrest.rest.entities.category.services;

import com.madirex.funkosspringrest.rest.entities.category.dto.CreateCategoryDTO;
import com.madirex.funkosspringrest.rest.entities.category.dto.PatchCategoryDTO;
import com.madirex.funkosspringrest.rest.entities.category.dto.UpdateCategoryDTO;
import com.madirex.funkosspringrest.rest.entities.category.exceptions.CategoryNotFoundException;
import com.madirex.funkosspringrest.rest.entities.category.exceptions.DeleteCategoryException;
import com.madirex.funkosspringrest.rest.entities.category.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Interface CategoryService
 */
public interface CategoryService {

    /**
     * Obtiene todas las categorías
     *
     * @param type     category
     * @param isActive active
     * @param pageable pageable
     * @return Page<Category>
     */
    Page<Category> getAllCategory(Optional<String> type, Optional<Boolean> isActive, Pageable pageable);

    /**
     * Obtiene una categoría por su id
     *
     * @param id id
     * @return Category
     * @throws CategoryNotFoundException CategoryNotFoundException
     */
    Category getCategoryById(Long id) throws CategoryNotFoundException;

    /**
     * Crea una categoría
     *
     * @param category category
     * @return Category
     */
    Category postCategory(CreateCategoryDTO category);

    /**
     * Actualiza una categoría
     *
     * @param id       id
     * @param category category
     * @return Category
     * @throws CategoryNotFoundException CategoryNotFoundException
     */
    Category putCategory(Long id, UpdateCategoryDTO category) throws CategoryNotFoundException;

    /**
     * Actualiza una categoría parcialmente
     *
     * @param id       id
     * @param category category
     * @return Category
     * @throws CategoryNotFoundException CategoryNotFoundException
     */
    Category patchCategory(Long id, PatchCategoryDTO category) throws CategoryNotFoundException;

    /**
     * Elimina una categoría
     *
     * @param id id
     * @throws CategoryNotFoundException CategoryNotFoundException
     * @throws DeleteCategoryException   DeleteCategoryException
     */
    void deleteCategory(Long id) throws CategoryNotFoundException, DeleteCategoryException;
}
