package com.madirex.funkosspringrest.storage.exceptions;

import com.madirex.funkosspringrest.manager.error.exceptions.ResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * Class StorageBadRequest
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StorageBadRequest extends ResponseException {
    @Serial
    private static final long serialVersionUID = 81248974575434657L;

    /**
     * Constructor StorageBadRequest
     *
     * @param msg Mensaje de error
     */
    public StorageBadRequest(String msg) {
        super(msg);
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