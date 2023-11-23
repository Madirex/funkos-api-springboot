package com.madirex.funkosspringrest.rest.entities.auth.exceptions;

import com.madirex.funkosspringrest.manager.error.exceptions.ResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class AuthException
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthSignInInvalid extends ResponseException {
    /**
     * Constructor AuthException
     *
     * @param message Mensaje de error
     */
    public AuthSignInInvalid(String message) {
        super(message);
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