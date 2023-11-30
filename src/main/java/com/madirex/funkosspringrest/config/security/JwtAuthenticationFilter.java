package com.madirex.funkosspringrest.config.security;

import com.madirex.funkosspringrest.rest.entities.auth.services.jwt.JwtService;
import com.madirex.funkosspringrest.rest.entities.auth.services.users.AuthUsersService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro de autenticación
 */
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final AuthUsersService authUsersService;

    /**
     * Constructor
     *
     * @param jwtService       servicio de JWT
     * @param authUsersService servicio de usuarios
     */
    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, AuthUsersService authUsersService) {
        this.jwtService = jwtService;
        this.authUsersService = authUsersService;
    }

    /**
     * Filtro de autenticación
     *
     * @param request     petición
     * @param response    respuesta
     * @param filterChain cadena de filtros
     * @throws ServletException excepción de servlet
     * @throws IOException      excepción de entrada/salida
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        log.info("Iniciando el filtro de autenticación");
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        UserDetails userDetails;
        String userName;

        if (!StringUtils.hasText(authHeader) || !StringUtils.startsWithIgnoreCase(authHeader, "Bearer ")) {
            log.info("No se ha encontrado cabecera de autenticación. Se va a ignorar.");
            filterChain.doFilter(request, response);
            return;
        }

        log.info("Se ha encontrado cabecera de autenticación. Se va a procesar.");
        jwt = authHeader.substring(7);
        try {
            userName = jwtService.extractUserName(jwt);
        } catch (Exception e) {
            log.info("Token no válido");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token no autorizado o no válido");
            return;
        }
        log.info("Usuario autenticado: {}", userName);
        if (StringUtils.hasText(userName)
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.info("Comprobando usuario y token");
            try {
                userDetails = authUsersService.loadUserByUsername(userName);
            } catch (Exception e) {
                log.info("Usuario no encontrado: {}", userName);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuario no autorizado");
                return;
            }
            authUsersService.loadUserByUsername(userName);
            log.info("Usuario encontrado: {}", userDetails);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                log.info("JWT válido");
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
        }
        filterChain.doFilter(request, response);
    }
}