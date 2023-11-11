package com.madirex.funkosspringrest.services.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface StorageService {
    void init() throws IOException;

    String store(MultipartFile file, List<String> fileTypes, String name) throws IOException;

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename) throws MalformedURLException;

    void delete(String filename) throws IOException;

    void deleteAll();

    String getUrl(String filename);
}
