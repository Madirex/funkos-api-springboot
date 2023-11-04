package com.madirex.funkosspringrest.models;

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
    public enum Type {CREATE, UPDATE, DELETE}
}