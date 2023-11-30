package com.madirex.funkosspringrest.rest.entities.funko.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.madirex.funkosspringrest.rest.entities.category.exceptions.CategoryNotFoundException;
import com.madirex.funkosspringrest.rest.entities.funko.dto.CreateFunkoDTO;
import com.madirex.funkosspringrest.rest.entities.funko.dto.GetFunkoDTO;
import com.madirex.funkosspringrest.rest.entities.funko.dto.PatchFunkoDTO;
import com.madirex.funkosspringrest.rest.entities.funko.dto.UpdateFunkoDTO;
import com.madirex.funkosspringrest.rest.entities.funko.exceptions.FunkoNotFoundException;
import com.madirex.funkosspringrest.rest.entities.funko.exceptions.FunkoNotValidUUIDException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

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
     * @throws JsonProcessingException   de la categoría
     */
    ResponseEntity<GetFunkoDTO> postFunko(@Valid @RequestBody CreateFunkoDTO funko) throws CategoryNotFoundException, JsonProcessingException;

    /**
     * Método para actualizar un Funko
     *
     * @param id    id del Funko
     * @param funko Funko a actualizar
     * @return ResponseEntity con el código de estado
     * @throws FunkoNotValidUUIDException del Funko
     * @throws FunkoNotFoundException     del Funko
     * @throws CategoryNotFoundException  de la categoría
     * @throws JsonProcessingException    excepción Json
     */
    ResponseEntity<GetFunkoDTO> putFunko(@Valid @PathVariable String id, @Valid @RequestBody UpdateFunkoDTO funko) throws FunkoNotValidUUIDException, FunkoNotFoundException, CategoryNotFoundException, JsonProcessingException;

    /**
     * Método para actualizar parcialmente un Funko
     *
     * @param id    id del Funko
     * @param funko Funko a actualizar
     * @return ResponseEntity con el código de estado
     * @throws FunkoNotValidUUIDException del Funko
     * @throws FunkoNotFoundException     del Funko
     * @throws CategoryNotFoundException  de la categoría
     * @throws JsonProcessingException    excepción Json
     */
    ResponseEntity<GetFunkoDTO> patchFunko(@Valid @PathVariable String id, @Valid @RequestBody PatchFunkoDTO funko) throws FunkoNotValidUUIDException, FunkoNotFoundException, CategoryNotFoundException, JsonProcessingException;

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

}
