package com.madirex.funkosspringrest.exceptions.storage;

import java.io.Serial;

/**
 * Class StorageException
 */
public abstract class StorageException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 81248974575434657L;

    /**
     * Constructor StorageException
     *
     * @param msg Mensaje de error
     */
    StorageException(String msg) {
        super(msg);
    }
}