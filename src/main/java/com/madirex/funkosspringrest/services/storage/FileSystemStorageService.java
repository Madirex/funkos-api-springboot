package com.madirex.funkosspringrest.services.storage;

import com.madirex.funkosspringrest.controllers.storage.StorageController;
import com.madirex.funkosspringrest.exceptions.storage.StorageBadRequest;
import com.madirex.funkosspringrest.exceptions.storage.StorageInternal;
import com.madirex.funkosspringrest.exceptions.storage.StorageNotFound;
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

@Service
@Slf4j
public class FileSystemStorageService implements StorageService {
    private final Path rootLocation;

    public FileSystemStorageService(@Value("${upload.root-location}") String path) {
        this.rootLocation = Paths.get(path);
    }

    @Override
    public String store(MultipartFile file, List<String> fileTypes, String name) throws IOException {
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String extension = StringUtils.getFilenameExtension(filename);
        String justFilename = filename.replace("." + extension, "");
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
                !fileTypes.contains(detectFileType(file.getBytes())))) {
            throw new StorageBadRequest("Tipo de fichero no permitido " + filename);
        }
        try (InputStream inputStream = file.getInputStream()) {
            log.info("Almacenando fichero " + filename + " como " + storedFilename);
            Files.copy(inputStream, this.rootLocation.resolve(storedFilename),
                    StandardCopyOption.REPLACE_EXISTING);
            return storedFilename;
        }
    }

    private String detectFileType(byte[] bytes) {
        if (bytes.length >= 2 && bytes[0] == (byte) 0xFF && bytes[1] == (byte) 0xD8) {
            return "jpeg";
        } else if (bytes.length >= 8 &&
                bytes[0] == (byte) 0x89 &&
                bytes[1] == (byte) 0x50 &&
                bytes[2] == (byte) 0x4E &&
                bytes[3] == (byte) 0x47 &&
                bytes[4] == (byte) 0x0D &&
                bytes[5] == (byte) 0x0A &&
                bytes[6] == (byte) 0x1A &&
                bytes[7] == (byte) 0x0A) {
            return "png";
        } else if (bytes.length >= 6 &&
                bytes[0] == (byte) 0x47 &&
                bytes[1] == (byte) 0x49 &&
                bytes[2] == (byte) 0x46 &&
                bytes[3] == (byte) 0x38) {
            return "gif";
        } else {
            return "application/octet-stream";
        }
    }

    @Override
    public Stream<Path> loadAll() {
        log.info("Cargando todos los ficheros almacenados");
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageInternal("Fallo al leer ficheros almacenados " + e);
        }
    }

    @Override
    public Path load(String filename) {
        log.info("Cargando fichero " + filename);
        return rootLocation.resolve(filename);
    }

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

    @Override
    public void deleteAll() {
        log.info("Eliminando todos los ficheros almacenados");
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() throws IOException {
        log.info("Inicializando almacenamiento");
        Files.createDirectories(rootLocation);
    }

    @Override
    public void delete(String filename) throws IOException {
        String justFilename = StringUtils.getFilename(filename);
        log.info("Eliminando fichero " + filename);
        Path file = load(justFilename);
        Files.deleteIfExists(file);
    }

    @Override
    public String getUrl(String filename) {
        log.info("Obteniendo URL del fichero " + filename);
        return MvcUriComponentsBuilder
                .fromMethodName(StorageController.class, "serveFile", filename, null)
                .build().toUriString();
    }
}