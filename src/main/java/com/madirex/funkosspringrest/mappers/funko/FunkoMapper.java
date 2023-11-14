package com.madirex.funkosspringrest.mappers.funko;

import com.madirex.funkosspringrest.dto.funko.CreateFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.GetFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.UpdateFunkoDTO;
import com.madirex.funkosspringrest.models.Category;
import com.madirex.funkosspringrest.models.Funko;

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
