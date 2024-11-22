package com.checkinExpress.checkin_express.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Permite todas as rotas
                .allowedOrigins("*")  // Permite todas as origens
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Permite todos os métodos HTTP
                .allowedHeaders("*");  // Permite todos os cabeçalhos
    }
}

