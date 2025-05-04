package com.broongs.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String uploadFile(MultipartFile file, String subDirectory);

    ResponseEntity<Resource> downloadFile(String subDirectory, String filename);

    void deleteFile(String subDirectory, String filename);
}

