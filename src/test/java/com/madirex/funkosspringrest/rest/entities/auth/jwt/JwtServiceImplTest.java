package com.madirex.funkosspringrest.rest.entities.auth.jwt;

import com.madirex.funkosspringrest.rest.entities.auth.services.jwt.JwtServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Clase JwtServiceImplTest
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class JwtServiceImplTest {

    @Mock
    private UserDetails userDetails;

    @Autowired
    private JwtServiceImpl jwtService;

    /**
     * Test para comprobar que se genera el token
     */
    @Test
    void testGenerateToken() {
        when(userDetails.getUsername()).thenReturn("testUser");
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    /**
     * Test para comprobar que se extrae el nombre de usuario del token
     */
    @Test
    void testExtractUserName() {
        when(userDetails.getUsername()).thenReturn("testUser");
        String token = jwtService.generateToken(userDetails);
        String extractedUserName = jwtService.extractUserName(token);
        assertNotNull(extractedUserName);
        assertEquals("testUser", extractedUserName);
    }

    /**
     * Test para comprobar que el token es válido
     */
    @Test
    void testIsTokenValid() {
        when(userDetails.getUsername()).thenReturn("testUser");
        String token = jwtService.generateToken(userDetails);
        boolean isValid = jwtService.isTokenValid(token, userDetails);
        assertTrue(isValid);
    }
}