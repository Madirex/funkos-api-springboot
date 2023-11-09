package com.madirex.funkosspringrest.services.storage;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;
public interface StorageService {
    void init() throws IOException;
    String store(MultipartFile file) throws IOException;
    Stream<Path> loadAll();
    Path load(String filename);
    Resource loadAsResource(String filename);
    void delete(String filename) throws IOException;
    void deleteAll();
    String getUrl(String filename);
}
