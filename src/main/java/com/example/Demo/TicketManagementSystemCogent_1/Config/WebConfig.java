package com.example.Demo.TicketManagementSystemCogent_1.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Apply CORS settings to all endpoints
        .allowedOrigins(
        		"http://localhost:3000", "http://192.168.1.102:3000","https://cogentmobileapp.in:8443","https://45.115.186.228:8443"
        	)
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // Allowed HTTP methods
            .allowedHeaders("Authorization", "Content-Type", "Accept") // Allowed headers (added Accept header)
            .allowCredentials(true); // Allow credentials (cookies, tokens)
    }
}
