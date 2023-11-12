package com.madirex.funkosspringrest.exceptions.category;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class CategoryNotValidException
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CategoryNotValidException extends CategoryException {
    /**
     * Constructor CategoryNotValidException
     *
     * @param message Mensaje de error
     */
    public CategoryNotValidException(String message) {
        super("Categoría no válida - " + message);
    }
}