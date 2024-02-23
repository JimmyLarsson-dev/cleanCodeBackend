package com.example.cleancode.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
       CorsConfiguration configuration = new CorsConfiguration();
        // Allows the sharing of credentials in cross-origin requests
        configuration.setAllowCredentials(true);
        // Specifies the list of allowed origins
        configuration.setAllowedOrigins(List.of("https://localhost:5173", "http://localhost:5173"));
        // Allows any HTTP method in cross-origin requests.
        configuration.addAllowedMethod("*");
        // Allows any header to be sent in cross-origin requests.
        configuration.addAllowedHeader("*");
        // Creates a URL-based configuration source for CORS.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Registers the CORS configuration for paths under "/api/".
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
   }

}
