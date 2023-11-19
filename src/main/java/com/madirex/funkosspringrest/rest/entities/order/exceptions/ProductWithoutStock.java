package com.madirex.funkosspringrest.rest.entities.order.exceptions;

import com.madirex.funkosspringrest.manager.error.exceptions.ResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción de producto Bad Request
 * Status 400
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductWithoutStock extends ResponseException {

    /**
     * Constructor de la excepción
     *
     * @param id id del producto
     */
    public ProductWithoutStock(String id) {
        super("Cantidad no válida o producto con id " + id + " no tiene stock suficiente");
    }

    /**
     * Método getHttpStatus
     *
     * @return HttpStatus
     */
    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}