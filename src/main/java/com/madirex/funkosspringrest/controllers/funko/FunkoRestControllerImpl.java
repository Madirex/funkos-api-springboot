package com.madirex.funkosspringrest.controllers.funko;

import com.madirex.funkosspringrest.dto.funko.CreateFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.GetFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.PatchFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.UpdateFunkoDTO;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotFoundException;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotValidIDException;
import com.madirex.funkosspringrest.exceptions.funko.FunkoNotFoundException;
import com.madirex.funkosspringrest.exceptions.funko.FunkoNotValidUUIDException;
import com.madirex.funkosspringrest.services.funko.FunkoServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase FunkoRestControllerImpl
 */
@RestController
@RequestMapping("/api/funkos")
public class FunkoRestControllerImpl implements FunkoRestController {

    private final FunkoServiceImpl service;

    /**
     * Constructor de la clase
     *
     * @param service Servicio de Funko
     */
    @Autowired
    public FunkoRestControllerImpl(FunkoServiceImpl service) {
        this.service = service;
    }

    /**
     * Método para obtener todos los Funkos
     *
     * @param category Categoría por la que filtrar
     * @return ResponseEntity con el código de estado
     */
    @GetMapping()
    @Override
    public ResponseEntity<List<GetFunkoDTO>> getAllFunko(@Valid @RequestParam(required = false) String category) {
        if (category != null && !category.isEmpty()) {
            return ResponseEntity.ok(service.getAllFunkoFilterByCategory(service.getAllFunko(), category));
        } else {
            return ResponseEntity.ok(service.getAllFunko());
        }
    }

    /**
     * Método para obtener un Funko por su UUID
     *
     * @param id UUID del Funko en formato String
     * @return ResponseEntity con el código de estado
     */
    @GetMapping("/{id}")
    @Override
    public ResponseEntity<GetFunkoDTO> getFunkoById(@Valid @PathVariable String id) throws FunkoNotFoundException {
        try {
            return ResponseEntity.ok(service.getFunkoById(id));
        } catch (FunkoNotValidUUIDException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El UUID del Funko no es válido: " + e.getMessage());
        }
    }

    /**
     * Método para crear un Funko
     *
     * @param funko Objeto CreateFunkoDTO con los campos a crear
     * @return ResponseEntity con el código de estado
     */
    @PostMapping
    @Override
    public ResponseEntity<GetFunkoDTO> postFunko(@Valid @RequestBody CreateFunkoDTO funko) {
        try {
            GetFunkoDTO funkoDTO = service.postFunko(funko);
            return ResponseEntity.status(HttpStatus.CREATED).body(funkoDTO);
        } catch (CategoryNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La categoría no se encuentra: " + e.getMessage());
        } catch (CategoryNotValidIDException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID de categoría no válido: " + e.getMessage());
        }
    }

    /**
     * Método para actualizar un Funko
     *
     * @param id    UUID del Funko en formato String
     * @param funko Objeto UpdateFunkoDTO con los campos a actualizar
     * @return ResponseEntity con el código de estado
     * @throws FunkoNotFoundException Si no se ha encontrado el Funko con el UUID indicado
     */
    @PutMapping("/{id}")
    @Override
    public ResponseEntity<GetFunkoDTO> putFunko(@Valid @PathVariable String id, @Valid @RequestBody UpdateFunkoDTO funko) throws FunkoNotFoundException {
        try {
            return ResponseEntity.ok(service.putFunko(id, funko));
        } catch (FunkoNotValidUUIDException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El UUID del Funko no es válido: " + e.getMessage());
        } catch (CategoryNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La categoría no se encuentra: " + e.getMessage());
        } catch (CategoryNotValidIDException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID de categoría no válido: " + e.getMessage());
        }
    }

    /**
     * Método para actualizar parcialmente un Funko
     *
     * @param id    UUID del Funko en formato String
     * @param funko Objeto PatchFunkoDTO con los campos a actualizar
     * @return ResponseEntity con el código de estado
     */
    @PatchMapping("/{id}")
    @Override
    public ResponseEntity<GetFunkoDTO> patchFunko(@Valid @PathVariable String id, @Valid @RequestBody PatchFunkoDTO funko) throws FunkoNotFoundException {
        try {
            return ResponseEntity.ok(service.patchFunko(id, funko));
        } catch (FunkoNotValidUUIDException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El UUID del Funko no es válido: " + e.getMessage());
        } catch (CategoryNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La categoría no se encuentra: " + e.getMessage());
        } catch (CategoryNotValidIDException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID de categoría no válido: " + e.getMessage());
        }
    }

    /**
     * Método para eliminar un Funko
     *
     * @param id UUID del Funko en formato String
     * @return ResponseEntity con el código de estado
     */
    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<String> deleteFunko(@Valid @PathVariable String id) throws FunkoNotFoundException {
        try {
            service.deleteFunko(id);
        } catch (FunkoNotValidUUIDException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El UUID del Funko no es válido: " + e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * Método para manejar las excepciones de validación
     *
     * @param ex Excepción
     * @return Mapa con los errores
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @Override
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    /**
     * Método para manejar las excepciones de ResponseStatusException
     *
     * @param ex Excepción
     * @return Error
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        HttpStatusCode httpStatus = ex.getStatusCode();
        String mensaje = ex.getReason();
        return new ResponseEntity<>(mensaje, httpStatus);
    }
}