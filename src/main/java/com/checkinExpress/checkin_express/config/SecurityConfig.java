package com.checkinExpress.checkin_express.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // Desabilita CSRF para testes
                .authorizeHttpRequests()
                .anyRequest().permitAll(); // Permite todas as requisições sem autenticação

        return http.build();
    }
}