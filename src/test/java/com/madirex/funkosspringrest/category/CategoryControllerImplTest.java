package com.madirex.funkosspringrest.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.madirex.funkosspringrest.rest.entities.category.dto.CreateCategoryDTO;
import com.madirex.funkosspringrest.rest.entities.category.dto.PatchCategoryDTO;
import com.madirex.funkosspringrest.rest.entities.category.dto.UpdateCategoryDTO;
import com.madirex.funkosspringrest.rest.entities.category.exceptions.DeleteCategoryException;
import com.madirex.funkosspringrest.rest.entities.category.models.Category;
import com.madirex.funkosspringrest.rest.entities.category.services.CategoryServiceImpl;
import com.madirex.funkosspringrest.rest.entities.funko.services.FunkoServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Clase CategoryControllerImplTest
 */
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class CategoryControllerImplTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @MockBean
    CategoryServiceImpl service;
    @MockBean
    FunkoServiceImpl funkoService;

    Category category = Category.builder()
            .id(1L)
            .type(Category.Type.MOVIE)
            .active(true)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    Category category2 = Category.builder()
            .id(2L)
            .type(Category.Type.SERIE)
            .active(false)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    String endpoint = "/api/category";

    /**
     * Test para comprobar que se obtienen todas las categorías
     *
     * @throws Exception excepción
     */
    @Test
    void getAllTest() throws Exception {
        var categoryList = List.of(category, category2);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(categoryList);
        when(service.getAllCategory(Optional.empty(), Optional.empty(), pageable)).thenReturn(page);
        MockHttpServletResponse response = mockMvc.perform(get(endpoint)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("\"type\":" + "\"" + category.getType() + "\"")),
                () -> assertTrue(response.getContentAsString().contains("\"active\":" + category.getActive()))
        );
    }

    /**
     * Test para comprobar retorno de error cuando page tiene valor no válido
     *
     * @throws Exception excepción
     */
    @Test
    void getAll_ShouldReturnErrorResponse_withInvalidPageParam() throws Exception {
        mockMvc.perform(get(endpoint)
                        .param("page", "-1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    /**
     * Test para comprobar retorno de error cuando size tiene valor no válido
     *
     * @throws Exception excepción
     */
    @Test
    void getAll_ShouldReturnErrorResponse_withInvalidSizeParam() throws Exception {
        mockMvc.perform(get(endpoint)
                        .param("size", "0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    /**
     * Test para comprobar que se obtiene una categoría por su id
     *
     * @throws Exception excepción
     */
    @Test
    void findByIdTest() throws Exception {
        Mockito.when(service.getCategoryById(category.getId())).thenReturn(category);
        MockHttpServletResponse response = mockMvc.perform(
                        get(endpoint + "/{id}", category.getId().toString())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("\"type\":" + "\"" + category.getType() + "\"")),
                () -> assertTrue(response.getContentAsString().contains("\"active\":" + category.getActive()))
        );
    }

    /**
     * Test para comprobar que se crea una categoría
     *
     * @throws Exception excepción
     */
    @Test
    void testPostCategory() throws Exception {
        CreateCategoryDTO newCategory = CreateCategoryDTO.builder()
                .type(Category.Type.MOVIE)
                .active(true)
                .build();

        Category createdCategory = Category.builder()
                .id(1L)
                .type(Category.Type.MOVIE)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Mockito.when(service.postCategory(newCategory)).thenReturn(createdCategory);
        mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newCategory)))
                .andExpect(status().isCreated());
    }

    /**
     * Test para comprobar que se actualiza una categoría
     *
     * @throws Exception excepción
     */
    @Test
    void testPutCategory() throws Exception {
        long funkId = 1L;

        UpdateCategoryDTO patchedCategory = UpdateCategoryDTO.builder()
                .type(Category.Type.MOVIE)
                .active(true)
                .build();

        Category patchedCategoryResponse = Category.builder()
                .id(1L)
                .type(Category.Type.MOVIE)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Mockito.when(service.putCategory(any(), eq(patchedCategory))).thenReturn(patchedCategoryResponse);

        mockMvc.perform(MockMvcRequestBuilders.put(endpoint + "/" + funkId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(patchedCategory)))
                .andExpect(status().isOk());
    }

    /**
     * Test para comprobar que se actualiza parcialmente una categoría
     *
     * @throws Exception excepción
     */
    @Test
    void testPatchCategory() throws Exception {
        long funkId = 1L;

        PatchCategoryDTO patchedCategory = PatchCategoryDTO.builder()
                .type(Category.Type.DISNEY)
                .active(false)
                .build();

        Category patchedCategoryResponse = Category.builder()
                .id(1L)
                .type(Category.Type.MOVIE)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Mockito.when(service.patchCategory(any(), eq(patchedCategory))).thenReturn(patchedCategoryResponse);

        mockMvc.perform(MockMvcRequestBuilders.patch(endpoint + "/" + funkId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(patchedCategory)))
                .andExpect(status().isOk());
    }

    /**
     * Test para comprobar que se elimina una categoría
     *
     * @throws Exception excepción
     */
    @Test
    void testDeleteCategory() throws Exception {
        Long categoryIdToDelete = 1L;
        service.deleteCategory(categoryIdToDelete);
        mockMvc.perform(delete(endpoint + "/" + categoryIdToDelete))
                .andExpect(status().isNoContent());
    }

    /**
     * Test para comprobar que se lanza la excepción CategoryNotFoundException
     * cuando se intenta obtener una categoría que no existe
     *
     * @throws Exception excepción
     */
    @Test
    void testDeleteCategoryFunkoExistsException() throws Exception {
        doThrow(new DeleteCategoryException("Error al eliminar la categoría"))
                .when(service).deleteCategory(anyLong());
        var myLocalEndpoint = endpoint + "/1";
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(400, response.getStatus());
    }
}