package com.madirex.funkosspringrest.storage.exceptions;

import com.madirex.funkosspringrest.manager.error.exceptions.ResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * Class StorageInternal
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class StorageInternal extends ResponseException {
    @Serial
    private static final long serialVersionUID = 81248974575434657L;

    /**
     * Constructor StorageInternal
     *
     * @param msg Mensaje de error
     */
    public StorageInternal(String msg) {
        super(msg);
    }

    /**
     * Método getHttpStatus
     *
     * @return HttpStatus
     */
    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}