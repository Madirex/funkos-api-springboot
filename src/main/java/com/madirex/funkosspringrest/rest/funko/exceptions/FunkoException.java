package com.madirex.funkosspringrest.rest.funko.exceptions;

/**
 * Class FunkoException
 */
public abstract class FunkoException extends RuntimeException {
    /**
     * Constructor FunkoException
     *
     * @param message Mensaje de error
     */
    protected FunkoException(String message) {
        super(message);
    }
}