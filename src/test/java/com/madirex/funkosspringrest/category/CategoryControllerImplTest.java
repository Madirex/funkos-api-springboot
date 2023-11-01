package com.madirex.funkosspringrest.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.madirex.funkosspringrest.dto.category.CreateCategoryDTO;
import com.madirex.funkosspringrest.dto.category.PatchCategoryDTO;
import com.madirex.funkosspringrest.dto.category.UpdateCategoryDTO;
import com.madirex.funkosspringrest.models.Category;
import com.madirex.funkosspringrest.services.category.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class CategoryControllerImplTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @MockBean
    CategoryServiceImpl service;
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

    @Test
    @Order(1)
    void getAllTest() throws Exception {
        var categoryList = List.of(category, category2);
        Mockito.when(service.getAllCategory()).thenReturn(categoryList);
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

    @Test
    @Order(3)
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

    @Test
    @Order(5)
    void testDeleteCategory() throws Exception {
        Long categoryIdToDelete = 1L;
        service.deleteCategory(categoryIdToDelete);
        mockMvc.perform(delete(endpoint + "/" + categoryIdToDelete))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(6)
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
                .content(mapper.writeValueAsString(newCategory)));
    }

    @Test
    @Order(7)
    void testPutCategory() throws Exception {
        Long funkId = 1L;

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

        Mockito.when(service.putCategory(eq(funkId), eq(patchedCategory))).thenReturn(patchedCategoryResponse);

        mockMvc.perform(MockMvcRequestBuilders.put(endpoint + "/" + funkId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(patchedCategory)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    void testPatchCategory() throws Exception {
        Long funkId = 1L;

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

        Mockito.when(service.patchCategory(eq(funkId), eq(patchedCategory))).thenReturn(patchedCategoryResponse);

        mockMvc.perform(MockMvcRequestBuilders.patch(endpoint + "/" + funkId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(patchedCategory)))
                .andExpect(status().isOk());
    }

}
