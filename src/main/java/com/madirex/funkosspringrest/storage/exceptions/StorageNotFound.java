package com.madirex.funkosspringrest.storage.exceptions;

import com.madirex.funkosspringrest.manager.error.exceptions.ResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * Class StorageNotFound
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class StorageNotFound extends ResponseException {
    @Serial
    private static final long serialVersionUID = 81248974575434657L;

    /**
     * Constructor StorageNotFound
     *
     * @param msg Mensaje de error
     */
    public StorageNotFound(String msg) {
        super(msg);
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