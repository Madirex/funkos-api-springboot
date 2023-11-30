package com.madirex.funkosspringrest.rest.entities.category.exceptions;

import com.madirex.funkosspringrest.manager.error.exceptions.ResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class CategoryNotFoundException
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoryNotFoundException extends ResponseException {
    /**
     * Constructor CategoryNotFoundException
     *
     * @param message Mensaje de error
     */
    public CategoryNotFoundException(String message) {
        super("Categoría no encontrada - " + message);
    }

    /**
     * Método getHttpStatus
     *
     * @return HttpStatus
     */
    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}