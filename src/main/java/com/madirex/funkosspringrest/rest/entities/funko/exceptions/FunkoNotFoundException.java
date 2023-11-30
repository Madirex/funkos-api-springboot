package com.madirex.funkosspringrest.rest.entities.funko.exceptions;

import com.madirex.funkosspringrest.manager.error.exceptions.ResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class FunkoNotFoundException
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class FunkoNotFoundException extends ResponseException {
    /**
     * Constructor FunkoNotFoundException
     *
     * @param message Mensaje de error
     */
    public FunkoNotFoundException(String message) {
        super("Funko no encontrado: " + message);
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