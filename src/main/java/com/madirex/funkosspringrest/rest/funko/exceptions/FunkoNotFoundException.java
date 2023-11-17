package com.madirex.funkosspringrest.rest.funko.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class FunkoNotFoundException
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class FunkoNotFoundException extends FunkoException {
    /**
     * Constructor FunkoNotFoundException
     *
     * @param message Mensaje de error
     */
    public FunkoNotFoundException(String message) {
        super("Funko no encontrado - " + message);
    }
}