package com.madirex.funkosspringrest.funko;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.madirex.funkosspringrest.rest.entities.funko.dto.CreateFunkoDTO;
import com.madirex.funkosspringrest.rest.entities.funko.dto.GetFunkoDTO;
import com.madirex.funkosspringrest.rest.entities.funko.dto.PatchFunkoDTO;
import com.madirex.funkosspringrest.rest.entities.funko.dto.UpdateFunkoDTO;
import com.madirex.funkosspringrest.rest.entities.funko.exceptions.FunkoNotValidUUIDException;
import com.madirex.funkosspringrest.rest.entities.category.models.Category;
import com.madirex.funkosspringrest.rest.entities.funko.services.FunkoServiceImpl;
import org.junit.jupiter.api.Test;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Clase FunkoControllerImplTest
 */
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

    /**
     * Constructor de FunkoControllerImplTest
     *
     * @param service servicio de Funko
     */
    @Autowired
    public FunkoControllerImplTest(FunkoServiceImpl service) {
        this.service = service;
        mapper.registerModule(new JavaTimeModule());
    }

    /**
     * Test para comprobar que se obtienen todos los Funkos
     *
     * @throws Exception excepción
     */
    @Test
    void testGetAll() throws Exception {
        var funkoList = List.of(funko, funko2);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(funkoList);
        when(service.getAllFunko(Optional.empty(), Optional.empty(), Optional.empty(),
                pageable)).thenReturn(page);
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
     * Test para comprobar que se obtiene un Funko por su id
     *
     * @throws Exception excepción
     */
    @Test
    void testFindById() throws Exception {
        when(service.getFunkoById(funko.getId().toString())).thenReturn(funko);
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

    /**
     * Test para comprobar que se obtiene un Funko por su id
     *
     * @throws Exception excepción
     */
    @Test
    void testFindByIdNotValidUUID() throws Exception {
        when(service.getFunkoById("()")).thenThrow(new FunkoNotValidUUIDException(""));
        MockHttpServletResponse response = mockMvc.perform(
                        get(endpoint + "/{id}", "()")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        assertEquals(400, response.getStatus());
    }

    /**
     * Test para comprobar que se obtiene un Funko por su id
     *
     * @throws Exception excepción
     */
    @Test
    void testDeleteFunko() throws Exception {
        String funkoIdToDelete = UUID.randomUUID().toString();
        service.deleteFunko(funkoIdToDelete);
        mockMvc.perform(delete(endpoint + "/" + funkoIdToDelete))
                .andExpect(status().isNoContent());
    }

    /**
     * Test para comprobar UUID no válida al intentar eliminar a un Funko
     *
     * @throws Exception excepción
     */
    @Test
    void testDeleteNotValidUUID() throws Exception {
        doThrow(new FunkoNotValidUUIDException("")).when(service).deleteFunko("()");
        MockHttpServletResponse response = mockMvc.perform(
                        delete(endpoint + "/{id}", "()")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        assertEquals(400, response.getStatus());
    }

    /**
     * Test para comprobar que se crea un Funko
     *
     * @throws Exception excepción
     */
    @Test
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

        when(service.postFunko(newFunko)).thenReturn(createdFunko);
        mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newFunko)))
                .andExpect(status().isCreated());

    }

    /**
     * Test para comprobar que se actualiza un Funko
     *
     * @throws Exception excepción
     */
    @Test
    void testPutFunko() throws Exception {
        String funkId = UUID.randomUUID().toString();

        UpdateFunkoDTO updatedFunko = UpdateFunkoDTO.builder()
                .price(14.99)
                .quantity(7)
                .name("test")
                .image("Imagen")
                .categoryId(1L)
                .build();

        GetFunkoDTO updatedFunkoResponse = GetFunkoDTO.builder()
                .id(UUID.fromString(funkId))
                .name("Nombre existente del Funko")
                .price(updatedFunko.getPrice())
                .quantity(updatedFunko.getQuantity())
                .image("Imagen existente del Funko")
                .category(Category.builder().id(1L).type(Category.Type.MOVIE).active(true).build())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(service.putFunko(any(), eq(updatedFunko))).thenReturn(updatedFunkoResponse);

        mockMvc.perform(MockMvcRequestBuilders.put(endpoint + "/" + funkId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedFunko)))
                .andExpect(status().isOk());
    }

    /**
     * Test para comprobar UUID no válida al hacer un Put
     *
     * @throws Exception excepción
     */
    @Test
    void testPutNotValidUUID() throws Exception {
        doThrow(new FunkoNotValidUUIDException("")).when(service).putFunko("a()a", UpdateFunkoDTO.builder()
                .price(14.99)
                .quantity(7)
                .name("test")
                .image("Imagen")
                .categoryId(1L)
                .build());
        MockHttpServletResponse response = mockMvc.perform(
                        put(endpoint + "/{id}", "a()a")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        assertEquals(400, response.getStatus());
    }

    /**
     * Test para comprobar que se actualiza parcialmente un Funko
     *
     * @throws Exception excepción
     */
    @Test
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

        when(service.patchFunko(any(), eq(patchedFunko))).thenReturn(patchedFunkoResponse);

        mockMvc.perform(MockMvcRequestBuilders.patch(endpoint + "/" + funkId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(patchedFunko)))
                .andExpect(status().isOk());
    }

    /**
     * Test de ValidationException
     *
     * @throws Exception excepción
     */
    @Test
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

    /**
     * Test para comprobar que se obtienen todos los Funkos
     *
     * @throws Exception excepción
     */
    @Test
    void updateFunkoImage() throws Exception {
        var myLocalEndpoint = endpoint + "/image/" + funko.getId().toString();

        when(service.updateImage(anyString(), any(MultipartFile.class), anyBoolean())).thenReturn(funko);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "filename.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "contenido del archivo".getBytes()
        );

        MockHttpServletResponse response = mockMvc.perform(
                multipart(myLocalEndpoint)
                        .file(file)
                        .with(req -> {
                            req.setMethod("PATCH");
                            return req;
                        })
        ).andReturn().getResponse();

        var res = mapper.readValue(response.getContentAsString(), GetFunkoDTO.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(funko.getId(), res.getId())
        );

        verify(service, times(1)).updateImage(anyString(), any(MultipartFile.class), anyBoolean());
    }

    /**
     * Test para comprobar cuando no se le asigna una imagen cuando se intenta actualizar la imagen de un Funko
     *
     * @throws Exception excepción
     */
    @Test
    void testUpdateFunkoImageNoImageProvided() throws Exception {
        var myLocalEndpoint = endpoint + "/image/" + funko.getId().toString();
        when(service.updateImage(anyString(), any(MultipartFile.class), anyBoolean()))
                .thenReturn(funko); // Esto no se va a invocar debido a la excepción

        MockHttpServletResponse response = mockMvc.perform(
                multipart(myLocalEndpoint)
                        .file(new MockMultipartFile("file", "", "text/plain", new byte[0]))
                        .with(requestPostProcessor -> {
                            requestPostProcessor.setMethod("PATCH");
                            return requestPostProcessor;
                        })
        ).andReturn().getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("No se ha enviado una imagen para el Funko"));

        verify(service, never()).updateImage(anyString(), any(MultipartFile.class), anyBoolean());
    }

}
