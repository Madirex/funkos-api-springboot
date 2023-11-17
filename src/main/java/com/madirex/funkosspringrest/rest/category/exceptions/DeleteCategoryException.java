package com.madirex.funkosspringrest.rest.category.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class DeleteCategoryException
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class DeleteCategoryException extends CategoryException {
    /**
     * Constructor DeleteCategoryException
     *
     * @param message Mensaje de error
     */
    public DeleteCategoryException(String message) {
        super("Error al eliminar la categoría - " + message);
    }
}