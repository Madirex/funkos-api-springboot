package com.madirex.funkosspringrest.rest.entities.category.exceptions;

import com.madirex.funkosspringrest.manager.error.exceptions.ResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class DeleteCategoryException
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class DeleteCategoryException extends ResponseException {
    /**
     * Constructor DeleteCategoryException
     *
     * @param message Mensaje de error
     */
    public DeleteCategoryException(String message) {
        super("Error al eliminar la categoría - " + message);
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