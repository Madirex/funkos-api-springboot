package com.madirex.funkosspringrest.rest.entities.category;

import com.madirex.funkosspringrest.rest.entities.category.dto.CreateCategoryDTO;
import com.madirex.funkosspringrest.rest.entities.category.dto.UpdateCategoryDTO;
import com.madirex.funkosspringrest.rest.entities.category.mappers.CategoryMapperImpl;
import com.madirex.funkosspringrest.rest.entities.category.models.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Clase CategoryMapperImplTest
 */
class CategoryMapperImplTest {
    private CategoryMapperImpl categoryMapperImpl;

    /**
     * Método setUp para inicializar los objetos
     */
    @BeforeEach
    void setUp() {
        categoryMapperImpl = new CategoryMapperImpl();
    }

    /**
     * Test para comprobar que el mapeo de CreateCategory a Category es correcto
     */
    @Test
    void testCreateCategoryDTOToCategory() {
        var category = CreateCategoryDTO.builder()
                .type("MOVIE")
                .active(true)
                .build();
        var mapped = categoryMapperImpl.toCategory(category);
        assertAll("Category properties",
                () -> assertEquals(category.getType(), mapped.getType(), "El tipo debe coincidir"),
                () -> assertEquals(category.getActive(), mapped.getActive(), "El estado debe coincidir")
        );
    }

    /**
     * Test para comprobar que el mapeo de un UpdateCategory a DTO es correcto
     */
    @Test
    void testUpdateCategoryDTOToCategory() {
        var existingCategory = Category.builder()
                .type("MOVIE")
                .active(true)
                .build();
        var category = UpdateCategoryDTO.builder()
                .type("MOVIE")
                .active(true)
                .build();
        var mapped = categoryMapperImpl.toCategory(existingCategory, category);
        assertAll("Category properties",
                () -> assertEquals(category.getType(), mapped.getType(), "El tipo debe coincidir"),
                () -> assertEquals(category.getActive(), mapped.getActive(), "El estado debe coincidir")
        );
    }
}
