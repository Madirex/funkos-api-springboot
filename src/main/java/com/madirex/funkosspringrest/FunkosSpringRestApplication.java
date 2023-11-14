package com.madirex.funkosspringrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase FunkosSpringRestApplication
 */
@SpringBootApplication
@EnableJpaAuditing
public class FunkosSpringRestApplication {

    /**
     * Método main
     *
     * @param args Argumentos de la aplicación
     */
    public static void main(String[] args) {
        SpringApplication.run(FunkosSpringRestApplication.class, args);
    }
}
