package com.madirex.funkosspringrest.rest.entities.user.dto;

import com.madirex.funkosspringrest.rest.entities.user.models.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

/**
 * Clase UserRequest
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdate {
    @NotBlank(message = "Name no puede estar vacío")
    private String name;

    @NotBlank(message = "Surname no puede estar vacío")
    private String surname;

    @Email(regexp = ".*@.*\\..*", message = "El Email debe de seguir el formato adecuado")
    @NotBlank(message = "Email no puede estar vacío")
    private String email;

    @NotBlank(message = "Password no puede estar vacío")
    @Size(min = 5, message = "Password debe tener al menos 5 caracteres")
    private String password;

    @Builder.Default
    private Set<Role> roles = Set.of(Role.USER);

    @Builder.Default
    private Boolean isDeleted = false;
}