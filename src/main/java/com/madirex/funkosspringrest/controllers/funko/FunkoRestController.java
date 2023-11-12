package com.madirex.funkosspringrest.controllers.funko;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.madirex.funkosspringrest.dto.funko.CreateFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.GetFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.PatchFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.UpdateFunkoDTO;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotFoundException;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotValidException;
import com.madirex.funkosspringrest.exceptions.funko.FunkoNotFoundException;
import com.madirex.funkosspringrest.exceptions.funko.FunkoNotValidUUIDException;
import com.madirex.funkosspringrest.utils.pagination.PageResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Optional;

/**
 * Interface FunkoRestController
 */
public interface FunkoRestController {

    @GetMapping()
    ResponseEntity<PageResponse<GetFunkoDTO>> getAllFunko(
            @Valid @RequestParam(required = false) Optional<String> category,
            @RequestParam(required = false) Optional<Double> maxPrice,
            @RequestParam(required = false) Optional<Integer> maxQuantity,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    );

    ResponseEntity<GetFunkoDTO> getFunkoById(@Valid @PathVariable String id) throws FunkoNotValidUUIDException, FunkoNotFoundException;

    ResponseEntity<GetFunkoDTO> postFunko(@Valid @RequestBody CreateFunkoDTO funko) throws CategoryNotFoundException, CategoryNotValidException, JsonProcessingException;

    ResponseEntity<GetFunkoDTO> putFunko(@Valid @PathVariable String id, @Valid @RequestBody UpdateFunkoDTO funko) throws FunkoNotValidUUIDException, FunkoNotFoundException, CategoryNotFoundException, CategoryNotValidException, JsonProcessingException;

    ResponseEntity<GetFunkoDTO> patchFunko(@Valid @PathVariable String id, @Valid @RequestBody PatchFunkoDTO funko) throws FunkoNotValidUUIDException, FunkoNotFoundException, CategoryNotFoundException, CategoryNotValidException, JsonProcessingException;

    ResponseEntity<String> deleteFunko(@Valid @PathVariable String id) throws FunkoNotValidUUIDException, FunkoNotFoundException, JsonProcessingException;

    Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex);
}
