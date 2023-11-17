package com.madirex.funkosspringrest.rest.funko.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class FunkoNotValidUUIDException
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FunkoNotValidUUIDException extends FunkoException {
    /**
     * Constructor FunkoNotValidUUIDException
     *
     * @param message Mensaje de error
     */
    public FunkoNotValidUUIDException(String message) {
        super("UUID no válido - " + message);
    }
}