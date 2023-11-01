package com.madirex.funkosspringrest.funko;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.madirex.funkosspringrest.dto.funko.CreateFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.GetFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.PatchFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.UpdateFunkoDTO;
import com.madirex.funkosspringrest.models.Category;
import com.madirex.funkosspringrest.services.funko.FunkoServiceImpl;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class FunkoControllerImplTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @MockBean
    FunkoServiceImpl service;
    GetFunkoDTO funko = GetFunkoDTO.builder()
            .id(UUID.randomUUID())
            .name("nombre")
            .price(2.2)
            .quantity(2)
            .image("https://madirex.com/favicon.ico")
            .category(Category.builder().id(1L).type(Category.Type.MOVIE).active(true).build())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    GetFunkoDTO funko2 = GetFunkoDTO.builder()
            .id(UUID.randomUUID())
            .name("nombre2")
            .price(23.22)
            .quantity(42)
            .image("https://tech.madirex.com/favicon.ico")
            .category(Category.builder().id(1L).type(Category.Type.MOVIE).active(true).build())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    String endpoint = "/api/funkos";

    @Test
    @Order(1)
    void getAllTest() throws Exception {
        var funkoList = List.of(funko, funko2);
        Mockito.when(service.getAllFunko()).thenReturn(funkoList);
        MockHttpServletResponse response = mockMvc.perform(get(endpoint)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("\"name\":" + "\"" + funko.getName() + "\"")),
                () -> assertTrue(response.getContentAsString().contains("\"price\":" + funko.getPrice())),
                () -> assertTrue(response.getContentAsString().contains("\"quantity\":" + funko.getQuantity())),
                () -> assertTrue(response.getContentAsString().contains("\"image\":" + "\"" + funko.getImage() + "\""))
        );
    }

    @Test
    @Order(2)
    void getFilteredByCategoryTest() throws Exception {
        var category = "Superhéroes";
        var funkoList = List.of(funko, funko2);
        Mockito.when(service.getAllFunkoFilterByCategory(service.getAllFunko(), category)).thenReturn(funkoList);
        MockHttpServletResponse response = mockMvc.perform(get(endpoint)
                        .param("category", category)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("\"name\":" + "\"" + funko.getName() + "\"")),
                () -> assertTrue(response.getContentAsString().contains("\"name\":" + "\"" + funko2.getName() + "\"")));
    }

    @Test
    @Order(3)
    void findByIdTest() throws Exception {
        Mockito.when(service.getFunkoById(funko.getId().toString())).thenReturn(funko);
        MockHttpServletResponse response = mockMvc.perform(
                        get(endpoint + "/{id}", funko.getId().toString())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("\"name\":" + "\"" + funko.getName() + "\"")),
                () -> assertTrue(response.getContentAsString().contains("\"price\":" + funko.getPrice())),
                () -> assertTrue(response.getContentAsString().contains("\"quantity\":" + funko.getQuantity())),
                () -> assertTrue(response.getContentAsString().contains("\"image\":" + "\"" + funko.getImage() + "\""))
        );
    }

    @Test
    @Order(5)
    void testDeleteFunko() throws Exception {
        String funkoIdToDelete = UUID.randomUUID().toString();
        service.deleteFunko(funkoIdToDelete);
        mockMvc.perform(delete(endpoint + "/" + funkoIdToDelete))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(6)
    void testPostFunko() throws Exception {
        CreateFunkoDTO newFunko = CreateFunkoDTO.builder()
                .name("Nuevo Funko")
                .price(10.99)
                .quantity(5)
                .image("https://www.madirex/favicon.png")
                .categoryId(1L)
                .build();

        GetFunkoDTO createdFunko = GetFunkoDTO.builder()
                .id(UUID.randomUUID())
                .name(newFunko.getName())
                .price(newFunko.getPrice())
                .quantity(newFunko.getQuantity())
                .image(newFunko.getImage())
                .category(Category.builder().id(1L).type(Category.Type.MOVIE).active(true).build())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Mockito.when(service.postFunko(newFunko)).thenReturn(createdFunko);
        mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newFunko)));
    }

    @Test
    @Order(7)
    void testPutFunko() throws Exception {
        String funkId = UUID.randomUUID().toString();

        UpdateFunkoDTO patchedFunko = UpdateFunkoDTO.builder()
                .price(14.99)
                .quantity(7)
                .name("test")
                .image("Imagen")
                .categoryId(1L)
                .build();

        GetFunkoDTO patchedFunkoResponse = GetFunkoDTO.builder()
                .id(UUID.fromString(funkId))
                .name("Nombre existente del Funko")
                .price(patchedFunko.getPrice())
                .quantity(patchedFunko.getQuantity())
                .image("Imagen existente del Funko")
                .category(Category.builder().id(1L).type(Category.Type.MOVIE).active(true).build())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Mockito.when(service.putFunko(eq(funkId), eq(patchedFunko))).thenReturn(patchedFunkoResponse);

        mockMvc.perform(MockMvcRequestBuilders.put(endpoint + "/" + funkId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(patchedFunko)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    void testPatchFunko() throws Exception {
        String funkId = UUID.randomUUID().toString();

        PatchFunkoDTO patchedFunko = PatchFunkoDTO.builder()
                .price(14.99)
                .quantity(7)
                .build();

        GetFunkoDTO patchedFunkoResponse = GetFunkoDTO.builder()
                .id(UUID.fromString(funkId))
                .name("Nombre existente del Funko")
                .price(patchedFunko.getPrice())
                .quantity(patchedFunko.getQuantity())
                .image("Imagen existente del Funko")
                .category(Category.builder().id(1L).type(Category.Type.MOVIE).active(true).build())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Mockito.when(service.patchFunko(eq(funkId), eq(patchedFunko))).thenReturn(patchedFunkoResponse);

        mockMvc.perform(MockMvcRequestBuilders.patch(endpoint + "/" + funkId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(patchedFunko)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(9)
    void testValidationExceptionHandling() throws Exception {
        CreateFunkoDTO invalidFunko = CreateFunkoDTO.builder()
                .name("")
                .price(-5.0)
                .quantity(0)
                .image("invalid-image-url")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidFunko)))
                .andExpect(status().isBadRequest());
    }

}
