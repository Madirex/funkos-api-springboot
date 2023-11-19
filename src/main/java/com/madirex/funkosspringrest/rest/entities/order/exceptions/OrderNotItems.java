package com.madirex.funkosspringrest.rest.entities.order.exceptions;

import com.madirex.funkosspringrest.manager.error.exceptions.ResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción de producto Bad Request
 * Status 400
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrderNotItems extends ResponseException {
    /**
     * Constructor de la excepción
     *
     * @param id id del producto
     */
    public OrderNotItems(String id) {
        super("Pedido con id " + id + " no contiene items para ser procesado");
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