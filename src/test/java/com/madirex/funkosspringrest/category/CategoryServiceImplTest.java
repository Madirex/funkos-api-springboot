package com.madirex.funkosspringrest.category;

import com.madirex.funkosspringrest.dto.category.CreateCategoryDTO;
import com.madirex.funkosspringrest.dto.category.PatchCategoryDTO;
import com.madirex.funkosspringrest.dto.category.UpdateCategoryDTO;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotFoundException;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotValidIDException;
import com.madirex.funkosspringrest.exceptions.category.DeleteCategoryException;
import com.madirex.funkosspringrest.mappers.category.CategoryMapperImpl;
import com.madirex.funkosspringrest.models.Category;
import com.madirex.funkosspringrest.repositories.CategoryRepository;
import com.madirex.funkosspringrest.services.category.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    List<Category> list;
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapperImpl categoryMapperImpl;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        list = new ArrayList<>();
        list.add(Category.builder()
                .id(1L)
                .type(Category.Type.MOVIE)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
        list.add(Category.builder()
                .id(2L)
                .type(Category.Type.DISNEY)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
    }

    @Test
    void testGetAllCategory() {
        when(categoryRepository.findAll()).thenReturn(list);
        var list3 = categoryService.getAllCategory();
        assertAll("Category properties",
                () -> assertEquals(2, list.size(), "La lista debe contener 2 elementos"),
                () -> assertEquals(2, list3.size(), "La lista debe contener 2 elementos"),
                () -> assertEquals(list.get(0).getType(), list3.get(0).getType(), "El tipo debe coincidir"),
                () -> assertEquals(list.get(0).getActive(), list3.get(0).getActive(), "El estado debe coincidir"),
                () -> assertEquals(list.get(1).getType(), list3.get(1).getType(), "El tipo debe coincidir"),
                () -> assertEquals(list.get(1).getActive(), list3.get(1).getActive(), "El estado debe coincidir")
        );
        verify(categoryRepository, times(1)).findAll();
    }


    @Test
    void testGetCategoryById() throws CategoryNotValidIDException, CategoryNotFoundException {
        when(categoryRepository.findById(list.get(0).getId())).thenReturn(Optional.ofNullable(list.get(0)));
        var category = categoryService.getCategoryById(list.get(0).getId());
        assertAll("Category properties",
                () -> assertEquals(list.get(0).getId(), category.getId(), "El id debe coincidir"),
                () -> assertEquals(list.get(0).getType(), category.getType(), "El tipo debe coincidir"),
                () -> assertEquals(list.get(0).getActive(), category.getActive(), "El estado debe coincidir")
        );
        verify(categoryRepository, times(1)).findById(list.get(0).getId());
    }

    @Test
    void testGetCategoryByIdNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(1L));
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void testPostCategory() {
        var insert = CreateCategoryDTO.builder()
                .type(Category.Type.MOVIE)
                .active(true)
                .build();
        when(categoryMapperImpl.toCategory(insert)).thenReturn(list.get(0));
        when(categoryRepository.save(list.get(0))).thenReturn(list.get(0));
        var inserted = categoryService.postCategory(insert);
        assertNotNull(inserted);
        assertAll("Category properties",
                () -> assertEquals(inserted.getType(), insert.getType(), "El tipo debe coincidir"),
                () -> assertEquals(inserted.getActive(), insert.getActive(), "El estado debe coincidir")
        );
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testPutCategory() throws CategoryNotFoundException, CategoryNotValidIDException {
        var update = UpdateCategoryDTO.builder()
                .type(Category.Type.MOVIE)
                .active(true)
                .build();
        when(categoryRepository.findById(list.get(0).getId())).thenReturn(Optional.of(list.get(0)));
        when(categoryMapperImpl.toCategory(list.get(0), update)).thenReturn(list.get(0));
        when(categoryRepository.save(list.get(0))).thenReturn(list.get(0));
        var inserted = categoryService.putCategory(list.get(0).getId(), update);
        assertNotNull(inserted);
        assertAll("Category properties",
                () -> assertEquals(inserted.getType(), update.getType(), "El tipo debe coincidir"),
                () -> assertEquals(inserted.getActive(), update.getActive(), "El estado debe coincidir")
        );
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testPatchCategory() throws CategoryNotFoundException, CategoryNotValidIDException {
        var update = PatchCategoryDTO.builder()
                .type(Category.Type.MOVIE)
                .build();
        when(categoryRepository.findById(list.get(0).getId())).thenReturn(Optional.of(list.get(0)));
        when(categoryRepository.save(list.get(0))).thenReturn(list.get(0));
        var inserted = categoryService.patchCategory(list.get(0).getId(), update);
        assertNotNull(inserted);
        assertAll("Category properties",
                () -> assertEquals(inserted.getType(), update.getType(), "El tipo debe coincidir"),
                () -> assertEquals(inserted.getActive(), list.get(0).getActive(), "El estado debe coincidir")
        );
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testPatchCategoryNotFoundCategory() {
        var fp = PatchCategoryDTO.builder()
                .type(Category.Type.MOVIE)
                .build();
        assertThrows(CategoryNotFoundException.class, () -> categoryService.patchCategory(1L, fp));
    }

    @Test
    void testDeleteCategory() throws CategoryNotFoundException, DeleteCategoryException {
        when(categoryRepository.findById(1L)).thenReturn(Optional.ofNullable(list.get(0)));
        doNothing().when(categoryRepository).delete(any(Category.class));
        categoryService.deleteCategory(1L);
        assertEquals(0, categoryService.getAllCategory().size());
        verify(categoryRepository, times(1)).delete(any(Category.class));
    }

    @Test
    void testDeleteCategoryNotFound() {
        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory(1L));
    }
}
