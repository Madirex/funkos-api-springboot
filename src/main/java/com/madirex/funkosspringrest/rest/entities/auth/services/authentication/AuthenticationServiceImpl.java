package com.madirex.funkosspringrest.rest.entities.auth.services.authentication;

import com.madirex.funkosspringrest.rest.entities.auth.dto.JwtAuthResponse;
import com.madirex.funkosspringrest.rest.entities.auth.dto.UserSignInRequest;
import com.madirex.funkosspringrest.rest.entities.auth.dto.UserSignUpRequest;
import com.madirex.funkosspringrest.rest.entities.auth.exceptions.AccountAlreadyExists;
import com.madirex.funkosspringrest.rest.entities.auth.exceptions.AuthSignInInvalid;
import com.madirex.funkosspringrest.rest.entities.auth.exceptions.PasswordsDiffer;
import com.madirex.funkosspringrest.rest.entities.auth.repository.AuthUsersRepository;
import com.madirex.funkosspringrest.rest.entities.auth.services.jwt.JwtService;
import com.madirex.funkosspringrest.rest.entities.user.models.Role;
import com.madirex.funkosspringrest.rest.entities.user.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementación de servicio de autenticación
 */
@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthUsersRepository authUsersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Constructor
     *
     * @param authUsersRepository   repositorio de autenticación de usuarios
     * @param passwordEncoder       encoder de contraseñas
     * @param jwtService            servicio de JWT
     * @param authenticationManager gestor de autenticación
     */
    @Autowired
    public AuthenticationServiceImpl(AuthUsersRepository authUsersRepository, PasswordEncoder passwordEncoder,
                                     JwtService jwtService, AuthenticationManager authenticationManager) {
        this.authUsersRepository = authUsersRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registra un usuario
     *
     * @param request datos del usuario
     * @return Token de autenticación
     */
    @Override
    public JwtAuthResponse signUp(UserSignUpRequest request) {
        log.info("Creando usuario: {}", request);
        if (request.getPassword().contentEquals(request.getPasswordRepeat())) {
            User user = User.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .email(request.getEmail())
                    .name(request.getName())
                    .surname(request.getSurname())
                    .roles(Stream.of(Role.USER).collect(Collectors.toSet()))
                    .build();
            try {
                var userStored = authUsersRepository.save(user);
                return JwtAuthResponse.builder().token(jwtService.generateToken(userStored)).build();
            } catch (DataIntegrityViolationException ex) {
                throw new AccountAlreadyExists("El usuario con username " + request.getUsername() + " o email " +
                        request.getEmail() + " no están disponibles.");
            }
        } else {
            throw new PasswordsDiffer("Las contraseñas no coinciden");

        }
    }

    /**
     * Autentica un usuario
     *
     * @param request datos del usuario
     * @return Token de autenticación
     */
    @Override
    public JwtAuthResponse signIn(UserSignInRequest request) {
        log.info("Autenticando usuario: {}", request);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = authUsersRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthSignInInvalid("Usuario o contraseña incorrectos"));
        var jwt = jwtService.generateToken(user);
        return JwtAuthResponse.builder().token(jwt).build();
    }
}