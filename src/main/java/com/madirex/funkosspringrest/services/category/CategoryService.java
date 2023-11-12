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

    Page<Category> getAllCategory(Optional<String> type, Optional<Boolean> isActive, Pageable pageable);

    Category getCategoryById(Long id) throws CategoryNotValidException, CategoryNotFoundException;

    Category postCategory(CreateCategoryDTO category);

    Category putCategory(Long id, UpdateCategoryDTO category) throws CategoryNotValidException, CategoryNotFoundException;

    Category patchCategory(Long id, PatchCategoryDTO category) throws CategoryNotValidException, CategoryNotFoundException;

    void deleteCategory(Long id) throws CategoryNotFoundException, DeleteCategoryException;
}
