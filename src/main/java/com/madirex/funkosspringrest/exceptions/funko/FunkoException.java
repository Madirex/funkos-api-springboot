package com.madirex.funkosspringrest.exceptions.funko;

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