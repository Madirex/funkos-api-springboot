package com.madirex.funkosspringrest.storage;


import com.madirex.funkosspringrest.exceptions.storage.StorageBadRequest;
import com.madirex.funkosspringrest.exceptions.storage.StorageInternal;
import com.madirex.funkosspringrest.exceptions.storage.StorageNotFound;
import com.madirex.funkosspringrest.services.storage.FileSystemStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FileSystemStorageServiceTest {

    private final FileSystemStorageService fileSystemStorageService = new FileSystemStorageService("funkos-images");

    @BeforeEach
    public void setUp() throws IOException {
        fileSystemStorageService.deleteAll();
        fileSystemStorageService.init();
        fileSystemStorageService.store(new MockMultipartFile("funko", "funko.jpg", "image/jpeg", "funko".getBytes()));
        fileSystemStorageService.store(new MockMultipartFile("funko", "funko2.jpg", "image/jpeg", "funko".getBytes()));
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletRequestAttributes attrs = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attrs);
    }

    @Test
    void testStore() throws IOException {
        String file = fileSystemStorageService.store(new MockMultipartFile("funko", "funko.jpg", "image/jpeg", "funko".getBytes()));

        assertAll(
                () -> assertNotNull(file),
                () -> assertTrue(file.contains("funko.jpg"))
        );
    }

    @Test
    void testStoreWithEmptyFile() {
        var res = assertThrows(StorageBadRequest.class, () -> fileSystemStorageService.store(new MockMultipartFile("funko", "funko.jpg", "image/jpeg", "".getBytes())));
        assertEquals("Fichero vacío funko.jpg", res.getMessage());
    }


    @Test
    void testStoreWith2puntos() {
        var res = assertThrows(StorageBadRequest.class, () -> fileSystemStorageService.store(new MockMultipartFile("funko", "../funko.jpg", "image/jpeg", "funko".getBytes())));
        assertEquals("No se puede almacenar un fichero con una ruta relativa fuera del directorio actual ../funko.jpg", res.getMessage());
    }

    @Test
    void testLoadAll() {
        var res = fileSystemStorageService.loadAll();
        assertAll(
                () -> assertNotNull(res),
                () -> assertFalse(res.toList().isEmpty())
        );
    }

    @Test
    void testLoad() {
        var res = fileSystemStorageService.load("funko.jpg");
        assertAll(
                () -> assertNotNull(res),
                () -> assertTrue(res.getFileName().toString().contains("funko.jpg"))
        );
    }

    @Test
    void testLoadAsResource() throws IOException {
        var file = fileSystemStorageService.store(new MockMultipartFile("funko", "funko.jpg", "image/jpeg", "funko".getBytes()));
        var res = fileSystemStorageService.loadAsResource(file);
        assertAll(
                () -> assertNotNull(res),
                () -> assertTrue(Objects.requireNonNull(res.getFilename()).contains("funko.jpg"))
        );
    }

    @Test
    void testLoadAsResoureNotFound() {
        var res = assertThrows(StorageNotFound.class, () -> fileSystemStorageService.loadAsResource("funko2.jpg"));
        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("No se puede leer el fichero: funko2.jpg", res.getMessage())
        );
    }

    @Test
    void testLoadAsResoureMalformedUrl() {
        var res = assertThrows(StorageNotFound.class, () -> fileSystemStorageService.loadAsResource("funko2.jpg"));

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("No se puede leer el fichero: funko2.jpg", res.getMessage())
        );
    }

    @Test
    void testDeleteAll() throws IOException {
        fileSystemStorageService.deleteAll();
        fileSystemStorageService.init();
        var res = fileSystemStorageService.loadAll();

        assertAll(
                () -> assertTrue(res.toList().isEmpty())
        );
    }

    @Test
    void testDeleteAllInternalError() {
        fileSystemStorageService.deleteAll();
        var res = assertThrows(StorageInternal.class, fileSystemStorageService::loadAll);
        assertAll(
                () -> assertNotNull(res),
                () -> assertTrue(res.getMessage().contains("Fallo al leer ficheros almacenados "))
        );
    }

    @Test
    void testInit() throws IOException {
        fileSystemStorageService.deleteAll();
        fileSystemStorageService.init();
        var res = fileSystemStorageService.loadAll();
        assertAll(
                () -> assertNotNull(res),
                () -> assertTrue(res.toList().isEmpty())
        );
    }

    @Test
    void testDelete() throws IOException {
        var file = fileSystemStorageService.store(new MockMultipartFile("funko", "funko.jpg", "image/jpeg", "funko".getBytes()));
        fileSystemStorageService.delete(file);
        var res = fileSystemStorageService.loadAll().toList();
        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals(2, res.size())
        );
    }

    @Test
    void testGetUrl() {
        String filename = "testFile.txt";
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String expectedUrl = baseUrl + "/storage/" + filename;
        String actualUrl = fileSystemStorageService.getUrl(filename);
        assertEquals(expectedUrl, actualUrl);
    }


}
