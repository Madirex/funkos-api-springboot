package com.madirex.funkosspringrest.storage.services;

import com.madirex.funkosspringrest.storage.controller.StorageController;
import com.madirex.funkosspringrest.storage.exceptions.StorageBadRequest;
import com.madirex.funkosspringrest.storage.exceptions.StorageInternal;
import com.madirex.funkosspringrest.storage.exceptions.StorageNotFound;
import com.madirex.funkosspringrest.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Clase FileSystemStorageService
 */
@Service
@Slf4j
public class FileSystemStorageService implements StorageService {
    private final Path rootLocation;

    /**
     * Constructor de la clase
     *
     * @param path ruta
     */
    public FileSystemStorageService(@Value("${upload.root-location}") String path) {
        this.rootLocation = Paths.get(path);
    }

    /**
     * Almacena un fichero
     *
     * @param file      fichero
     * @param fileTypes tipos de fichero
     * @param name      nombre
     * @return nombre del fichero
     * @throws IOException excepción de entrada/salida
     */
    @Override
    public String store(MultipartFile file, List<String> fileTypes, String name) throws IOException {
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String extension = StringUtils.getFilenameExtension(filename);
        String storedFilename = name + "." + extension;

        if (file.isEmpty()) {
            throw new StorageBadRequest("Fichero vacío " + filename);
        }
        if (filename.contains("..")) {
            throw new StorageBadRequest(
                    "No se puede almacenar un fichero con una ruta relativa fuera del directorio actual "
                            + filename);
        }
        if (fileTypes != null && !fileTypes.isEmpty() && (!fileTypes.contains(extension) ||
                !fileTypes.contains(Util.detectFileType(file.getBytes())))) {
            throw new StorageBadRequest("Tipo de fichero no permitido " + filename);
        }
        try (InputStream inputStream = file.getInputStream()) {
            log.info("Almacenando fichero " + filename + " como " + storedFilename);
            Files.copy(inputStream, this.rootLocation.resolve(storedFilename),
                    StandardCopyOption.REPLACE_EXISTING);
            return storedFilename;
        }
    }

    /**
     * Carga todos los ficheros
     *
     * @return Stream de Path
     */
    @Override
    public Stream<Path> loadAll() {
        log.info("Cargando todos los ficheros almacenados");
        try (Stream<Path> pathStream = Files.walk(this.rootLocation, 1)
                .filter(path -> !path.equals(this.rootLocation))
                .map(this.rootLocation::relativize)) {
            return pathStream.toList().stream();
        } catch (IOException e) {
            throw new StorageInternal("Fallo al leer ficheros almacenados " + e);
        }
    }

    /**
     * Carga un fichero
     *
     * @param filename nombre del fichero
     * @return Path
     */
    @Override
    public Path load(String filename) {
        log.info("Cargando fichero " + filename);
        return rootLocation.resolve(filename);
    }

    /**
     * Carga un fichero como recurso
     *
     * @param filename nombre del fichero
     * @return Resource
     * @throws MalformedURLException excepción de URL mal formada
     */
    @Override
    public Resource loadAsResource(String filename) throws MalformedURLException {
        log.info("Cargando fichero " + filename);
        Path file = load(filename);
        Resource resource = new UrlResource(file.toUri());
        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new StorageNotFound("No se puede leer el fichero: " + filename);
        }
    }

    /**
     * Elimina todos los ficheros
     */
    @Override
    public void deleteAll() {
        log.info("Eliminando todos los ficheros almacenados");
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    /**
     * Inicializa el almacenamiento
     *
     * @throws IOException excepción de entrada/salida
     */
    @Override
    public void init() throws IOException {
        log.info("Inicializando almacenamiento");
        Files.createDirectories(rootLocation);
    }

    /**
     * Elimina un fichero
     *
     * @param filename nombre del fichero
     * @throws IOException excepción de entrada/salida
     */
    @Override
    public void delete(String filename) throws IOException {
        String justFilename = StringUtils.getFilename(filename);
        log.info("Eliminando fichero " + filename);
        Path file = load(justFilename);
        Files.deleteIfExists(file);
    }

    /**
     * Obtiene la URL de un fichero
     *
     * @param filename nombre del fichero
     * @return URL del fichero
     */
    @Override
    public String getUrl(String filename) {
        log.info("Obteniendo URL del fichero " + filename);
        return MvcUriComponentsBuilder
                .fromMethodName(StorageController.class, "serveFile", filename, null)
                .build().toUriString();
    }
}