package com.madirex.funkosspringrest.config;

import com.madirex.funkosspringrest.storage.services.StorageService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Clase StorageConfig
 */
@Configuration
@Slf4j
public class StorageConfig {
    private final StorageService storageService;
    private final String deleteAll;

    /**
     * Constructor
     *
     * @param storageService Servicio de almacenamiento
     * @param deleteAll      Borrar todos los ficheros
     */
    @Autowired
    public StorageConfig(StorageService storageService, @Value("${upload.delete}") String deleteAll) {
        this.storageService = storageService;
        this.deleteAll = deleteAll;
    }

    /**
     * Método init
     *
     * @throws IOException Error al inicializar el servicio de almacenamiento
     */
    @PostConstruct
    public void init() throws IOException {
        if (deleteAll.equals("true")) {
            log.info("Borrando ficheros de almacenamiento...");
            storageService.deleteAll();
        }
        storageService.init();
    }
}