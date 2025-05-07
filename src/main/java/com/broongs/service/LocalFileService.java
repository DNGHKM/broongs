package com.broongs.service;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class LocalFileService implements FileService {
    private final String BASE_DIR;

    public LocalFileService(@Value("${file.base.dir}") String BASE_DIR) {
        this.BASE_DIR = BASE_DIR;
    }

    @Override
    public String uploadFile(MultipartFile file, String subDirectory) {
        String originalFilename = file.getOriginalFilename();
        String fileUUID = String.valueOf(UUID.randomUUID());
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fullFileName = fileUUID + ext;

        Path filePath = Paths.get(BASE_DIR, subDirectory, fullFileName);
        try {
            Files.createDirectories(filePath.getParent());

            Thumbnails.of(file.getInputStream())
                    .size(1600, 1200)
                    .outputQuality(0.9)
                    .toFile(filePath.toFile());

            return fullFileName;
        } catch (IOException e) {
            log.error("File upload failed: " + e.getMessage());
            throw new RuntimeException("File upload failed", e);
        }
    }


    @Override
    public ResponseEntity<Resource> downloadFile(String subDirectory, String filename) {
        Path filePath = Paths.get(BASE_DIR, subDirectory, filename);
        if (!Files.exists(filePath)) {
            throw new RuntimeException("File not found");
        }

        try {
            Resource resource = new InputStreamResource(Files.newInputStream(filePath));

            String encodedFileName = URLEncoder.encode(filename, StandardCharsets.UTF_8).replace("+", "%20");

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName)
                    .body(resource);

        } catch (IOException e) {
            throw new RuntimeException("File download failed", e);
        }
    }

    @Override
    public void deleteFile(String subDirectory, String filename) {
        Path filePath = Paths.get(BASE_DIR, subDirectory, filename);
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            log.info("{}", e.getMessage());
        }
    }

}

