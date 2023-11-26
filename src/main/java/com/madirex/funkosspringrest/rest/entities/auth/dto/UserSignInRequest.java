package com.madirex.funkosspringrest.rest.entities.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * UserSignInRequest
 */
@Data
@NoArgsConstructor
public class UserSignInRequest {
    @NotBlank(message = "Username no puede estar vacío")
    private String username;

    @NotBlank(message = "Password no puede estar vacío")
    private String password;
}