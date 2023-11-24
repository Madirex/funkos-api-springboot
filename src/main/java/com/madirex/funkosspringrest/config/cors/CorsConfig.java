package com.madirex.funkosspringrest.config.cors;

import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de CORS
 */
@Configuration
public class CorsConfig {
    /**
     * Configuración de CORS
     *
     * @return configuración de CORS
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            /**
             * Configuración de CORS
             * @param registry registro de CORS
             */
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE")
                        .maxAge(3600);
            }
        };
    }
}