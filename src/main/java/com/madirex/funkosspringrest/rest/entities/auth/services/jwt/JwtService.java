package com.madirex.funkosspringrest.rest.entities.auth.services.jwt;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    /**
     * Extrae el nombre de usuario del token
     *
     * @param token Token
     * @return Nombre de usuario
     */
    String extractUserName(String token);

    /**
     * Genera un token
     *
     * @param userDetails Detalles del usuario
     * @return Token
     */
    String generateToken(UserDetails userDetails);

    /**
     * Verifica si el token es válido
     *
     * @param token       Token
     * @param userDetails Detalles del usuario
     * @return Boolean
     */
    boolean isTokenValid(String token, UserDetails userDetails);
}