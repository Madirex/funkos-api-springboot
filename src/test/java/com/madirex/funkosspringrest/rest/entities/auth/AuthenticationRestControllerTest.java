package com.madirex.funkosspringrest.rest.entities.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.madirex.funkosspringrest.rest.entities.auth.dto.JwtAuthResponse;
import com.madirex.funkosspringrest.rest.entities.auth.dto.UserSignInRequest;
import com.madirex.funkosspringrest.rest.entities.auth.dto.UserSignUpRequest;
import com.madirex.funkosspringrest.rest.entities.auth.exceptions.AuthSignInInvalid;
import com.madirex.funkosspringrest.rest.entities.auth.exceptions.PasswordsDiffer;
import com.madirex.funkosspringrest.rest.entities.auth.services.authentication.AuthenticationService;
import com.madirex.funkosspringrest.rest.entities.user.exceptions.UsernameOrEmailExists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Clase AuthenticationRestControllerTest
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class AuthenticationRestControllerTest {
    private final String myEndpoint = "/api/auth";
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthenticationService authenticationService;

    /**
     * Constructor AuthenticationRestControllerTest
     *
     * @param authenticationService AuthenticationService
     */
    @Autowired
    public AuthenticationRestControllerTest(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
        mapper.registerModule(new JavaTimeModule());
    }

    /**
     * Test para comprobar que el método signUp devuelve un token cuando los datos son correctos
     *
     * @throws Exception Excepción
     */
    @Test
    void signUp() throws Exception {
        var userSignUpRequest = new UserSignUpRequest("Test", "Test", "test",
                "test@test.com", "test12345", "test12345");
        var jwtAuthResponse = new JwtAuthResponse("token");
        var myLocalEndpoint = myEndpoint + "/signup";
        when(authenticationService.signUp(any(UserSignUpRequest.class))).thenReturn(jwtAuthResponse);

        MockHttpServletResponse response = mockMvc.perform(
                        post(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userSignUpRequest)))
                .andReturn().getResponse();

        JwtAuthResponse res = mapper.readValue(response.getContentAsString(), JwtAuthResponse.class);

        assertAll("signup",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals("token", res.getToken())
        );

        verify(authenticationService, times(1)).signUp(any(UserSignUpRequest.class));
    }

    /**
     * Test para comprobar que el método signUp lanza una excepción cuando las contraseñas no coinciden
     */
    @Test
    void signUp_WhenPasswordsDoNotMatch_ShouldThrowException() {
        UserSignUpRequest request = new UserSignUpRequest();
        request.setUsername("testuser");
        request.setPassword("password");
        request.setPasswordRepeat("password2");
        request.setEmail("test@test.com");
        request.setName("Test");
        request.setSurname("User");

        when(authenticationService.signUp(any(UserSignUpRequest.class))).thenThrow(new PasswordsDiffer("Las contraseñas no coinciden"));

        assertThrows(PasswordsDiffer.class, () -> authenticationService.signUp(request));

        verify(authenticationService, times(1)).signUp(any(UserSignUpRequest.class));
    }

    /**
     * Test para comprobar que el método signUp lanza una excepción cuando el nombre de usuario o el email ya existen
     */
    @Test
    void signUp_WhenUsernameOrEmailAlreadyExist_ShouldThrowException() {
        UserSignUpRequest request = new UserSignUpRequest();
        request.setUsername("testuser");
        request.setPassword("password");
        request.setPasswordRepeat("password2");
        request.setEmail("test@test.com");
        request.setName("Test");
        request.setSurname("User");

        when(authenticationService.signUp(any(UserSignUpRequest.class))).thenThrow(new UsernameOrEmailExists("El usuario con username " + request.getUsername() + " o email " + request.getEmail() + " ya existe"));

        assertThrows(UsernameOrEmailExists.class, () -> authenticationService.signUp(request));

        verify(authenticationService, times(1)).signUp(any(UserSignUpRequest.class));
    }

    /**
     * Test para comprobar que el método signUp lanza una excepción cuando manda un nombre de usuario, email, nombre o apellidos vacíos
     *
     * @throws Exception Excepción
     */
    @Test
    void signUp_BadRequest_When_Name_Surname_Email_Username_Empty_ShouldThrowException() throws Exception {
        var myLocalEndpoint = myEndpoint + "/signup";
        UserSignUpRequest request = new UserSignUpRequest();
        request.setUsername("");
        request.setPassword("password");
        request.setPasswordRepeat("password");
        request.setEmail("");
        request.setName("");
        request.setSurname("");

        MockHttpServletResponse response = mockMvc.perform(
                        post(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request)))
                .andReturn().getResponse();

        assertAll("signup",
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("Name no puede estar")),
                () -> assertTrue(response.getContentAsString().contains("Surname no puede ")),
                () -> assertTrue(response.getContentAsString().contains("Username no puede"))
        );
    }

    /**
     * Test para comprobar que el método signIn devuelve un token cuando el nombre de usuario y contraseña son correctos
     *
     * @throws Exception Excepción
     */
    @Test
    void signIn() throws Exception {
        var userSignUpRequest = new UserSignUpRequest("Test", "Test", "test", "test@test.com", "test12345", "test12345");
        var jwtAuthResponse = new JwtAuthResponse("token");
        var myLocalEndpoint = myEndpoint + "/signin";
        when(authenticationService.signIn(any(UserSignInRequest.class))).thenReturn(jwtAuthResponse);

        MockHttpServletResponse response = mockMvc.perform(
                        post(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userSignUpRequest)))
                .andReturn().getResponse();

        JwtAuthResponse res = mapper.readValue(response.getContentAsString(), JwtAuthResponse.class);

        assertAll("signin",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals("token", res.getToken())
        );

        verify(authenticationService, times(1)).signIn(any(UserSignInRequest.class));
    }

    /**
     * Test para comprobar que el método signIn lanza una excepción cuando el nombre de usuario y contraseña son incorrectos
     */
    @Test
    void signIn_Invalid() {
        UserSignInRequest request = new UserSignInRequest();
        request.setUsername("testuser");
        request.setPassword("<PASSWORD>");

        when(authenticationService.signIn(any(UserSignInRequest.class))).thenThrow(new AuthSignInInvalid("Usuario o contraseña incorrectos"));

        assertThrows(AuthSignInInvalid.class, () -> authenticationService.signIn(request));

        verify(authenticationService, times(1)).signIn(any(UserSignInRequest.class));
    }

    /**
     * Test para comprobar que el método signIn lanza una excepción cuando el nombre de usuario y contraseña están vacíos
     *
     * @throws Exception Excepción
     */
    @Test
    void signIn_BadRequest_When_Username_Password_Empty_ShouldThrowException() throws Exception {
        var myLocalEndpoint = myEndpoint + "/signin";
        UserSignInRequest request = new UserSignInRequest();
        request.setUsername("");
        request.setPassword("");

        MockHttpServletResponse response = mockMvc.perform(
                        post(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request)))
                .andReturn().getResponse();

        assertAll("signin",
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("Username no puede"))
        );
    }

}