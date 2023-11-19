package com.madirex.funkosspringrest.rest.entities.funko.dto;

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