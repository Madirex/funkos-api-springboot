package com.madirex.funkosspringrest.rest.category.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class CategoryNotFoundException
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoryNotFoundException extends CategoryException {
    /**
     * Constructor CategoryNotFoundException
     *
     * @param message Mensaje de error
     */
    public CategoryNotFoundException(String message) {
        super("Categoría no encontrada - " + message);
    }
}