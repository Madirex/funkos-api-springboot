package com.madirex.funkosspringrest.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Clase SecurityConfiguration
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    /**
     * Método para configurar la seguridad
     *
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception Excepción
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request.requestMatchers("/error/**")
                        .permitAll())
                .authorizeHttpRequests(request -> request.requestMatchers("/api/funkos/**")
                        .permitAll())
                .authorizeHttpRequests(request -> request.requestMatchers("/api/category/**")
                        .permitAll())
                .authorizeHttpRequests(request -> request.requestMatchers("/api/orders/**")
                        .permitAll());
        return http.build();
    }
}