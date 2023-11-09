package com.madirex.funkosspringrest.controllers.funko;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.madirex.funkosspringrest.dto.funko.CreateFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.GetFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.PatchFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.UpdateFunkoDTO;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotFoundException;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotValidIDException;
import com.madirex.funkosspringrest.exceptions.funko.FunkoNotFoundException;
import com.madirex.funkosspringrest.exceptions.funko.FunkoNotValidUUIDException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Interface FunkoRestController
 */
public interface FunkoRestController {
    ResponseEntity<List<GetFunkoDTO>> getAllFunko(@Valid @RequestParam(required = false) String category);

    ResponseEntity<GetFunkoDTO> getFunkoById(@Valid @PathVariable String id) throws FunkoNotValidUUIDException, FunkoNotFoundException;

    ResponseEntity<GetFunkoDTO> postFunko(@Valid @RequestBody CreateFunkoDTO funko) throws CategoryNotFoundException, CategoryNotValidIDException, JsonProcessingException;

    ResponseEntity<GetFunkoDTO> putFunko(@Valid @PathVariable String id, @Valid @RequestBody UpdateFunkoDTO funko) throws FunkoNotValidUUIDException, FunkoNotFoundException, CategoryNotFoundException, CategoryNotValidIDException, JsonProcessingException;

    ResponseEntity<GetFunkoDTO> patchFunko(@Valid @PathVariable String id, @Valid @RequestBody PatchFunkoDTO funko) throws FunkoNotValidUUIDException, FunkoNotFoundException, CategoryNotFoundException, CategoryNotValidIDException, JsonProcessingException;

    ResponseEntity<String> deleteFunko(@Valid @PathVariable String id) throws FunkoNotValidUUIDException, FunkoNotFoundException, JsonProcessingException;

    Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex);
}
