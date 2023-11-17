package com.madirex.funkosspringrest.rest.funko.mappers;

import com.madirex.funkosspringrest.rest.funko.dto.CreateFunkoDTO;
import com.madirex.funkosspringrest.rest.funko.dto.GetFunkoDTO;
import com.madirex.funkosspringrest.rest.funko.dto.UpdateFunkoDTO;
import com.madirex.funkosspringrest.rest.category.models.Category;
import com.madirex.funkosspringrest.rest.funko.models.Funko;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Clase FunkoMapper
 */
@Component
public class FunkoMapperImpl implements FunkoMapper {

    /**
     * Mapea un CreateFunkoDTO en Funko
     *
     * @param dto      CreateFunkoDTO a mapear
     * @param category Category a mapear
     * @return Funko mapeado
     */
    public Funko toFunko(CreateFunkoDTO dto, Category category) {
        return Funko.builder()
                .id(UUID.randomUUID())
                .name(dto.getName())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .image(dto.getImage())
                .category(category)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Mapea un UpdateFunkoDTO en Funko
     *
     * @param funko    Funko a mapear
     * @param dto      UpdateFunkoDTO a mapear
     * @param category Category a mapear
     * @return Funko mapeado
     */
    public Funko toFunko(Funko funko, UpdateFunkoDTO dto, Category category) {
        return Funko.builder()
                .id(funko.getId())
                .name(dto.getName())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .image(dto.getImage())
                .category(category)
                .createdAt(funko.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Mapea un Funko en GetFunkoDTO
     *
     * @param funko Funko a mapear
     * @return GetFunkoDTO mapeado
     */
    public GetFunkoDTO toGetFunkoDTO(Funko funko) {
        return GetFunkoDTO.builder()
                .id(funko.getId())
                .name(funko.getName())
                .price(funko.getPrice())
                .quantity(funko.getQuantity())
                .image(funko.getImage())
                .category(funko.getCategory())
                .createdAt(funko.getCreatedAt())
                .updatedAt(funko.getUpdatedAt())
                .build();
    }

    /**
     * Mapea una lista de Funko en GetFunkoDTO
     *
     * @param dto Lista de Funko a mapear
     * @return Lista de GetFunkoDTO mapeados
     */
    public List<GetFunkoDTO> toFunkoList(List<Funko> dto) {
        return dto.stream()
                .map(this::toGetFunkoDTO)
                .toList();
    }
}
