package com.madirex.funkosspringrest.rest.entities.auth.controller;

import com.madirex.funkosspringrest.rest.entities.auth.dto.JwtAuthResponse;
import com.madirex.funkosspringrest.rest.entities.auth.dto.UserSignInRequest;
import com.madirex.funkosspringrest.rest.entities.auth.dto.UserSignUpRequest;
import com.madirex.funkosspringrest.rest.entities.auth.services.authentication.AuthenticationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador de autenticación
 */
@RestController
@Slf4j
@RequestMapping("api/auth")
public class AuthenticationRestController {
    private final AuthenticationService authenticationService;

    /**
     * Constructor
     *
     * @param authenticationService servicio de autenticación
     */
    @Autowired
    public AuthenticationRestController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Registra un usuario
     *
     * @param request datos del usuario
     * @return Token de autenticación
     */
    @PostMapping("/signup")
    public ResponseEntity<JwtAuthResponse> signUp(@Valid @RequestBody UserSignUpRequest request) {
        log.info("Registrando usuario: {}", request);
        return ResponseEntity.ok(authenticationService.signUp(request));
    }

    /**
     * Inicia sesión de un usuario
     *
     * @param request datos del usuario
     * @return Token de autenticación
     */
    @PostMapping("/signin")
    public ResponseEntity<JwtAuthResponse> signIn(@Valid @RequestBody UserSignInRequest request) {
        log.info("Iniciando sesión de usuario: {}", request);
        return ResponseEntity.ok(authenticationService.signIn(request));
    }
}