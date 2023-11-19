package com.madirex.funkosspringrest.rest.entities.order.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 * Record Client
 */
public record Client(
        @Length(min = 3, message = "El nombre debe tener al menos 3 caracteres")
        String fullName,

        @Email(message = "El email debe ser válido")
        String email,

        @NotBlank(message = "El teléfono no puede estar vacío")
        String phone,

        @NotNull(message = "La dirección no puede ser nula")
        Address address
) {
}