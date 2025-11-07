package com.cityconnect.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Configuration class to map web requests for static resources
 * (like uploaded images) to physical locations on the server's disk.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // This is the physical directory where files are stored.
    // It MUST match the 'rootLocation' in FileStorageServiceImpl.
    private final Path rootLocation = Paths.get("uploads");

    /**
     * This method maps the web path "/media/**" to the
     * physical directory "./uploads/".
     *
     * For example, a request to "http://localhost:8080/media/my-image.jpg"
     * will serve the file from the "./uploads/my-image.jpg" directory.
     *
     * This is necessary so the frontend can display the images we upload.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // We use rootLocation.toUri().toString() to get the absolute path
        // in a format Spring understands (e.g., "file:///D:/path/to/project/uploads/")
        registry.addResourceHandler("/media/**")
                .addResourceLocations(rootLocation.toUri().toString());
    }
}