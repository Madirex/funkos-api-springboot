package com.madirex.funkosspringrest.rest.entities.funko;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.madirex.funkosspringrest.config.websockets.WebSocketConfig;
import com.madirex.funkosspringrest.config.websockets.WebSocketHandler;
import com.madirex.funkosspringrest.rest.entities.funko.dto.CreateFunkoDTO;
import com.madirex.funkosspringrest.rest.entities.funko.dto.GetFunkoDTO;
import com.madirex.funkosspringrest.rest.entities.funko.dto.PatchFunkoDTO;
import com.madirex.funkosspringrest.rest.entities.funko.dto.UpdateFunkoDTO;
import com.madirex.funkosspringrest.rest.entities.category.exceptions.CategoryNotFoundException;
import com.madirex.funkosspringrest.rest.entities.funko.exceptions.FunkoNotFoundException;
import com.madirex.funkosspringrest.rest.entities.funko.exceptions.FunkoNotValidUUIDException;
import com.madirex.funkosspringrest.rest.entities.funko.mappers.FunkoMapperImpl;
import com.madirex.funkosspringrest.rest.entities.funko.mappers.FunkoNotificationMapper;
import com.madirex.funkosspringrest.rest.entities.category.models.Category;
import com.madirex.funkosspringrest.rest.entities.funko.models.Funko;
import com.madirex.funkosspringrest.rest.entities.funko.models.Notification;
import com.madirex.funkosspringrest.rest.entities.funko.repository.FunkoRepository;
import com.madirex.funkosspringrest.rest.entities.category.services.CategoryService;
import com.madirex.funkosspringrest.rest.entities.funko.services.FunkoServiceImpl;
import com.madirex.funkosspringrest.storage.services.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Clase FunkoServiceImplTest
 */
@ExtendWith(MockitoExtension.class)
class FunkoServiceImplTest {
    List<Funko> list;
    @Mock
    private FunkoRepository funkoRepository;

    @Mock
    private FunkoMapperImpl funkoMapperImpl;

    @Mock
    private CategoryService categoryService;

    @Mock
    private StorageService storageService;

    @Mock
    private WebSocketConfig webSocketConfig;

    @Mock
    private WebSocketHandler webSocketHandlerMock;

    @Mock
    private FunkoNotificationMapper funkoNotificationMapper;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private FunkoServiceImpl funkoService;

    /**
     * Método setUp para inicializar los objetos
     */
    @BeforeEach
    void setUp() {
        funkoService.setWebSocketService(webSocketHandlerMock);
        list = new ArrayList<>();
        list.add(Funko.builder()
                .id(UUID.randomUUID())
                .name("Test")
                .price(2.2)
                .quantity(2)
                .image("https://tech.madirex.com/favicon.ico")
                .category(Category.builder().id(1L).type("MOVIE").active(true).build())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
        list.add(Funko.builder()
                .id(UUID.randomUUID())
                .name("Test2")
                .price(42.42)
                .quantity(42)
                .image("https://www.madirex.com/favicon.ico")
                .category(Category.builder().id(1L).type("MOVIE").active(true).build())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
    }

    /**
     * Test para comprobar que se reciben todos los Funko
     */
    @Test
    void testGetAllFunko() {
        List<GetFunkoDTO> list2 = new ArrayList<>();
        list2.add(GetFunkoDTO.builder()
                .id(UUID.randomUUID())
                .name("Test")
                .price(2.2)
                .quantity(2)
                .image("https://tech.madirex.com/favicon.ico")
                .category(Category.builder().id(1L).type("MOVIE").active(true).build())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
        list2.add(GetFunkoDTO.builder()
                .id(UUID.randomUUID())
                .name("Test2")
                .price(42.42)
                .quantity(42)
                .image("https://www.madirex.com/favicon.ico")
                .category(Category.builder().id(1L).type("MOVIE").active(true).build())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Funko> expectedPage = new PageImpl<>(list);
        Specification<Funko> anySpecification = any();
        when(funkoRepository.findAll(anySpecification, any(Pageable.class))).thenReturn(expectedPage);
        when(funkoMapperImpl.toGetFunkoDTO(any(Funko.class))).thenReturn(list2.get(0));
        Page<GetFunkoDTO> actualPage = funkoService.getAllFunko(Optional.empty(), Optional.empty(),
                Optional.empty(), pageable);
        var list3 = actualPage.getContent();
        assertAll("Funko properties",
                () -> assertEquals(2, list.size(), "La lista debe contener 2 elementos"),
                () -> assertEquals(2, list3.size(), "La lista debe contener 2 elementos"),
                () -> assertEquals(list.get(0).getName(), list3.get(0).getName(), "El nombre debe coincidir"),
                () -> assertEquals(list.get(0).getPrice(), list3.get(0).getPrice(), "El precio debe coincidir"),
                () -> assertEquals(list.get(0).getQuantity(), list3.get(0).getQuantity(), "La cantidad debe coincidir")
        );
    }


    /**
     * Test para comprobar que se obtiene un Funko por su id
     *
     * @throws FunkoNotValidUUIDException excepción
     * @throws FunkoNotFoundException     excepción
     */
    @Test
    void testGetFunkoById() throws FunkoNotValidUUIDException, FunkoNotFoundException {
        List<GetFunkoDTO> list2 = new ArrayList<>();
        list2.add(GetFunkoDTO.builder()
                .id(UUID.randomUUID())
                .name("Test")
                .price(2.2)
                .quantity(2)
                .image("https://tech.madirex.com/favicon.ico")
                .category(Category.builder().id(1L).type("MOVIE").active(true).build())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
        when(funkoRepository.findById(list.get(0).getId())).thenReturn(java.util.Optional.ofNullable(list.get(0)));
        when(funkoMapperImpl.toGetFunkoDTO(list.get(0))).thenReturn(list2.get(0));
        var funko = funkoService.getFunkoById(String.valueOf(list.get(0).getId()));
        assertAll("Funko properties",
                () -> assertEquals(list.get(0).getName(), funko.getName(), "El nombre debe coincidir"),
                () -> assertEquals(list.get(0).getPrice(), funko.getPrice(), "El precio debe coincidir"),
                () -> assertEquals(list.get(0).getQuantity(), funko.getQuantity(), "La cantidad debe coincidir"),
                () -> assertEquals(list.get(0).getImage(), funko.getImage(), "La imagen debe coincidir")
        );
        verify(funkoRepository, times(1)).findById(list.get(0).getId());
    }

    /**
     * Test para comprobar que no se ha encontrado el Funko por su id
     */
    @Test
    void testGetFunkoByIdNotFound() {
        when(funkoRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        String id = UUID.randomUUID().toString();
        assertThrows(FunkoNotFoundException.class, () -> funkoService.getFunkoById(id));
        verify(funkoRepository, times(1)).findById(any(UUID.class));
    }

    /**
     * Test para comprobar que el ID del Funko no es válido cuando se hace un FindById
     */
    @Test
    void testGetFunkoByIdNotValidUUID() {
        String invalidUUID = "UUID NO VÁLIDA";
        assertThrows(FunkoNotValidUUIDException.class, () -> funkoService.getFunkoById(invalidUUID));
    }

    /**
     * Test para comprobar que se inserta un Funko
     *
     * @throws CategoryNotFoundException excepción de categoría no encontrada
     * @throws IOException               excepción de entrada/salida
     */
    @Test
    void testPostFunko() throws CategoryNotFoundException, IOException {
        var insert = CreateFunkoDTO.builder()
                .name("nombre").price(2.2).quantity(2).image("imagen").categoryId(1L).build();
        var category = Category.builder().id(1L).type("MOVIE").active(true).createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now()).build();
        var inserted = new Funko();
        inserted = Funko.builder().name("nombre").price(2.2).quantity(2).image("imagen").category(category).build();
        when(categoryService.getCategoryById(1L)).thenReturn(category);
        when(funkoMapperImpl.toFunko(insert, category)).thenReturn(inserted);
        when(funkoRepository.save(inserted)).thenReturn(inserted);
        when(funkoMapperImpl.toGetFunkoDTO(inserted))
                .thenReturn(GetFunkoDTO.builder().name("nombre").price(2.2).quantity(2).image("imagen")
                        .category(category).build());
        GetFunkoDTO inserted2 = funkoService.postFunko(insert);
        assertNotNull(inserted2);
        assertAll("Funko properties",
                () -> assertEquals(insert.getName(), inserted2.getName(), "El nombre debe coincidir"),
                () -> assertEquals(insert.getPrice(), inserted2.getPrice(), "El precio debe coincidir"),
                () -> assertEquals(insert.getQuantity(), inserted2.getQuantity(), "La cantidad debe coincidir"),
                () -> assertEquals(insert.getImage(), inserted2.getImage(), "La imagen debe coincidir")
        );
        verify(funkoRepository, times(1)).save(any(Funko.class));
    }

    /**
     * Test para probar que el Funko se ha actualizado
     *
     * @throws CategoryNotFoundException  excepción de categoría no encontrada
     * @throws FunkoNotValidUUIDException excepción de Funko no válido
     * @throws FunkoNotFoundException     excepción de Funko no encontrado
     * @throws IOException                excepción de entrada/salida
     */
    @Test
    void testPutFunko() throws CategoryNotFoundException, FunkoNotValidUUIDException, FunkoNotFoundException, IOException {
        var update = UpdateFunkoDTO.builder()
                .name("nombre").price(2.2).quantity(2).image("imagen").categoryId(1L).build();
        var category = Category.builder().id(1L).type("MOVIE").active(true).createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now()).build();
        when(funkoRepository.findById(list.get(0).getId())).thenReturn(Optional.of(list.get(0)));
        when(categoryService.getCategoryById(1L)).thenReturn(category);
        when(funkoMapperImpl.toFunko(list.get(0), update, category)).thenReturn(list.get(0));
        when(funkoRepository.save(list.get(0))).thenReturn(list.get(0));
        when(funkoMapperImpl.toGetFunkoDTO(list.get(0)))
                .thenReturn(GetFunkoDTO.builder().name("nombre").price(2.2).quantity(2).image("imagen")
                        .category(category).build());
        GetFunkoDTO updated2 = funkoService.putFunko(list.get(0).getId().toString(), update);
        assertNotNull(updated2);
        assertAll("Funko properties",
                () -> assertEquals(update.getName(), updated2.getName(), "El nombre debe coincidir"),
                () -> assertEquals(update.getPrice(), updated2.getPrice(), "El precio debe coincidir"),
                () -> assertEquals(update.getQuantity(), updated2.getQuantity(), "La cantidad debe coincidir"),
                () -> assertEquals(update.getImage(), updated2.getImage(), "La imagen debe coincidir")
        );
        verify(funkoRepository, times(1)).save(any(Funko.class));
    }

    /**
     * Test para comprobar que el Funko no se encuentra cuando se hace un Put
     */
    @Test
    void testPutFunkoNotFound() {
        var update = UpdateFunkoDTO.builder()
                .name("nombre").price(2.2).quantity(2).image("imagen").categoryId(1L).build();
        var data = list.get(0).getId().toString();
        when(funkoRepository.findById(list.get(0).getId())).thenReturn(Optional.empty());
        assertThrows(FunkoNotFoundException.class, () -> funkoService.putFunko(data, update));
    }

    /**
     * Test para comprobar el Funko se puede actualizar parcialmente
     *
     * @throws CategoryNotFoundException  excepción de categoría no encontrada
     * @throws FunkoNotValidUUIDException excepción de UUID del Funko no válida
     * @throws FunkoNotFoundException     excepción de Funko no encontrado
     * @throws IOException                excepción de entrada/salida
     */
    @Test
    void testPatchFunko() throws CategoryNotFoundException, FunkoNotValidUUIDException, FunkoNotFoundException, IOException {
        var update = PatchFunkoDTO.builder()
                .name("nombre").price(2.2).quantity(2).image("imagen").categoryId(1L).build();
        var category = Category.builder().id(1L).type("MOVIE").active(true).createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now()).build();
        when(funkoRepository.findById(list.get(0).getId())).thenReturn(Optional.of(list.get(0)));
        when(funkoRepository.save(list.get(0))).thenReturn(list.get(0));
        when(funkoMapperImpl.toGetFunkoDTO(list.get(0)))
                .thenReturn(GetFunkoDTO.builder().name("nombre").price(2.2).quantity(2).image("imagen")
                        .category(category).build());
        GetFunkoDTO updated2 = funkoService.patchFunko(list.get(0).getId().toString(), update);
        assertNotNull(updated2);
        assertAll("Funko properties",
                () -> assertEquals(update.getName(), updated2.getName(), "El nombre debe coincidir"),
                () -> assertEquals(update.getPrice(), updated2.getPrice(), "El precio debe coincidir"),
                () -> assertEquals(update.getQuantity(), updated2.getQuantity(), "La cantidad debe coincidir"),
                () -> assertEquals(update.getImage(), updated2.getImage(), "La imagen debe coincidir")
        );
        verify(funkoRepository, times(1)).save(any(Funko.class));
    }

    /**
     * Test para comprobar que el Funko no se encuentra cuando se hace un Patch
     */
    @Test
    void testPatchFunkoNotFound() {
        var update = PatchFunkoDTO.builder()
                .name("nombre").price(2.2).quantity(2).image("imagen").categoryId(1L).build();
        var data = list.get(0).getId().toString();
        when(funkoRepository.findById(list.get(0).getId())).thenReturn(Optional.empty());
        assertThrows(FunkoNotFoundException.class, () -> funkoService.patchFunko(data, update));
    }

    /**
     * Test para comprobar que el Funko no es válido cuando se hace un Put
     */
    @Test
    void testNotValidUUIDPutFunko() {
        var data = UpdateFunkoDTO.builder()
                .price(14.99)
                .quantity(7)
                .build();
        assertThrows(FunkoNotValidUUIDException.class, () -> funkoService.putFunko("()", data));
    }

    /**
     * Test para comprobar que el ID del Funko no es válido cuando se hace un Patch
     */
    @Test
    void testNotValidUUIDPatchFunko() {
        var data = PatchFunkoDTO.builder()
                .price(14.99)
                .quantity(7)
                .build();
        assertThrows(FunkoNotValidUUIDException.class, () -> funkoService.patchFunko("()", data));
    }

    /**
     * Test para comprobar que se elimina un Funko
     *
     * @throws FunkoNotValidUUIDException excepción cuando el Funko no es válido
     * @throws FunkoNotFoundException     excepción cuando el Funko no se encuentra
     * @throws IOException                excepción de entrada/salida
     */
    @Test
    void testDeleteFunko() throws FunkoNotValidUUIDException, FunkoNotFoundException, IOException {
        when(funkoRepository.findById(list.get(0).getId())).thenReturn(Optional.ofNullable(list.get(0)));
        doNothing().when(funkoRepository).delete(any(Funko.class));
        funkoService.deleteFunko(String.valueOf(list.get(0).getId()));

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Funko> expectedPage = new PageImpl<>(new ArrayList<>());
        Specification<Funko> anySpecification = any();
        when(funkoRepository.findAll(anySpecification, any(Pageable.class))).thenReturn(expectedPage);
        Page<GetFunkoDTO> actualPage = funkoService.getAllFunko(Optional.empty(), Optional.empty(),
                Optional.empty(), pageable);
        var list3 = actualPage.getContent();
        assertNotNull(list3);
        assertEquals(0, actualPage.getContent().size());
        verify(funkoRepository, times(1)).delete(any(Funko.class));
    }

    /**
     * Test para comprobar que el UUID no es válido al intentar eliminar el Funko
     */
    @Test
    void testNotValidUUIDDeleteFunko() {
        assertThrows(FunkoNotValidUUIDException.class, () -> funkoService.deleteFunko("()"));
    }

    /**
     * Test para comprobar que el Funko no se encuentra cuando se intenta eliminar
     */
    @Test
    void testDeleteFunkoNotFound() {
        var data = UUID.randomUUID().toString();
        assertThrows(FunkoNotFoundException.class, () -> funkoService.deleteFunko(data));
    }

    /**
     * Test para comprobar que el UUID no es válido al intentar eliminar el Funko
     */
    @Test
    void testDeleteFunkoNotValidUUID() {
        String invalidUUID = "UUID NO VÁLIDA";
        assertThrows(FunkoNotValidUUIDException.class, () -> funkoService.deleteFunko(invalidUUID));
    }

    /**
     * Test que comprueba que se puede actualizar una imagen
     *
     * @throws CategoryNotFoundException No se ha encontrado la categoría
     * @throws IOException               Problema Entrada/Salida
     */
    @Test
    void testUpdateImageSuccess() throws CategoryNotFoundException, IOException {
        String existingFunkoId = list.get(0).getId().toString();
        String imageUrl = "https://tech.madirex.com/favicon.ico";
        MultipartFile multipartFile = mock(MultipartFile.class);
        GetFunkoDTO expectedFunkoDTO = GetFunkoDTO.builder()
                .id(UUID.randomUUID())
                .name("Test")
                .price(2.2)
                .quantity(2)
                .image(imageUrl)
                .category(Category.builder().id(1L).type("MOVIE").active(true).build())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        expectedFunkoDTO.setImage(imageUrl);

        when(funkoRepository.findById(list.get(0).getId()))
                .thenReturn(Optional.of(list.get(0)));
        when(storageService.store(any(), any(), any()))
                .thenReturn(imageUrl);

        //path
        var update = PatchFunkoDTO.builder().image(imageUrl).build();
        when(funkoRepository.findById(list.get(0).getId())).thenReturn(Optional.of(list.get(0)));
        when(funkoRepository.save(list.get(0))).thenReturn(any());
        when(funkoMapperImpl.toGetFunkoDTO(list.get(0))).thenReturn(expectedFunkoDTO);
        GetFunkoDTO updated2 = funkoService.patchFunko(list.get(0).getId().toString(), update);

        //check
        GetFunkoDTO resultFunkoDTO = funkoService.updateImage(existingFunkoId, multipartFile, false);
        assertAll(
                () -> assertNotNull(updated2),
                () -> assertNotNull(resultFunkoDTO),
                () -> assertEquals(expectedFunkoDTO.getImage(), resultFunkoDTO.getImage())
        );
        verify(funkoRepository, times(3)).findById(list.get(0).getId());
        verify(funkoRepository, times(2)).save(list.get(0));
        verify(storageService, times(1)).store(any(), any(), any());
    }

    /**
     * Test que comprueba que el Funko no se ha encontrado al intentar actualizar la imagen
     */
    @Test
    void testUpdateImageFunkoNotFound() {
        UUID fakeUuid = UUID.randomUUID();
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(funkoRepository.findById(fakeUuid)).thenThrow(new FunkoNotFoundException(fakeUuid.toString()));
        String fakeUuidString = fakeUuid.toString();
        assertThrows(FunkoNotFoundException.class, () -> funkoService.updateImage(fakeUuidString, multipartFile, false));
    }

    /**
     * Test que comprueba que el UUID no es válido al intentar actualizar la imagen
     */
    @Test
    void testUpdateImageFunkoNotValidUUID() {
        MultipartFile multipartFile = mock(MultipartFile.class);
        assertThrows(FunkoNotValidUUIDException.class,
                () -> funkoService.updateImage("()", multipartFile, false));
    }

    /**
     * Test que comprueba el OnChange cuando el WebSocketService es nulo
     *
     * @throws JsonProcessingException Excepción al procesar Json
     */
    @Test
    void testOnChangeWhenWebSocketServiceIsNull() throws JsonProcessingException {
        MockitoAnnotations.openMocks(this);
        GetFunkoDTO getFunkoDTO = new GetFunkoDTO();
        funkoService.setWebSocketService(null);
        funkoService.onChange(Notification.Type.CREATE, getFunkoDTO);
        assertNotNull(getFunkoDTO);
    }

    /**
     * Test que comprueba el OnChange cuando el WebSocketService tiene una excepción de entrada/salida
     *
     * @throws JsonProcessingException Excepción al procesar Json
     */
    @Test
    void testOnChangeWithIOException() throws JsonProcessingException {
        GetFunkoDTO dummyData = new GetFunkoDTO();
        funkoService.onChange(Notification.Type.CREATE, dummyData);
        assertNotNull(dummyData);
    }

}
