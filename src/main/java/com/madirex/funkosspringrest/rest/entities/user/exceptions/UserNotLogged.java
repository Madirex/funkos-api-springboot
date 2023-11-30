package com.madirex.funkosspringrest.rest.entities.user.exceptions;

import com.madirex.funkosspringrest.manager.error.exceptions.ResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class UserNotLogged
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UserNotLogged extends ResponseException {
    /**
     * Constructor UserNotLogged
     *
     * @param message Mensaje de error
     */
    public UserNotLogged(String message) {
        super("User no autenticado: " + message);
    }

    /**
     * Método getHttpStatus
     *
     * @return HttpStatus
     */
    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}