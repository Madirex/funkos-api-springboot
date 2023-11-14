package com.madirex.funkosspringrest.services.category;

import com.madirex.funkosspringrest.dto.category.CreateCategoryDTO;
import com.madirex.funkosspringrest.dto.category.PatchCategoryDTO;
import com.madirex.funkosspringrest.dto.category.UpdateCategoryDTO;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotFoundException;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotValidException;
import com.madirex.funkosspringrest.exceptions.category.DeleteCategoryException;
import com.madirex.funkosspringrest.models.Category;
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
     * @throws CategoryNotValidException CategoryNotValidException
     * @throws CategoryNotFoundException CategoryNotFoundException
     */
    Category getCategoryById(Long id) throws CategoryNotValidException, CategoryNotFoundException;

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
     * @throws CategoryNotValidException CategoryNotValidException
     * @throws CategoryNotFoundException CategoryNotFoundException
     */
    Category putCategory(Long id, UpdateCategoryDTO category) throws CategoryNotValidException, CategoryNotFoundException;

    /**
     * Actualiza una categoría parcialmente
     *
     * @param id       id
     * @param category category
     * @return Category
     * @throws CategoryNotValidException CategoryNotValidException
     * @throws CategoryNotFoundException CategoryNotFoundException
     */
    Category patchCategory(Long id, PatchCategoryDTO category) throws CategoryNotValidException, CategoryNotFoundException;

    /**
     * Elimina una categoría
     *
     * @param id id
     * @throws CategoryNotFoundException CategoryNotFoundException
     * @throws DeleteCategoryException   DeleteCategoryException
     */
    void deleteCategory(Long id) throws CategoryNotFoundException, DeleteCategoryException;
}
