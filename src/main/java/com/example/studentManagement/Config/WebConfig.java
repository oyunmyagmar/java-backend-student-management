package com.example.studentManagement.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Ингэснээр браузер дээр http://localhost:8086/uploads/зураг.jpg гэж хандахад
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///C:/upload/");
    }
}