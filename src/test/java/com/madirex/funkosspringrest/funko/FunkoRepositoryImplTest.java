//package com.madirex.funkosspringrest;
//
//import com.madirex.funkosspringrest.models.Funko;
//import com.madirex.funkosspringrest.repositories.FunkoRepository;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class FunkoRepositoryImplTest {
//
//    private FunkoRepository funkoRepository;
//
//    @BeforeEach
//    void setUp() {
//        funkoRepository = new FunkoRepositoryImpl();
//        var list = funkoRepository.getAllFunko();
//        List<Funko> copyList = new ArrayList<>(list);
//        for (Funko e : copyList) {
//            funkoRepository.deleteFunko(e.getId());
//        }
//    }
//
//    @AfterEach
//    void tearDown() {
//        var list = funkoRepository.getAllFunko();
//        List<Funko> copyList = new ArrayList<>(list);
//        for (Funko e : copyList) {
//            funkoRepository.deleteFunko(e.getId());
//        }
//    }
//
//    @Test
//    void testGetAllFunko() {
//        var list = funkoRepository.getAllFunko();
//        assertNotNull(list, "La lista no debe ser nula");
//        assertEquals(0, list.size(), "La lista debe estar vacía");
//        Funko funko = Funko.builder()
//                .id(UUID.randomUUID())
//                .name("Test")
//                .price(2.2)
//                .quantity(2)
//                .image("http://tech.madirex.com/favicon.ico")
//                .category("Otros")
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .build();
//        var saved = funkoRepository.postFunko(funko);
//        assertTrue(saved.isPresent());
//        assertEquals(1, list.size(), "La lista debe contener 1 elemento");
//        assertAll("Funko properties",
//                () -> assertNotNull(saved.get().getId(), "El ID no debe ser nulo"),
//                () -> assertEquals(funko.getName(), saved.get().getName(), "El nombre debe coincidir"),
//                () -> assertEquals(funko.getPrice(), saved.get().getPrice(), "El precio debe coincidir"),
//                () -> assertEquals(funko.getQuantity(), saved.get().getQuantity(), "La cantidad debe coincidir"),
//                () -> assertEquals(funko.getImage(), saved.get().getImage(), "La imagen debe coincidir"),
//                () -> assertEquals(funko.getCategory(), saved.get().getCategory(), "La categoría debe coincidir"),
//                () -> assertEquals(funko.getCreatedAt(), saved.get().getCreatedAt(), "La fecha de creación debe coincidir"),
//                () -> assertEquals(funko.getUpdatedAt(), saved.get().getUpdatedAt(), "La fecha de actualización debe coincidir"));
//    }
//
//    @Test
//    void testGetFunkoById() {
//        Funko funko = Funko.builder()
//                .id(UUID.randomUUID())
//                .name("Test")
//                .price(2.2)
//                .quantity(2)
//                .image("http://tech.madirex.com/favicon.ico")
//                .category("Otros")
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .build();
//        var saved = funkoRepository.postFunko(funko);
//        assertTrue(saved.isPresent());
//        var find = funkoRepository.getFunkoById(UUID.fromString(funko.getId().toString()));
//        assertTrue(find.isPresent());
//        assertAll("Funko properties",
//                () -> assertNotNull(find.get().getId(), "El ID no debe ser nulo"),
//                () -> assertEquals(funko.getName(), find.get().getName(), "El nombre debe coincidir"),
//                () -> assertEquals(funko.getPrice(), find.get().getPrice(), "El precio debe coincidir"),
//                () -> assertEquals(funko.getQuantity(), find.get().getQuantity(), "La cantidad debe coincidir"),
//                () -> assertEquals(funko.getImage(), find.get().getImage(), "La imagen debe coincidir"),
//                () -> assertEquals(funko.getCategory(), find.get().getCategory(), "La categoría debe coincidir"),
//                () -> assertEquals(funko.getCreatedAt(), find.get().getCreatedAt(), "La fecha de creación debe coincidir"),
//                () -> assertEquals(funko.getUpdatedAt(), find.get().getUpdatedAt(), "La fecha de actualización debe coincidir")
//        );
//    }
//
//    @Test
//    void testPostFunko() {
//        Funko funko = Funko.builder()
//                .id(UUID.randomUUID())
//                .name("Test")
//                .price(2.2)
//                .quantity(2)
//                .image("http://tech.madirex.com/favicon.ico")
//                .category("Otros")
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .build();
//        var saved = funkoRepository.postFunko(funko);
//        assertTrue(saved.isPresent());
//        assertAll("Funko properties",
//                () -> assertNotNull(saved.get().getId(), "El ID no debe ser nulo"),
//                () -> assertEquals(funko.getName(), saved.get().getName(), "El nombre debe coincidir"),
//                () -> assertEquals(funko.getPrice(), saved.get().getPrice(), "El precio debe coincidir"),
//                () -> assertEquals(funko.getQuantity(), saved.get().getQuantity(), "La cantidad debe coincidir"),
//                () -> assertEquals(funko.getImage(), saved.get().getImage(), "La imagen debe coincidir"),
//                () -> assertEquals(funko.getCategory(), saved.get().getCategory(), "La categoría debe coincidir"),
//                () -> assertEquals(funko.getCreatedAt(), saved.get().getCreatedAt(), "La fecha de creación debe coincidir"),
//                () -> assertEquals(funko.getUpdatedAt(), saved.get().getUpdatedAt(), "La fecha de actualización debe coincidir")
//        );
//    }
//
//    @Test
//    void testPutFunko() {
//        LocalDateTime date = LocalDateTime.now();
//        Funko funko = Funko.builder()
//                .id(UUID.randomUUID())
//                .name("Test")
//                .price(2.2)
//                .quantity(2)
//                .image("http://tech.madirex.com/favicon.ico")
//                .category("Otros")
//                .createdAt(date)
//                .updatedAt(LocalDateTime.now())
//                .build();
//        var inserted = funkoRepository.postFunko(funko);
//        var name = "Testing2";
//        var price = 42.42;
//        var quantity = 23;
//        var image = "http://games.madirex.com/favicon.ico";
//        var category = "ANIME";
//        var updatedAt = LocalDateTime.now();
//        Funko modified = Funko.builder()
//                .id(UUID.randomUUID())
//                .name(name)
//                .price(price)
//                .quantity(quantity)
//                .image(image)
//                .category(category)
//                .updatedAt(updatedAt)
//                .build();
//        var updated = funkoRepository.putFunko(UUID.fromString(funko.getId().toString()), modified);
//        assertTrue(updated.isPresent());
//        assertTrue(inserted.isPresent());
//        System.out.println(updated.get().getCategory());
//        assertAll("Funko properties",
//                () -> assertNotNull(updated.get().getId(), "El ID no debe ser nulo"),
//                () -> assertEquals(name, updated.get().getName(), "El nombre debe coincidir"),
//                () -> assertEquals(price, updated.get().getPrice(), "El precio debe coincidir"),
//                () -> assertEquals(quantity, updated.get().getQuantity(), "La cantidad debe coincidir"),
//                () -> assertEquals(image, updated.get().getImage(), "La imagen debe coincidir"),
//                () -> assertEquals(category, updated.get().getCategory(), "La categoría debe coincidir"),
//                () -> assertEquals(date, updated.get().getCreatedAt(), "La fecha de creación debe coincidir")
//        );
//    }
//
//    @Test
//    void testPutFunkoNotFound() {
//        LocalDateTime date = LocalDateTime.now();
//        Funko funko = Funko.builder()
//                .id(UUID.randomUUID())
//                .name("Test")
//                .price(2.2)
//                .quantity(2)
//                .image("http://tech.madirex.com/favicon.ico")
//                .category("Otros")
//                .createdAt(date)
//                .updatedAt(LocalDateTime.now())
//                .build();
//        var updated = funkoRepository.putFunko(UUID.randomUUID(), funko);
//        assertTrue(updated.isEmpty());
//    }
//
//    @Test
//    void testDeleteFunko() {
//        Funko funko = Funko.builder()
//                .id(UUID.randomUUID())
//                .name("Test")
//                .price(2.2)
//                .quantity(2)
//                .image("http://tech.madirex.com/favicon.ico")
//                .category("Otros")
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .build();
//        var saved = funkoRepository.postFunko(funko);
//        var deleted = funkoRepository.deleteFunko(funko.getId());
//        assertAll("Funko properties",
//                () -> assertTrue(saved.isPresent(), "No se ha podido guardar"),
//                () -> assertTrue(deleted.isPresent(), "No se ha podido eliminar"));
//    }
//
//    @Test
//    void testDeleteFunkoNotFound() {
//        var deleted = funkoRepository.deleteFunko(UUID.randomUUID());
//        assertTrue(deleted.isEmpty());
//    }
//
//
//}
