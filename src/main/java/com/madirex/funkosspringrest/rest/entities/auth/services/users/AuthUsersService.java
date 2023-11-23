package com.madirex.funkosspringrest.rest.entities.auth.services.users;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Interface AuthUsersService
 */
public interface AuthUsersService extends UserDetailsService {
    /**
     * Carga un usuario por su nombre de usuario
     *
     * @param username Nombre de usuario
     * @return Usuario
     */
    @Override
    UserDetails loadUserByUsername(String username);
}