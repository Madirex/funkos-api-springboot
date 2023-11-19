package com.madirex.funkosspringrest.rest.entities.funko.exceptions;

import com.madirex.funkosspringrest.manager.error.exceptions.ResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class FunkoNotValidUUIDException
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FunkoNotValidUUIDException extends ResponseException {
    /**
     * Constructor FunkoNotValidUUIDException
     *
     * @param message Mensaje de error
     */
    public FunkoNotValidUUIDException(String message) {
        super("UUID no válido - " + message);
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