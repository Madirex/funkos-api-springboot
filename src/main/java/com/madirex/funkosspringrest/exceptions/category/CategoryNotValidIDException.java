package com.madirex.funkosspringrest.exceptions.category;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class CategoryNotValidIDException
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CategoryNotValidIDException extends CategoryException {
    /**
     * Constructor CategoryNotValidIDException
     *
     * @param message Mensaje de error
     */
    public CategoryNotValidIDException(String message) {
        super("ID no válido - " + message);
    }
}