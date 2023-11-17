package com.madirex.funkosspringrest.controllers.category;

import com.madirex.funkosspringrest.dto.category.CreateCategoryDTO;
import com.madirex.funkosspringrest.dto.category.PatchCategoryDTO;
import com.madirex.funkosspringrest.dto.category.UpdateCategoryDTO;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotFoundException;
import com.madirex.funkosspringrest.models.Category;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Interface CategoryRestController
 */
public interface CategoryRestController {

    /**
     * Método para obtener una categoría por su id
     *
     * @param id id de la categoría
     * @return ResponseEntity con el código de estado
     * @throws CategoryNotFoundException de la categoría
     */
    ResponseEntity<Category> findById(@Valid @PathVariable Long id) throws CategoryNotFoundException;

    /**
     * Método para crear una categoría
     *
     * @param category categoría a crear
     * @return ResponseEntity con el código de estado
     */
    ResponseEntity<Category> post(@Valid @RequestBody CreateCategoryDTO category);

    /**
     * Método para actualizar una categoría
     *
     * @param id       id de la categoría
     * @param category categoría a actualizar
     * @return ResponseEntity con el código de estado
     * @throws CategoryNotFoundException de la categoría
     */
    ResponseEntity<Category> put(@Valid @PathVariable Long id, @Valid @RequestBody UpdateCategoryDTO category) throws CategoryNotFoundException;

    /**
     * Método para actualizar parcialmente una categoría
     *
     * @param id       id de la categoría
     * @param category categoría a actualizar
     * @return ResponseEntity con el código de estado
     * @throws CategoryNotFoundException de la categoría
     */
    ResponseEntity<Category> patch(@Valid @PathVariable Long id, @Valid @RequestBody PatchCategoryDTO category) throws CategoryNotFoundException;

    /**
     * Método para eliminar una categoría
     *
     * @param id id de la categoría
     * @return ResponseEntity con el código de estado
     * @throws CategoryNotFoundException de la categoría
     */
    ResponseEntity<String> delete(@Valid @PathVariable Long id) throws CategoryNotFoundException;

}
