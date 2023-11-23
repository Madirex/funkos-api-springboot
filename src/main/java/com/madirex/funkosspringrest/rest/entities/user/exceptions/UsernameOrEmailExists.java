package com.madirex.funkosspringrest.rest.entities.user.exceptions;

import com.madirex.funkosspringrest.manager.error.exceptions.ResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class UserNameOrEmailExists
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class UsernameOrEmailExists extends ResponseException {
    /**
     * Constructor UserNameOrEmailExists
     *
     * @param message Mensaje de error
     */
    public UsernameOrEmailExists(String message) {
        super("Usuario ya existente - " + message);
    }

    /**
     * Método getHttpStatus
     *
     * @return HttpStatus
     */
    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.CONFLICT;
    }
}