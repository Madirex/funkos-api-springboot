package com.madirex.funkosspringrest.rest.entities.order.exceptions;

import com.madirex.funkosspringrest.manager.error.exceptions.ResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción de producto no encontrado
 * Status 404
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderNotFound extends ResponseException {
    /**
     * Constructor OrderNotFound
     *
     * @param id Id del pedido
     */
    public OrderNotFound(String id) {
        super("Pedido con id " + id + " no encontrado");
    }

    /**
     * Método getHttpStatus
     *
     * @return HttpStatus
     */
    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}