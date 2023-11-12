package com.madirex.funkosspringrest.services.funko;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.madirex.funkosspringrest.dto.funko.CreateFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.GetFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.PatchFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.UpdateFunkoDTO;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotFoundException;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotValidException;
import com.madirex.funkosspringrest.exceptions.funko.FunkoNotFoundException;
import com.madirex.funkosspringrest.exceptions.funko.FunkoNotValidUUIDException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

/**
 * Interface FunkoService
 */
public interface FunkoService {
    Page<GetFunkoDTO> getAllFunko(Optional<String> category, Optional<Double> maxPrice,
                                  Optional<Integer> maxQuantity, PageRequest pageable);

    GetFunkoDTO getFunkoById(String id) throws FunkoNotValidUUIDException, FunkoNotFoundException;

    GetFunkoDTO postFunko(CreateFunkoDTO funko) throws CategoryNotFoundException, CategoryNotValidException, JsonProcessingException;

    GetFunkoDTO putFunko(String id, UpdateFunkoDTO funko) throws FunkoNotValidUUIDException, FunkoNotFoundException, CategoryNotFoundException, CategoryNotValidException, JsonProcessingException;

    GetFunkoDTO patchFunko(String id, PatchFunkoDTO funko) throws FunkoNotValidUUIDException, FunkoNotFoundException, CategoryNotFoundException, CategoryNotValidException, JsonProcessingException;

    void deleteFunko(String id) throws FunkoNotFoundException, FunkoNotValidUUIDException, JsonProcessingException;

    GetFunkoDTO updateImage(String id, MultipartFile image, Boolean withUrl) throws FunkoNotFoundException, FunkoNotValidUUIDException, CategoryNotFoundException, CategoryNotValidException, IOException;
}
