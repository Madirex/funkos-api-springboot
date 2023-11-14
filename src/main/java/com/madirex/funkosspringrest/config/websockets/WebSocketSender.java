package com.madirex.funkosspringrest.config.websockets;

import java.io.IOException;

/**
 * Interfaz para enviar mensajes por WebSockets
 */
public interface WebSocketSender {

    /**
     * Enviar mensaje
     *
     * @param message Mensaje a enviar
     * @throws IOException Error al enviar el mensaje
     */
    void sendMessage(String message) throws IOException;

    /**
     * Enviar mensajes periódicos
     *
     * @throws IOException Error al enviar el mensaje
     */
    void sendPeriodicMessages() throws IOException;
}