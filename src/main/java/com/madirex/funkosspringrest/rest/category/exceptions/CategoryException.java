package com.madirex.funkosspringrest.rest.category.exceptions;

/**
 * Class CategoryException
 */
public abstract class CategoryException extends Exception {
    /**
     * Constructor CategoryException
     *
     * @param message Mensaje de error
     */
    protected CategoryException(String message) {
        super(message);
    }
}