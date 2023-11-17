package com.madirex.funkosspringrest.rest.funko.mappers;

import com.madirex.funkosspringrest.rest.funko.dto.GetFunkoDTO;
import com.madirex.funkosspringrest.rest.funko.dto.FunkoNotificationResponse;
import org.springframework.stereotype.Component;

/**
 * FunkoNotificationMapper
 */
@Component
public class FunkoNotificationMapper {
    /**
     * Método que convierte un GetFunkoDTO a un FunkoNotificationResponse
     *
     * @param funko GetFunkoDTO
     * @return FunkoNotificationResponse
     */
    public FunkoNotificationResponse toFunkoNotificationDto(GetFunkoDTO funko) {
        return new FunkoNotificationResponse(
                funko.getId().toString(),
                funko.getName(),
                funko.getPrice(),
                funko.getQuantity(),
                funko.getImage(),
                funko.getCategory().toString(),
                funko.getCreatedAt().toString(),
                funko.getUpdatedAt().toString()
        );
    }
}