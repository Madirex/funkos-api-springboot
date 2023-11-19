package com.madirex.funkosspringrest.rest.pagination.exceptions;

import com.madirex.funkosspringrest.manager.error.exceptions.ResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class PageNotValidException
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class PageNotValidException extends ResponseException {
    /**
     * Constructor PageNotValidException
     *
     * @param message Mensaje de error
     */
    public PageNotValidException(String message) {
        super("Página no válida - " + message);
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