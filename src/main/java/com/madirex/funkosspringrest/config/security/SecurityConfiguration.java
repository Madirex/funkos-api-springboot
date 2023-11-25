package com.madirex.funkosspringrest.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Clase SecurityConfiguration
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private final UserDetailsService userService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Constructor
     *
     * @param userService             UserDetailsService
     * @param jwtAuthenticationFilter JwtAuthenticationFilter
     */
    @Autowired
    public SecurityConfiguration(UserDetailsService userService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userService = userService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Método para configurar la seguridad
     *
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception Excepción
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        //acceso a los recursos estáticos para todos los usuarios
                        .requestMatchers("/error/**").permitAll()
                        .requestMatchers("/static/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("/webjars/**").permitAll()
                        .requestMatchers("/storage/**").permitAll()
                        //WebSockets y Swagger solo para Admins
                        .requestMatchers("/ws/**").hasRole("ADMIN")
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").hasRole("ADMIN")
                        //acceso a autentificación para todos los usuarios
                        .requestMatchers("/api/auth/**").permitAll()
                        //Acceso a los endpoints de la API solo para administradores
                        .requestMatchers("/api/orders/**").hasRole("ADMIN")
                        //Acceso al perfil para usuarios autenticados
                        //TODO: ORDERS
                        //TODO: USER
                        //TODO: FUNKO
                        //TODO: CATEGORY
//                        .requestMatchers("/api/**").permitAll()
                        .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider()).addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Método para configurar PasswordEncoder
     *
     * @return SecurityFilterChain
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Método para configurar AuthenticationProvider
     *
     * @return AuthenticationProvider
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Método para configurar AuthenticationManager
     *
     * @param config AuthenticationConfiguration
     * @return AuthenticationManager
     * @throws Exception Excepción
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}