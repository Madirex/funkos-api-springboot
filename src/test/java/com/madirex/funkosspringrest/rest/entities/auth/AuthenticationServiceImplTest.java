package com.madirex.funkosspringrest.rest.entities.auth;

import com.madirex.funkosspringrest.rest.entities.auth.dto.JwtAuthResponse;
import com.madirex.funkosspringrest.rest.entities.auth.dto.UserSignInRequest;
import com.madirex.funkosspringrest.rest.entities.auth.dto.UserSignUpRequest;
import com.madirex.funkosspringrest.rest.entities.auth.exceptions.AccountAlreadyExists;
import com.madirex.funkosspringrest.rest.entities.auth.exceptions.AuthSignInInvalid;
import com.madirex.funkosspringrest.rest.entities.auth.exceptions.PasswordsDiffer;
import com.madirex.funkosspringrest.rest.entities.auth.repository.AuthUsersRepository;
import com.madirex.funkosspringrest.rest.entities.auth.services.authentication.AuthenticationServiceImpl;
import com.madirex.funkosspringrest.rest.entities.auth.services.jwt.JwtService;
import com.madirex.funkosspringrest.rest.entities.user.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Clase AuthenticationServiceImplTest
 */
@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private AuthUsersRepository authUsersRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    /**
     * Test para comprobar que el método signUp devuelve un token cuando los datos son correctos
     */
    @Test
    void testSignUp_WhenPasswordsMatch_ShouldReturnToken() {
        UserSignUpRequest request = new UserSignUpRequest();
        request.setUsername("testuser");
        request.setPassword("password");
        request.setPasswordRepeat("password");
        request.setEmail("test@example.com");
        request.setName("Test");
        request.setSurname("User");

        User userStored = new User();
        when(authUsersRepository.save(any(User.class))).thenReturn(userStored);

        String token = "test_token";
        when(jwtService.generateToken(userStored)).thenReturn(token);

        JwtAuthResponse response = authenticationService.signUp(request);

        assertAll("Sign Up",
                () -> assertNotNull(response),
                () -> assertEquals(token, response.getToken()),
                () -> verify(authUsersRepository, times(1)).save(any(User.class)),
                () -> verify(jwtService, times(1)).generateToken(userStored)
        );
    }

    /**
     * Test para comprobar que el método signUp lanza una excepción cuando las contraseñas no coinciden
     */
    @Test
    void testSignUp_WhenPasswordsDoNotMatch_ShouldThrowException() {
        UserSignUpRequest request = new UserSignUpRequest();
        request.setUsername("testuser");
        request.setPassword("password1");
        request.setPasswordRepeat("password");
        request.setEmail("test@example.com");
        request.setName("Test");
        request.setSurname("User");

        assertThrows(PasswordsDiffer.class, () -> authenticationService.signUp(request));
    }

    /**
     * Test para comprobar que el método signUp lanza una excepción cuando el nombre de usuario o el email ya existen
     */
    @Test
    void testSignUp_WhenUsernameOrEmailAlreadyExist_ShouldThrowException() {
        UserSignUpRequest request = new UserSignUpRequest();
        request.setUsername("testuser");
        request.setPassword("password");
        request.setPasswordRepeat("password");
        request.setEmail("test@example.com");
        request.setName("Test");
        request.setSurname("User");

        when(authUsersRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(AccountAlreadyExists.class, () -> authenticationService.signUp(request));
    }

    /**
     * Test para comprobar que el método signIn devuelve un token cuando las credenciales son válidas
     */
    @Test
    void testSignIn_WhenValidCredentials_ShouldReturnToken() {
        UserSignInRequest request = new UserSignInRequest();
        request.setPassword("password");
        request.setUsername("test");

        User user = new User();
        when(authUsersRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(user));

        String token = "test_token";
        when(jwtService.generateToken(user)).thenReturn(token);

        JwtAuthResponse response = authenticationService.signIn(request);

        assertAll("Sign In",
                () -> assertNotNull(response),
                () -> assertEquals(token, response.getToken()),
                () -> verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class)),
                () -> verify(authUsersRepository, times(1)).findByUsername(request.getUsername()),
                () -> verify(jwtService, times(1)).generateToken(user)
        );
    }

    /**
     * Test para comprobar que el método signIn lanza una excepción cuando las credenciales no son válidas
     */
    @Test
    void testSignIn_WhenInvalidCredentials_ShouldThrowException() {
        UserSignInRequest request = new UserSignInRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        when(authUsersRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());

        assertThrows(AuthSignInInvalid.class, () -> authenticationService.signIn(request));
    }
}