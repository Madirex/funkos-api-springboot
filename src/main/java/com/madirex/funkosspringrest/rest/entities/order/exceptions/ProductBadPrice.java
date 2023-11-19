package com.madirex.funkosspringrest.rest.entities.order.exceptions;

import com.madirex.funkosspringrest.manager.error.exceptions.ResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción de producto Bad Request
 * Status 400
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductBadPrice extends ResponseException {
    /**
     * Constructor de la excepción
     *
     * @param id id del producto
     */
    public ProductBadPrice(String id) {
        super("Producto con id " + id + " no tiene un precio válido o no coincide con su precio actual");
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