package com.madirex.funkosspringrest.services.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * Interface StorageService
 */
public interface StorageService {
    /**
     * Inicializa el almacenamiento
     *
     * @throws IOException excepción de entrada/salida
     */
    void init() throws IOException;

    /**
     * Almacena un fichero
     *
     * @param file      fichero
     * @param fileTypes tipos de fichero
     * @param name      nombre
     * @return nombre del fichero
     * @throws IOException excepción de entrada/salida
     */
    String store(MultipartFile file, List<String> fileTypes, String name) throws IOException;

    /**
     * Carga todos los ficheros
     *
     * @return Stream de Path
     */
    Stream<Path> loadAll();

    /**
     * Carga un fichero
     *
     * @param filename nombre del fichero
     * @return Path
     */
    Path load(String filename);

    /**
     * Carga un fichero como recurso
     *
     * @param filename nombre del fichero
     * @return Resource
     * @throws MalformedURLException excepción de URL mal formada
     */
    Resource loadAsResource(String filename) throws MalformedURLException;

    /**
     * Elimina un fichero
     *
     * @param filename nombre del fichero
     * @throws IOException excepción de entrada/salida
     */
    void delete(String filename) throws IOException;

    /**
     * Elimina todos los ficheros
     */
    void deleteAll();

    /**
     * Obtiene la URL de un fichero
     *
     * @param filename nombre del fichero
     * @return URL del fichero
     */
    String getUrl(String filename);
}
