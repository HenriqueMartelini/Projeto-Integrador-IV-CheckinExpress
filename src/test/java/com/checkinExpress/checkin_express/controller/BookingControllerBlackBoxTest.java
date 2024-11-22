package com.checkinExpress.checkin_express.controller;

import com.checkinExpress.checkin_express.model.Booking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BookingControllerBlackBoxTest {

    private RestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:8080/bookings";

    @BeforeEach
    void setUp() {
        // Inicialização de qualquer configuração, se necessário
    }

    @Test
    void testCreateBooking_ValidData() {
        // Simulação de uma requisição com dados válidos
        Booking booking = new Booking("John Doe", "2024-12-01", "2024-12-05");
        ResponseEntity<String> response = restTemplate.postForEntity(
                BASE_URL,
                booking,
                String.class
        );
        // Verifica se a criação foi bem-sucedida (status HTTP 200)
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testCreateBooking_InvalidData() {
        // Simulação de uma requisição com dados inválidos
        Booking invalidBooking = new Booking(null, "2024-12-01", null);
        ResponseEntity<String> response = restTemplate.postForEntity(
                BASE_URL,
                invalidBooking,
                String.class
        );
        // Verifica se a resposta é 400 (Bad Request) para dados inválidos
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testCreateBooking_WithoutAuthentication() {
        // Simular falha de autenticação (ajustar conforme seu sistema de segurança)
        // No caso de não haver autenticação, a resposta esperada pode ser 403 (Forbidden)
        Booking booking = new Booking("John Doe", "2024-12-01", "2024-12-05");
        ResponseEntity<String> response = restTemplate.postForEntity(
                BASE_URL,
                booking,
                String.class
        );
        // Verifica se o acesso é negado devido à falta de autenticação
        assertEquals(403, response.getStatusCodeValue());
    }
}