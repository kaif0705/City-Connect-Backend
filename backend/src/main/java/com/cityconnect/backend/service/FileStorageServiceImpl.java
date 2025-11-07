package com.cityconnect.backend.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Implementation of the FileStorageService.
 * This service handles saving files to a local 'uploads' directory.
 */
@Service
public class FileStorageServiceImpl implements FileStorageService {

    // Define the root storage location (e.g., ./uploads in your project root)
    private final Path rootLocation = Paths.get("uploads");

    // We'll map this web path to the rootLocation in our WebMvcConfig
    private static final String WEB_PATH = "/media/";

    /**
     * This method is run by Spring after the bean is created.
     * It creates the 'uploads' directory if it doesn't exist.
     */
    @Override
    @PostConstruct // Run this method on initialization
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage directory", e);
        }
    }

    /**
     * Stores the file on disk and returns the web-accessible path.
     */
    @Override
    public String storeFile(MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file.");
            }

            // 1. Get the original filename and extension
            String originalFilename = file.getOriginalFilename();
            String extension = StringUtils.getFilenameExtension(originalFilename);

            // 2. Generate a unique filename using UUID
            String uniqueFilename = UUID.randomUUID().toString() + "." + extension;

            // 3. Resolve the full destination path
            // This ensures the path is absolute and within our storage directory
            Path destinationFile = this.rootLocation.resolve(uniqueFilename)
                    .toAbsolutePath();

            // 4. Copy the file's InputStream to the destination path
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            // 5. Return the web-accessible path
            return WEB_PATH + uniqueFilename;

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }

    /**
     * Deletes a file from the filesystem based on its web path.
     */
    @Override
    public void deleteFile(String webPath) {
        // Do nothing if the path is null or blank
        if (webPath == null || webPath.isBlank()) {
            return;
        }

        try {
            // 1. Convert the web path (e.g., /media/foo.jpg)
            //    to just the filename (e.g., foo.jpg)
            String filename = webPath.substring(WEB_PATH.length());

            // 2. Resolve the full file path on the disk
            Path filePath = this.rootLocation.resolve(filename).toAbsolutePath();

            // 3. Delete the file if it exists
            Files.deleteIfExists(filePath);

        } catch (IOException e) {
            // We can log this, but we don't want to fail the whole operation
            // (e.g., if the issue deletes but the file fails, it's not critical)
            System.err.println("Could not delete file: " + webPath);
            e.printStackTrace();
        }
    }
}