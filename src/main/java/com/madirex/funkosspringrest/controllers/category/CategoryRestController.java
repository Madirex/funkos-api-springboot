package com.madirex.funkosspringrest.controllers.category;

import com.madirex.funkosspringrest.dto.category.CreateCategoryDTO;
import com.madirex.funkosspringrest.dto.category.PatchCategoryDTO;
import com.madirex.funkosspringrest.dto.category.UpdateCategoryDTO;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotFoundException;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotValidException;
import com.madirex.funkosspringrest.models.Category;
import com.madirex.funkosspringrest.utils.pagination.PageResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

/**
 * Interface CategoryRestController
 */
public interface CategoryRestController {

    @GetMapping()
    ResponseEntity<PageResponse<Category>> findAll(
            @RequestParam(required = false) Optional<String> type,
            @RequestParam(required = false) Optional<Boolean> isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    );

    ResponseEntity<Category> findById(@Valid @PathVariable Long id) throws CategoryNotValidException, CategoryNotFoundException;

    ResponseEntity<Category> post(@Valid @RequestBody CreateCategoryDTO category);

    ResponseEntity<Category> put(@Valid @PathVariable Long id, @Valid @RequestBody UpdateCategoryDTO category) throws CategoryNotValidException, CategoryNotFoundException;

    ResponseEntity<Category> patch(@Valid @PathVariable Long id, @Valid @RequestBody PatchCategoryDTO category) throws CategoryNotValidException, CategoryNotFoundException;

    ResponseEntity<String> delete(@Valid @PathVariable Long id) throws CategoryNotFoundException;

}
