package com.madirex.funkosspringrest.rest.entities.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * UserSignUpRequest
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignUpRequest {
    @NotBlank(message = "Name no puede estar vacío")
    private String name;

    @NotBlank(message = "Surname no puede estar vacío")
    private String surname;

    @NotBlank(message = "Username no puede estar vacío")
    private String username;

    @Email(regexp = ".*@.*\\..*", message = "El Email debe de seguir el formato adecuado")
    @NotBlank(message = "Email no puede estar vacío")
    private String email;

    @NotBlank(message = "Password no puede estar vacío")
    @Length(min = 5, message = "Password debe tener al menos 5 caracteres")
    private String password;

    @NotBlank(message = "La repetición de Password no puede estar vacía")
    @Length(min = 5, message = "La repetición de Password debe tener al menos 5 caracteres")
    private String passwordRepeat;

}