package com.madirex.funkosspringrest.rest.entities.user.dto;

import com.madirex.funkosspringrest.rest.entities.user.models.Role;
import lombok.*;

import java.util.Set;
import java.util.UUID;

/**
 * Clase UserResponse
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String name;
    private String surname;
    private String username;
    private String email;
    @Builder.Default
    private Set<Role> roles = Set.of(Role.USER);
    @Builder.Default
    private Boolean isDeleted = false;
}