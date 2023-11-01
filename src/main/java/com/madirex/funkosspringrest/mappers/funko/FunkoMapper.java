package com.madirex.funkosspringrest.mappers.funko;

import com.madirex.funkosspringrest.dto.funko.CreateFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.GetFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.UpdateFunkoDTO;
import com.madirex.funkosspringrest.models.Category;
import com.madirex.funkosspringrest.models.Funko;

import java.util.List;

public interface FunkoMapper {
    Funko toFunko(CreateFunkoDTO dto, Category category);

    Funko toFunko(Funko funko, UpdateFunkoDTO dto, Category category);

    GetFunkoDTO toGetFunkoDTO(Funko funko);

    List<GetFunkoDTO> toFunkoList(List<Funko> dto);
}
