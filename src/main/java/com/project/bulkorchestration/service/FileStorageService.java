package com.project.bulkorchestration.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path baseDir = Paths.get("data", "imports");

    public String store(MultipartFile file) {
        try {
            if (!Files.exists(baseDir)) {
                Files.createDirectories(baseDir);
            }
            String originalFileName = file.getOriginalFilename();
            String safeFilename = (originalFileName == null || originalFileName.isBlank())
                    ? "uploaded.csv" : originalFileName;
            String storedFileName = UUID.randomUUID() + "_" + safeFilename;
            Path targetPath = baseDir.resolve(storedFileName);
            Files.copy(file.getInputStream(), targetPath);
            return targetPath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

}
