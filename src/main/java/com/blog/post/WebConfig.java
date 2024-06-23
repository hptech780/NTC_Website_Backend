package com.blog.post;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDir = getUploadDir();
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }

    private String getUploadDir() {
        try {
            return new ClassPathResource("static/images").getFile().getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException("Failed to determine upload directory", e);
        }
    }
}
