package com.cityconnect.backend.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Interface for the file storage service,
 * which handles saving, loading, and deleting files.
 */
public interface FileStorageService {

    /**
     * Initializes the storage directory on application startup.
     */
    void init();

    /**
     * Stores a file on the server's filesystem.
     *
     * @param file The MultipartFile to store.
     * @return The web-accessible path to the stored file (e.g., /media/filename.jpg).
     */
    String storeFile(MultipartFile file);

    /**
     * Deletes a file from the filesystem.
     *
     * @param webPath The web-accessible path (e.g., /media/filename.jpg)
     */
    void deleteFile(String webPath);
}