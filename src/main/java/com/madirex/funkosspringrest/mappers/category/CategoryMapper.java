package com.madirex.funkosspringrest.mappers.category;

import com.madirex.funkosspringrest.dto.category.CreateCategoryDTO;
import com.madirex.funkosspringrest.dto.category.UpdateCategoryDTO;
import com.madirex.funkosspringrest.models.Category;

public interface CategoryMapper {
    Category toCategory(CreateCategoryDTO dto);

    Category toCategory(Category existingCategory, UpdateCategoryDTO dto);

}
