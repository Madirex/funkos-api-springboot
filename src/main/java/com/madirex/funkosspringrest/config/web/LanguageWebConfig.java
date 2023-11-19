package com.madirex.funkosspringrest.config.web;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Clase LanguageWebConfig
 */
@Configuration
public class LanguageWebConfig {

    /**
     * Método para configurar el MessageSource
     * Carga el idioma en Resource Bundle "messages"
     *
     * @return MessageSource
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}