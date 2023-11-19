package com.madirex.funkosspringrest.rest.entities.order.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

/**
 * Record Address
 */
@Builder
public record Address(
        @Length(min = 3, message = "La calle debe tener al menos 3 caracteres")
        String street,

        @NotBlank(message = "El número no puede estar vacío")
        String number,

        @Length(min = 3, message = "La ciudad debe tener al menos 3 caracteres")
        String city,

        @Length(min = 3, message = "La provincia debe tener al menos 3 caracteres")
        String province,

        @Length(min = 3, message = "El país debe tener al menos 3 caracteres")
        String country,

        @NotBlank(message = "El código postal no puede estar vacío")
        @Pattern(regexp = "^[0-9]{5}$", message = "El código postal debe tener 5 dígitos")
        String cp
) {
}