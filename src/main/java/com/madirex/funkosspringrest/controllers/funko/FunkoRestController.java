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
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * Interface FunkoRestController
 */
public interface FunkoRestController {

    /**
     * Método para obtener todos los Funkos
     *
     * @param id id del Funko
     * @return ResponseEntity con el código de estado
     * @throws FunkoNotValidUUIDException del Funko
     * @throws FunkoNotFoundException     del Funko
     */
    ResponseEntity<GetFunkoDTO> getFunkoById(@Valid @PathVariable String id) throws FunkoNotValidUUIDException, FunkoNotFoundException;

    /**
     * Método para crear un Funko
     *
     * @param funko Funko a crear
     * @return ResponseEntity con el código de estado
     * @throws CategoryNotFoundException de la categoría
     * @throws CategoryNotValidException de la categoría
     * @throws JsonProcessingException   de la categoría
     */
    ResponseEntity<GetFunkoDTO> postFunko(@Valid @RequestBody CreateFunkoDTO funko) throws CategoryNotFoundException, CategoryNotValidException, JsonProcessingException;

    /**
     * Método para actualizar un Funko
     *
     * @param id    id del Funko
     * @param funko Funko a actualizar
     * @return ResponseEntity con el código de estado
     * @throws FunkoNotValidUUIDException del Funko
     * @throws FunkoNotFoundException     del Funko
     * @throws CategoryNotFoundException  de la categoría
     * @throws CategoryNotValidException  de la categoría
     * @throws JsonProcessingException    excepción Json
     */
    ResponseEntity<GetFunkoDTO> putFunko(@Valid @PathVariable String id, @Valid @RequestBody UpdateFunkoDTO funko) throws FunkoNotValidUUIDException, FunkoNotFoundException, CategoryNotFoundException, CategoryNotValidException, JsonProcessingException;

    /**
     * Método para actualizar parcialmente un Funko
     *
     * @param id    id del Funko
     * @param funko Funko a actualizar
     * @return ResponseEntity con el código de estado
     * @throws FunkoNotValidUUIDException del Funko
     * @throws FunkoNotFoundException     del Funko
     * @throws CategoryNotFoundException  de la categoría
     * @throws CategoryNotValidException  de la categoría
     * @throws JsonProcessingException    excepción Json
     */
    ResponseEntity<GetFunkoDTO> patchFunko(@Valid @PathVariable String id, @Valid @RequestBody PatchFunkoDTO funko) throws FunkoNotValidUUIDException, FunkoNotFoundException, CategoryNotFoundException, CategoryNotValidException, JsonProcessingException;

    /**
     * Método para eliminar un Funko
     *
     * @param id id del Funko
     * @return ResponseEntity con el código de estado
     * @throws FunkoNotValidUUIDException del Funko
     * @throws FunkoNotFoundException     del Funko
     * @throws JsonProcessingException    excepción Json
     */
    ResponseEntity<String> deleteFunko(@Valid @PathVariable String id) throws FunkoNotValidUUIDException, FunkoNotFoundException, JsonProcessingException;

    /**
     * Manejador de excepciones
     *
     * @param ex MethodArgumentNotValidException
     * @return Map<String, String>
     */
    Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex);
}
