package com.madirex.funkosspringrest.category;

import com.madirex.funkosspringrest.dto.category.CreateCategoryDTO;
import com.madirex.funkosspringrest.dto.category.UpdateCategoryDTO;
import com.madirex.funkosspringrest.mappers.category.CategoryMapperImpl;
import com.madirex.funkosspringrest.models.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryMapperImplTest {
    private CategoryMapperImpl categoryMapperImpl;

    @BeforeEach
    void setUp() {
        categoryMapperImpl = new CategoryMapperImpl();
    }

    @Test
    void testCreateCategoryDTOToCategory() {
        var category = CreateCategoryDTO.builder()
                .type(Category.Type.MOVIE)
                .active(true)
                .build();
        var mapped = categoryMapperImpl.toCategory(category);
        assertAll("Category properties",
                () -> assertEquals(category.getType(), mapped.getType(), "El tipo debe coincidir"),
                () -> assertEquals(category.getActive(), mapped.getActive(), "El estado debe coincidir")
        );
    }

    @Test
    void testUpdateCategoryDTOToCategory() {
        var category = UpdateCategoryDTO.builder()
                .type(Category.Type.MOVIE)
                .active(true)
                .build();
        var mapped = categoryMapperImpl.toCategory(existingCategory, category);
        assertAll("Category properties",
                () -> assertEquals(category.getType(), mapped.getType(), "El tipo debe coincidir"),
                () -> assertEquals(category.getActive(), mapped.getActive(), "El estado debe coincidir")
        );
    }
}
