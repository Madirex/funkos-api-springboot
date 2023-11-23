package com.madirex.funkosspringrest.rest.entities.user.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Clase User
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USERS")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {
    @Id
    @NotNull
    private UUID id;

    @NotBlank(message = "Name no puede estar vacío")
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "Surname no puede estar vacío")
    private String surname;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Username no puede estar vacío")
    private String username;

    @Column(unique = true, nullable = false)
    @Email(regexp = ".*@.*\\..*", message = "El Email debe de seguir el formato adecuado")
    @NotBlank(message = "Email no puede estar vacío")
    private String email;

    @NotBlank(message = "Password no puede estar vacío")
    @Length(min = 5, message = "Password debe tener al menos 5 caracteres")
    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    @Column(updatable = false, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(columnDefinition = "boolean default false")
    @Builder.Default
    private Boolean isDeleted = false;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    /**
     * Método que devuelve los roles del usuario
     *
     * @return roles del usuario
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());
    }

    /**
     * Método que devuelve el userName (clave primaria por la que se buscará)
     *
     * @return username (clave primaria por la que se buscará)
     */
    @Override
    public String getUsername() {
        // email in our case
        return username;
    }

    /**
     * Método que devuelve si la cuenta del usuario ha expirado o no
     *
     * @return true si la cuenta del usuario no ha expirado, false en caso contrario
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Método que devuelve si la cuenta del usuario está bloqueada o no
     *
     * @return true si la cuenta del usuario no está bloqueada, false en caso contrario
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Método que devuelve si las credenciales del usuario han expirado o no
     *
     * @return true si las credenciales del usuario no han expirado, false en caso contrario
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Método que devuelve si el usuario está habilitado o no
     *
     * @return true si el usuario está habilitado, false en caso contrario
     */
    @Override
    public boolean isEnabled() {
        return !isDeleted;
    }
}