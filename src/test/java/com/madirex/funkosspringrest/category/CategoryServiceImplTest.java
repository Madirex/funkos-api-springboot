package com.madirex.funkosspringrest.category;

import com.madirex.funkosspringrest.dto.category.CreateCategoryDTO;
import com.madirex.funkosspringrest.dto.category.PatchCategoryDTO;
import com.madirex.funkosspringrest.dto.category.UpdateCategoryDTO;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotFoundException;
import com.madirex.funkosspringrest.mappers.category.CategoryMapperImpl;
import com.madirex.funkosspringrest.models.Category;
import com.madirex.funkosspringrest.repositories.CategoryRepository;
import com.madirex.funkosspringrest.repositories.FunkoRepository;
import com.madirex.funkosspringrest.services.category.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Clase CategoryServiceImplTest
 */
@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    List<Category> list;
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private FunkoRepository funkoRepository;

    @Mock
    private CategoryMapperImpl categoryMapperImpl;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    /**
     * Método setUp para inicializar los objetos
     */
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

    /**
     * Test para comprobar que se obtienen todas las categorías
     */
    @Test
    void testGetAllCategory() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Category> expectedPage = new PageImpl<>(list);
        when(categoryRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        Page<Category> actualPage = categoryService.getAllCategory(Optional.empty(), Optional.empty()
                , pageable);
        var list3 = actualPage.getContent();
        assertAll("Category properties",
                () -> assertEquals(2, list.size(), "La lista debe contener 2 elementos"),
                () -> assertEquals(2, list3.size(), "La lista debe contener 2 elementos"),
                () -> assertEquals(list.get(0).getType(), list3.get(0).getType(), "El tipo debe coincidir"),
                () -> assertEquals(list.get(0).getActive(), list3.get(0).getActive(), "El estado debe coincidir"),
                () -> assertEquals(list.get(1).getType(), list3.get(1).getType(), "El tipo debe coincidir"),
                () -> assertEquals(list.get(1).getActive(), list3.get(1).getActive(), "El estado debe coincidir")
        );
        verify(categoryRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }


    /**
     * Test para comprobar que se obtiene una categoría por su id
     *
     * @throws CategoryNotFoundException excepción
     */
    @Test
    void testGetCategoryById() throws CategoryNotFoundException {
        when(categoryRepository.findById(list.get(0).getId())).thenReturn(Optional.ofNullable(list.get(0)));
        var category = categoryService.getCategoryById(list.get(0).getId());
        assertAll("Category properties",
                () -> assertEquals(list.get(0).getId(), category.getId(), "El id debe coincidir"),
                () -> assertEquals(list.get(0).getType(), category.getType(), "El tipo debe coincidir"),
                () -> assertEquals(list.get(0).getActive(), category.getActive(), "El estado debe coincidir")
        );
        verify(categoryRepository, times(1)).findById(list.get(0).getId());
    }

    /**
     * Test para comprobar la excepción cuando no se encuentra la categoría dado un ID
     */
    @Test
    void testGetCategoryByIdNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(1L));
        verify(categoryRepository, times(1)).findById(1L);
    }

    /**
     * Test para comprobar que se crea una categoría
     */
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

    /**
     * Test para comprobar que se actualiza una categoría
     *
     * @throws CategoryNotFoundException excepción
     */
    @Test
    void testPutCategory() throws CategoryNotFoundException {
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

    /**
     * Test para comprobar que se puede actualizar parcialmente una categoría
     *
     * @throws CategoryNotFoundException excepción
     */
    @Test
    void testPatchCategory() throws CategoryNotFoundException {
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

    /**
     * Test para comprobar que se lanza una excepción cuando no se encuentra la categoría al tratar de hacer un Patch
     */
    @Test
    void testPatchCategoryNotFoundCategory() {
        var fp = PatchCategoryDTO.builder()
                .type(Category.Type.MOVIE)
                .build();
        assertThrows(CategoryNotFoundException.class, () -> categoryService.patchCategory(1L, fp));
    }

    /**
     * Test que comprueba que se pueda eliminar la categoría
     *
     * @throws CategoryNotFoundException excepción
     */
    @Test
    void testDeleteCategory() throws CategoryNotFoundException {
        var update = PatchCategoryDTO.builder()
                .type(Category.Type.MOVIE)
                .build();
        when(categoryRepository.findById(list.get(0).getId())).thenReturn(Optional.of(list.get(0)));
        when(categoryRepository.save(list.get(0))).thenReturn(list.get(0));
        var inserted = categoryService.patchCategory(list.get(0).getId(), update);
        categoryService.deleteCategory(1L);
        assertNotNull(inserted);
        assertAll("Category properties",
                () -> assertEquals(inserted.getType(), update.getType(), "El tipo debe coincidir"),
                () -> assertEquals(inserted.getActive(), list.get(0).getActive(), "El estado debe coincidir"),
                () -> assertDoesNotThrow(() -> categoryService.deleteCategory(1L))
        );
    }

    /**
     * Test que comprueba que se lanza una excepción NotFound cuando no se encuentra la categoría al tratar de eliminarla
     */
    @Test
    void testDeleteCategoryNotFound() {
        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory(1L));
    }
}
