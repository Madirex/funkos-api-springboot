package com.madirex.funkosspringrest.rest.entities.auth.services.authentication;

import com.madirex.funkosspringrest.rest.entities.auth.dto.JwtAuthResponse;
import com.madirex.funkosspringrest.rest.entities.auth.dto.UserSignInRequest;
import com.madirex.funkosspringrest.rest.entities.auth.dto.UserSignUpRequest;

/**
 * Interface AuthenticationService
 */
public interface AuthenticationService {
    /**
     * Registra un nuevo usuario
     *
     * @param request Datos de registro
     * @return Token de autenticación
     */
    JwtAuthResponse signUp(UserSignUpRequest request);

    /**
     * Inicia sesión
     *
     * @param request Datos de inicio de sesión
     * @return Token de autenticación
     */
    JwtAuthResponse signIn(UserSignInRequest request);
}