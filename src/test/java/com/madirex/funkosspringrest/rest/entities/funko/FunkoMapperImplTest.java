package com.madirex.funkosspringrest.rest.entities.funko;

import com.madirex.funkosspringrest.rest.entities.funko.dto.CreateFunkoDTO;
import com.madirex.funkosspringrest.rest.entities.funko.dto.UpdateFunkoDTO;
import com.madirex.funkosspringrest.rest.entities.funko.mappers.FunkoMapperImpl;
import com.madirex.funkosspringrest.rest.entities.category.models.Category;
import com.madirex.funkosspringrest.rest.entities.funko.models.Funko;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase FunkoMapperImplTest
 */
class FunkoMapperImplTest {
    private FunkoMapperImpl funkoMapperImpl;

    /**
     * Método setUp para inicializar los objetos
     */
    @BeforeEach
    void setUp() {
        funkoMapperImpl = new FunkoMapperImpl();
    }

    /**
     * Test para comprobar que el mapeo de CreateFunko a Funko es correcto
     */
    @Test
    void testCreateFunkoDTOToFunko() {
        var funko = CreateFunkoDTO.builder()
                .name("nombre")
                .price(2.2)
                .quantity(2)
                .image("imagen")
                .categoryId(1L)
                .build();
        var mapped = funkoMapperImpl.toFunko(funko, Category.builder().id(1L).type("MOVIE").active(true).build());
        assertAll("Funko properties",
                () -> assertNotNull(mapped.getId(), "El ID no debe ser nulo"),
                () -> assertEquals(funko.getName(), mapped.getName(), "El nombre debe coincidir"),
                () -> assertEquals(funko.getPrice(), mapped.getPrice(), "El precio debe coincidir"),
                () -> assertEquals(funko.getQuantity(), mapped.getQuantity(), "La cantidad debe coincidir"),
                () -> assertEquals(funko.getImage(), mapped.getImage(), "La imagen debe coincidir"),
                () -> assertEquals(funko.getCategoryId(), mapped.getCategory().getId(), "La categoría debe coincidir")
        );
    }

    /**
     * Test para comprobar que el mapeo de un UpdateFunko a DTO es correcto
     */
    @Test
    void testUpdateFunkoDTOToFunko() {
        var funkoToEdit = Funko.builder()
                .id(UUID.randomUUID())
                .name("nombre")
                .price(2.2)
                .quantity(2)
                .image("imagen")
                .category(Category.builder().id(1L).type("MOVIE").active(true).build())
                .build();
        var funko = UpdateFunkoDTO.builder()
                .name("nombre")
                .price(2.2)
                .quantity(2)
                .image("imagen")
                .categoryId(1L)
                .build();
        var mapped = funkoMapperImpl.toFunko(funkoToEdit, funko, Category.builder().id(1L).type("MOVIE").active(true).build());
        assertAll("Funko properties",
                () -> assertNotNull(mapped.getId(), "El ID no debe ser nulo"),
                () -> assertEquals(funko.getName(), mapped.getName(), "El nombre debe coincidir"),
                () -> assertEquals(funko.getPrice(), mapped.getPrice(), "El precio debe coincidir"),
                () -> assertEquals(funko.getQuantity(), mapped.getQuantity(), "La cantidad debe coincidir"),
                () -> assertEquals(funko.getImage(), mapped.getImage(), "La imagen debe coincidir"),
                () -> assertEquals(funko.getCategoryId(), mapped.getCategory().getId(), "La categoría debe coincidir")
        );
    }

    /**
     * Test para comprobar que el mapeo de un Funko a DTO es correcto
     */
    @Test
    void toGetFunkoDTO() {
        var funko = Funko.builder()
                .id(UUID.randomUUID())
                .name("nombre")
                .price(2.2)
                .quantity(2)
                .image("imagen")
                .category(Category.builder().id(1L).type("MOVIE").active(true).build())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        var mapped = funkoMapperImpl.toGetFunkoDTO(funko);
        assertAll("Funko properties",
                () -> assertNotNull(mapped.getId(), "El ID no debe ser nulo"),
                () -> assertEquals(funko.getName(), mapped.getName(), "El nombre debe coincidir"),
                () -> assertEquals(funko.getPrice(), mapped.getPrice(), "El precio debe coincidir"),
                () -> assertEquals(funko.getQuantity(), mapped.getQuantity(), "La cantidad debe coincidir"),
                () -> assertEquals(funko.getImage(), mapped.getImage(), "La imagen debe coincidir"),
                () -> assertEquals(funko.getCategory(), mapped.getCategory(), "La categoría debe coincidir"),
                () -> assertEquals(funko.getCreatedAt(), mapped.getCreatedAt(), "La fecha de creación debe coincidir"),
                () -> assertEquals(funko.getUpdatedAt(), mapped.getUpdatedAt(), "La fecha de actualización debe coincidir"));
    }

    /**
     * Test para comprobar mappeo a GetFunkoDTOList
     */
    @Test
    void toGetFunkoDTOList() {
        List<Funko> list = new ArrayList<>();
        list.add(Funko.builder()
                .id(UUID.randomUUID())
                .name("nombre")
                .price(2.2)
                .quantity(2)
                .image("imagen")
                .category(Category.builder().id(1L).type("MOVIE").active(true).build())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
        var mapped = funkoMapperImpl.toFunkoList(list);
        assertAll("Funko properties",
                () -> assertNotNull(mapped.get(0).getId(), "El ID no debe ser nulo"),
                () -> assertEquals(1, mapped.size(), "La lista debe contener 1 elemento"),
                () -> assertEquals(list.get(0).getName(), mapped.get(0).getName(), "El nombre debe coincidir"),
                () -> assertEquals(list.get(0).getPrice(), mapped.get(0).getPrice(), "El precio debe coincidir"),
                () -> assertEquals(list.get(0).getQuantity(), mapped.get(0).getQuantity(), "La cantidad debe coincidir"),
                () -> assertEquals(list.get(0).getImage(), mapped.get(0).getImage(), "La imagen debe coincidir"),
                () -> assertEquals(list.get(0).getCategory(), mapped.get(0).getCategory(), "La categoría debe coincidir"),
                () -> assertEquals(list.get(0).getCreatedAt(), mapped.get(0).getCreatedAt(), "La fecha de creación debe coincidir"),
                () -> assertEquals(list.get(0).getUpdatedAt(), mapped.get(0).getUpdatedAt(), "La fecha de actualización debe coincidir"));
    }

}
