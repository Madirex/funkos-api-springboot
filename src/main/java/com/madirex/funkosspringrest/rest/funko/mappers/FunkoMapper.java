package com.madirex.funkosspringrest.rest.funko.mappers;

import com.madirex.funkosspringrest.rest.funko.dto.CreateFunkoDTO;
import com.madirex.funkosspringrest.rest.funko.dto.GetFunkoDTO;
import com.madirex.funkosspringrest.rest.funko.dto.UpdateFunkoDTO;
import com.madirex.funkosspringrest.rest.category.models.Category;
import com.madirex.funkosspringrest.rest.funko.models.Funko;

import java.util.List;

/**
 * Clase FunkoMapper
 */
public interface FunkoMapper {
    /**
     * Mapper de CreateFunkoDTO
     *
     * @param dto      DTO con los datos del funko
     * @param category Categoría del funko
     * @return Funko creado
     */
    Funko toFunko(CreateFunkoDTO dto, Category category);

    /**
     * Mapper de UpdateFunkoDTO dada una categoría existente
     *
     * @param funko    Funko existente
     * @param dto      DTO con los datos del funko
     * @param category Categoría del funko
     * @return Funko actualizado
     */
    Funko toFunko(Funko funko, UpdateFunkoDTO dto, Category category);

    /**
     * Mapper de GetFunkoDTO
     *
     * @param funko Funko a mapear
     * @return Funko mapeado
     */
    GetFunkoDTO toGetFunkoDTO(Funko funko);

    /**
     * Mapper de List<GetFunkoDTO>
     *
     * @param dto List<Funko> a mapear
     * @return List<GetFunkoDTO> mapeado
     */
    List<GetFunkoDTO> toFunkoList(List<Funko> dto);
}
