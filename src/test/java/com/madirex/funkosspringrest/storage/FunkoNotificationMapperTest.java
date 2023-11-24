package com.madirex.funkosspringrest.storage;

import com.madirex.funkosspringrest.rest.entities.funko.dto.GetFunkoDTO;
import com.madirex.funkosspringrest.rest.entities.funko.mappers.FunkoNotificationMapper;
import com.madirex.funkosspringrest.rest.entities.category.models.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Clase FunkoNotificationMapperTest
 */
class FunkoNotificationMapperTest {

    private FunkoNotificationMapper mapper;

    /**
     * Método setUp para inicializar los objetos
     */
    @BeforeEach
    void setUp() {
        mapper = new FunkoNotificationMapper();
    }

    /**
     * Test para comprobar que el mapeo a la NotificationDTO funciona correctamente
     */
    @Test
    void testToFunkoNotificationDto() {
        GetFunkoDTO dto = GetFunkoDTO.builder()
                .id(UUID.randomUUID())
                .name("nombre")
                .price(2.2)
                .quantity(2)
                .image("imagen")
                .category(Category.builder()
                        .id(1L)
                        .type("MOVIE")
                        .active(true)
                        .build())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        var result = mapper.toFunkoNotificationDto(dto);
        assertAll("FunkoNotificationResponse properties",
                () -> assertEquals(dto.getId().toString(), result.id(), "El ID debe coincidir"),
                () -> assertEquals(dto.getName(), result.name(), "El nombre debe coincidir"),
                () -> assertEquals(dto.getPrice(), result.price(), "El precio debe coincidir"),
                () -> assertEquals(dto.getQuantity(), result.quantity(), "La cantidad debe coincidir"),
                () -> assertEquals(dto.getImage(), result.image(), "La imagen debe coincidir"),
                () -> assertEquals(dto.getCategory().toString(), result.category(), "La categoría debe coincidir"),
                () -> assertEquals(dto.getCreatedAt().toString(), result.createdAt(), "La fecha de creación debe coincidir"),
                () -> assertEquals(dto.getUpdatedAt().toString(), result.updatedAt(), "La fecha de actualización debe coincidir")
        );
    }
}
