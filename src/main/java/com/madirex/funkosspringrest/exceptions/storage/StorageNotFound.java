package com.madirex.funkosspringrest.exceptions.storage;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StorageNotFound extends StorageException {
    @Serial
    private static final long serialVersionUID = 81248974575434657L;

    public StorageNotFound(String msg) {
        super(msg);
    }
}