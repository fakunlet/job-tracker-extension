package com.example.jobtracker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Browsers block a page on one origin from reading a response from a different
 * origin unless the server opts in. The extension popup's origin is
 * "chrome-extension://<some-id>", which is not "http://localhost:8080", so
 * without this the POST from popup.js fails with a CORS error.
 *
 * The extension id changes every time the folder is loaded unpacked, so the
 * pattern matches any chrome-extension origin rather than one fixed id.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                // allowedOriginPatterns, not allowedOrigins, because only the
                // pattern version supports a wildcard in the origin.
                .allowedOriginPatterns("chrome-extension://*")
                .allowedMethods("GET", "POST")
                .allowedHeaders("Content-Type");
    }
}
