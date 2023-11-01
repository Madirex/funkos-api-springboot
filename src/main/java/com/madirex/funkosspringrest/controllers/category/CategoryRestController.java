package com.madirex.funkosspringrest.controllers.category;

import com.madirex.funkosspringrest.dto.category.CreateCategoryDTO;
import com.madirex.funkosspringrest.dto.category.PatchCategoryDTO;
import com.madirex.funkosspringrest.dto.category.UpdateCategoryDTO;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotFoundException;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotValidIDException;
import com.madirex.funkosspringrest.exceptions.category.DeleteCategoryException;
import com.madirex.funkosspringrest.models.Category;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * Interface CategoryRestController
 */
public interface CategoryRestController {
    ResponseEntity<List<Category>> findAll();

    ResponseEntity<Category> findById(@Valid @PathVariable Long id) throws CategoryNotValidIDException, CategoryNotFoundException;

    ResponseEntity<Category> post(@Valid @RequestBody CreateCategoryDTO category);

    ResponseEntity<Category> put(@Valid @PathVariable Long id, @Valid @RequestBody UpdateCategoryDTO category) throws CategoryNotValidIDException, CategoryNotFoundException;

    ResponseEntity<Category> patch(@Valid @PathVariable Long id, @Valid @RequestBody PatchCategoryDTO category) throws CategoryNotValidIDException, CategoryNotFoundException;

    ResponseEntity<String> delete(@Valid @PathVariable Long id) throws CategoryNotFoundException;

    Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex);
}
