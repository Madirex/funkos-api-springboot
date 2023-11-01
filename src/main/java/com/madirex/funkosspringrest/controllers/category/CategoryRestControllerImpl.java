package com.madirex.funkosspringrest.controllers.category;

import com.madirex.funkosspringrest.dto.category.CreateCategoryDTO;
import com.madirex.funkosspringrest.dto.category.PatchCategoryDTO;
import com.madirex.funkosspringrest.dto.category.UpdateCategoryDTO;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotFoundException;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotValidIDException;
import com.madirex.funkosspringrest.exceptions.category.DeleteCategoryException;
import com.madirex.funkosspringrest.models.Category;
import com.madirex.funkosspringrest.services.category.CategoryServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase CategoryRestControllerImpl
 */
@RestController
@RequestMapping("/api/category")
public class CategoryRestControllerImpl implements CategoryRestController {

    private final CategoryServiceImpl service;

    /**
     * Constructor de la clase
     *
     * @param service Servicio de Category
     */
    @Autowired
    public CategoryRestControllerImpl(CategoryServiceImpl service) {
        this.service = service;
    }

    /**
     * Método para obtener todos los Categorys
     *
     * @return ResponseEntity con el código de estado
     */
    @GetMapping()
    @Override
    public ResponseEntity<List<Category>> findAll() {
        return ResponseEntity.ok(service.getAllCategory());
    }

    /**
     * Método para obtener un Category por su ID
     *
     * @param id ID del Category en formato String
     * @return ResponseEntity con el código de estado
     */
    @GetMapping("/{id}")
    @Override
    public ResponseEntity<Category> findById(@Valid @PathVariable Long id) throws CategoryNotValidIDException, CategoryNotFoundException {

        Category category = service.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    /**
     * Método para crear un Category
     *
     * @param funko Objeto CreateCategoryDTO con los campos a crear
     * @return ResponseEntity con el código de estado
     */
    @PostMapping()
    @Override
    public ResponseEntity<Category> post(@Valid @RequestBody CreateCategoryDTO funko) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.postCategory(funko));
    }

    /**
     * Método para actualizar un Category
     *
     * @param id    ID del Category en formato String
     * @param funko Objeto UpdateCategoryDTO con los campos a actualizar
     * @return ResponseEntity con el código de estado
     */
    @PutMapping("/{id}")
    @Override
    public ResponseEntity<Category> put(@Valid @PathVariable Long id, @Valid @RequestBody UpdateCategoryDTO funko) throws CategoryNotValidIDException, CategoryNotFoundException {
        Category updatedCategory = service.putCategory(id, funko);
        return ResponseEntity.ok(updatedCategory);
    }


    /**
     * Método para actualizar parcialmente un Category
     *
     * @param id    ID del Category en formato String
     * @param funko Objeto PatchCategoryDTO con los campos a actualizar
     * @return ResponseEntity con el código de estado
     */
    @PatchMapping("/{id}")
    @Override
    public ResponseEntity<Category> patch(@Valid @PathVariable Long id, @Valid @RequestBody PatchCategoryDTO funko) throws CategoryNotValidIDException, CategoryNotFoundException {
        Category updatedCategory = service.patchCategory(id, funko);
        return ResponseEntity.ok(updatedCategory);
    }


    /**
     * Método para eliminar un Category
     *
     * @param id ID del Category en formato String
     * @return ResponseEntity con el código de estado
     */
    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<String> delete(@Valid @PathVariable Long id) throws CategoryNotFoundException {
        try {
            service.deleteCategory(id);
            return ResponseEntity.noContent().build();
        } catch (DeleteCategoryException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se ha eliminado el Category. Revisa que no existan Funkos asociados a él.");
        }
    }

    /**
     * Método para manejar las excepciones de validación
     *
     * @param ex Excepción
     * @return Mapa con los errores
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}