package com.madirex.funkosspringrest.rest.entities.category.exceptions;

import com.madirex.funkosspringrest.manager.error.exceptions.ResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class CategoryAlreadyExistsException
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CategoryAlreadyExistsException extends ResponseException {
    /**
     * Constructor CategoryAlreadyExistsException
     *
     * @param message Mensaje de error
     */
    public CategoryAlreadyExistsException(String message) {
        super("Categoría ya existente - " + message);
    }

    /**
     * Método getHttpStatus
     *
     * @return HttpStatus
     */
    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}