package com.madirex.funkosspringrest.rest.funko.models;

/**
 * Notification
 *
 * @param <T> Tipo de dato de la notificación
 */
public record Notification<T>(
        String entity,
        Notification.Type type,
        T data,
        String createdAt
) {
    /**
     * Enum Type
     */
    public enum Type {CREATE, UPDATE, DELETE}
}