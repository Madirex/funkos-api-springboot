package com.madirex.funkosspringrest.services.category;

import com.madirex.funkosspringrest.dto.category.CreateCategoryDTO;
import com.madirex.funkosspringrest.dto.category.PatchCategoryDTO;
import com.madirex.funkosspringrest.dto.category.UpdateCategoryDTO;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotFoundException;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotValidIDException;
import com.madirex.funkosspringrest.exceptions.category.DeleteCategoryException;
import com.madirex.funkosspringrest.models.Category;

import java.util.List;

/**
 * Interface CategoryService
 */
public interface CategoryService {
    List<Category> getAllCategory();

    Category getCategoryById(Long id) throws CategoryNotValidIDException, CategoryNotFoundException;

    Category postCategory(CreateCategoryDTO category);

    Category putCategory(Long id, UpdateCategoryDTO category) throws CategoryNotValidIDException, CategoryNotFoundException;

    Category patchCategory(Long id, PatchCategoryDTO category) throws CategoryNotValidIDException, CategoryNotFoundException;

    void deleteCategory(Long id) throws CategoryNotFoundException, DeleteCategoryException;
}
