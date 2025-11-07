package com.cityconnect.backend.controller;

import com.cityconnect.backend.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * REST API Controller for handling File Uploads.
 */
@RestController
@RequestMapping("/api/v1/files")
@CrossOrigin(origins = "http://localhost:5173") // Allow requests from our React frontend
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * Endpoint for uploading an image.
     * The frontend must send this as 'multipart/form-data'
     * with the file attached under the key "file".
     *
     * @param file The uploaded image file.
     * @return A JSON object with the web-accessible URL of the saved file.
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {

        // 1. Store the file using our service
        String fileUrl = fileStorageService.storeFile(file);

        // 2. Create a simple JSON response
        // e.g., { "url": "/media/123e4567-e89b-12d3-a456-426614174000.jpg" }
        Map<String, String> response = new HashMap<>();
        response.put("url", fileUrl);

        // 3. Return the response with an HTTP 200 OK
        return ResponseEntity.ok(response);
    }
}