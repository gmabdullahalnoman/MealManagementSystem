package com.mealmanager.MealManagementSystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration for the application
 * Configures CORS, static resources, and other web settings
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configure CORS (Cross-Origin Resource Sharing)
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000", "http://localhost:8080")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    /**
     * Configure static resource handlers
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configure CSS resources
        registry.addResourceHandler("/static/css/**")
                .addResourceLocations("classpath:/static/css/")
                .setCachePeriod(3600);

        // Configure JavaScript resources
        registry.addResourceHandler("/static/js/**")
                .addResourceLocations("classpath:/static/js/")
                .setCachePeriod(3600);

        // Configure image resources
        registry.addResourceHandler("/static/images/**")
                .addResourceLocations("classpath:/static/images/")
                .setCachePeriod(3600);

        // Configure Font Awesome and other libraries
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                .setCachePeriod(3600);
    }
}
