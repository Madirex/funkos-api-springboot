package com.madirex.funkosspringrest.rest.entities.user.exceptions;

import com.madirex.funkosspringrest.manager.error.exceptions.ResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class UserDiffers
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UserDiffers extends ResponseException {
    /**
     * Constructor UserDiffers
     *
     * @param message Mensaje de error
     */
    public UserDiffers(String message) {
        super("User de la consulta no coincide con el usuario que está logged: " + message);
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