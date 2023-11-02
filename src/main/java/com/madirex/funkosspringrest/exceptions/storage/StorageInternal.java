package com.madirex.funkosspringrest.exceptions.storage;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class StorageInternal extends StorageException {
    @Serial
    private static final long serialVersionUID = 81248974575434657L;

    public StorageInternal(String msg) {
        super(msg);
    }
}