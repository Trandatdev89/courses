package com.project01.skillineserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/lecture/**","/uploads/image/**")
                .addResourceLocations("file:uploads/image/")
                .addResourceLocations("file:uploads/lecture/");
    }
}
