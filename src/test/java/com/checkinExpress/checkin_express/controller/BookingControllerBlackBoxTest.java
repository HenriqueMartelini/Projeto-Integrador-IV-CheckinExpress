package com.checkinExpress.checkin_express.controller;

import com.checkinExpress.checkin_express.model.Booking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookingControllerBlackBoxTest {

    private RestTemplate restTemplate;
    private static final String BASE_URL = "http://localhost:8080/api/bookings";

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
    }

    @Test
    void testGetBookingById_ValidId() {
        String validId = "booking123";
        ResponseEntity<Booking> response = restTemplate.getForEntity(
                BASE_URL + "/" + validId,
                Booking.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(validId, response.getBody().getId());
    }

    @Test
    void testGetBookingById_InvalidId() {
        String invalidId = "999";
        ResponseEntity<String> response = restTemplate.getForEntity(
                BASE_URL + "/" + invalidId,
                String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetAllBookings() {
        ResponseEntity<Booking[]> response = restTemplate.getForEntity(
                BASE_URL,
                Booking[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
    }

    @Test
    void testValidateBooking_ValidData() {
        String url = BASE_URL + "/validate";
        Map<String, String> requestData = new HashMap<>();
        requestData.put("reservationNumber", "123456");
        requestData.put("guestName", "Alice Smith");

        ResponseEntity<Map> response = restTemplate.postForEntity(
                url,
                requestData,
                Map.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("valid", response.getBody().get("status"));
    }

    @Test
    void testValidateBooking_InvalidData() {
        String url = BASE_URL + "/validate";
        Map<String, String> requestData = new HashMap<>();
        requestData.put("reservationNumber", "");
        requestData.put("guestName", "");

        ResponseEntity<Map> response = restTemplate.postForEntity(
                url,
                requestData,
                Map.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("invalid", response.getBody().get("status"));
    }

    @Test
    void testCheckIn_ValidData() {
        String url = BASE_URL + "/checkin";
        Map<String, String> checkInRequest = new HashMap<>();
        checkInRequest.put("reservationNumber", "123456");
        checkInRequest.put("guestName", "Alice Smith");
        checkInRequest.put("documentType", "CPF");
        checkInRequest.put("documentNumber", "12345678900");

        ResponseEntity<Booking> response = restTemplate.postForEntity(
                url,
                checkInRequest,
                Booking.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testCheckIn_InvalidData() {
        String url = BASE_URL + "/checkin";
        Map<String, String> checkInRequest = new HashMap<>();
        checkInRequest.put("reservationNumber", "87889237");
        checkInRequest.put("guestName", "Filip Adam");
        checkInRequest.put("documentType", "ID");
        checkInRequest.put("documentNumber", "8787878787");

        ResponseEntity<String> response = restTemplate.postForEntity(
                url,
                checkInRequest,
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetBookingDetails_ValidReservationNumber() {
        String reservationNumber = "123456"; // Substituir pelo número de reserva válido
        ResponseEntity<Map> response = restTemplate.getForEntity(
                BASE_URL + "/details/" + reservationNumber,
                Map.class
        );

        // Verifica se os detalhes da reserva são retornados corretamente
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(reservationNumber, response.getBody().get("reservationNumber"));
    }

    @Test
    void testGetBookingDetails_InvalidReservationNumber() {
        String reservationNumber = "99999"; // Substituir por um número inexistente
        ResponseEntity<Map> response = restTemplate.getForEntity(
                BASE_URL + "/details/" + reservationNumber,
                Map.class
        );

        // Verifica se retorna 404 para um número de reserva inválido
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}