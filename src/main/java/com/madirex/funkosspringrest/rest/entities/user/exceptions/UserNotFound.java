package com.madirex.funkosspringrest.rest.entities.user.exceptions;

import com.madirex.funkosspringrest.manager.error.exceptions.ResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class UserNotFound
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFound extends ResponseException {
    /**
     * Constructor UserNotFound
     *
     * @param message Mensaje de error
     */
    public UserNotFound(String message) {
        super("User no encontrado: " + message);
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