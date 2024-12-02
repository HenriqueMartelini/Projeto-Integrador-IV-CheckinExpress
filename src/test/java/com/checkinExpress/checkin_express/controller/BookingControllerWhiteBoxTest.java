package com.checkinExpress.checkin_express.controller;

import com.checkinExpress.checkin_express.model.Booking;
import com.checkinExpress.checkin_express.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class BookingControllerWhiteBoxTest {

    private MockMvc mockMvc;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        // Inicializa os mocks e configura o MockMvc
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
    }

    @Test
    void testGetBooking_ValidId() throws Exception {
        // Simula o comportamento do serviço ao buscar uma reserva existente
        Booking booking = new Booking("Alice Smith", "2024-12-01T21:28:59.737Z", "2025-12-15T00:00:00.000Z");
        when(bookingService.getBookingById("booking123")).thenReturn(booking);

        // Envia a requisição GET e verifica se o status é 200 (OK)
        mockMvc.perform(get("/bookings/booking123")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"name\":\"Alice Smith\", \"checkInDate\":\"2024-12-01T21:28:59.737Z\", \"checkOutDate\":\"2025-12-15T00:00:00.000Z\"}"));

        verify(bookingService, times(1)).getBookingById("booking123");
    }

    @Test
    void testGetBooking_NotFound() throws Exception {
        // Simula o comportamento quando a reserva não é encontrada
        when(bookingService.getBookingById("booking123")).thenReturn(null);

        // Envia a requisição GET e verifica se o status é 404 (Not Found)
        mockMvc.perform(get("/bookings/1")
                        .contentType("application/json"))
                .andExpect(status().isNotFound());

        verify(bookingService, times(1)).getBookingById("booking123");
    }

    @Test
    void testGetBooking_ServiceThrowsException() throws Exception {
        // Simula o comportamento quando o serviço lança uma exceção (erro interno)
        when(bookingService.getBookingById("booking123")).thenThrow(new RuntimeException("Internal server error"));

        // Envia a requisição GET e verifica se o status é 500 (Internal Server Error)
        mockMvc.perform(get("/bookings/1")
                        .contentType("application/json"))
                .andExpect(status().isInternalServerError());

        // Verifica se o método getBookingById foi chamado uma vez com o ID 1
        verify(bookingService, times(1)).getBookingById("booking123");
    }
}