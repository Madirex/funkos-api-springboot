package com.madirex.funkosspringrest.services.funko;

import com.madirex.funkosspringrest.dto.funko.CreateFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.GetFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.PatchFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.UpdateFunkoDTO;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotFoundException;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotValidIDException;
import com.madirex.funkosspringrest.exceptions.funko.FunkoNotFoundException;
import com.madirex.funkosspringrest.exceptions.funko.FunkoNotValidUUIDException;

import java.util.List;

/**
 * Interface FunkoService
 */
public interface FunkoService {
    List<GetFunkoDTO> getAllFunko();

    GetFunkoDTO getFunkoById(String id) throws FunkoNotValidUUIDException, FunkoNotFoundException;

    GetFunkoDTO postFunko(CreateFunkoDTO funko) throws CategoryNotFoundException, CategoryNotValidIDException;

    GetFunkoDTO putFunko(String id, UpdateFunkoDTO funko) throws FunkoNotValidUUIDException, FunkoNotFoundException, CategoryNotFoundException, CategoryNotValidIDException;

    GetFunkoDTO patchFunko(String id, PatchFunkoDTO funko) throws FunkoNotValidUUIDException, FunkoNotFoundException, CategoryNotFoundException, CategoryNotValidIDException;

    void deleteFunko(String id) throws FunkoNotFoundException, FunkoNotValidUUIDException;
}
