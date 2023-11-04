package com.madirex.funkosspringrest.dto.notification;

/**
 * FunkoNotificationResponse
 */
public record FunkoNotificationResponse(
        String id,
        String name,
        Double price,
        Integer quantity,
        String image,
        String category,
        String createdAt,
        String updatedAt
) {
}