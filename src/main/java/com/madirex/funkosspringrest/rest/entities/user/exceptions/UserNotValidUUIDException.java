package com.madirex.funkosspringrest.rest.entities.user.exceptions;

import com.madirex.funkosspringrest.manager.error.exceptions.ResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class UserNotValidUUIDException
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserNotValidUUIDException extends ResponseException {
    /**
     * Constructor UserNotValidUUIDException
     *
     * @param message Mensaje de error
     */
    public UserNotValidUUIDException(String message) {
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