package com.madirex.funkosspringrest.exceptions.storage;

import java.io.Serial;

public abstract class StorageException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 81248974575434657L;

    StorageException(String msg) {
        super(msg);
    }
}