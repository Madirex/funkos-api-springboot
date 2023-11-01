package com.madirex.funkosspringrest.exceptions.category;

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