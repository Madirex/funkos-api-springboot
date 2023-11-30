package com.madirex.funkosspringrest.manager.error.exceptions;

/**
 * Class ResponseException
 */
public abstract class ResponseException extends RuntimeException implements ResponseExceptionInterface {
    /**
     * Constructor ResponseException
     *
     * @param message Mensaje de error
     */
    protected ResponseException(String message) {
        super(message);
    }
}
