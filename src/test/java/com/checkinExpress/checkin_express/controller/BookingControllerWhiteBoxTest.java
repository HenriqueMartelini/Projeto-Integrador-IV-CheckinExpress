package com.checkinExpress.checkin_express.controller;

import com.checkinExpress.checkin_express.model.Booking;
import com.checkinExpress.checkin_express.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingControllerWhiteBoxTest {

    private MockMvc mockMvc;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
    }

    @Test
    void testCreateBooking_ValidData() throws Exception {
        // Simula o comportamento do serviço de criação de reserva
        Booking booking = new Booking("John Doe", "2024-12-01", "2024-12-05");
        when(bookingService.createBooking(any(Booking.class))).thenReturn(booking);

        // Envia a requisição HTTP e verifica se o status é 200
        mockMvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content("{\"name\":\"John Doe\", \"checkInDate\":\"2024-12-01\", \"checkOutDate\":\"2024-12-05\"}"))
                .andExpect(status().isOk());

        // Verifica se o método createBooking do BookingService foi chamado
        verify(bookingService, times(1)).createBooking(any(Booking.class));
    }

    @Test
    void testCreateBooking_InvalidData() throws Exception {
        // Simula o comportamento quando os dados são inválidos
        Booking invalidBooking = new Booking(null, "2024-12-01", null);
        when(bookingService.createBooking(any(Booking.class))).thenThrow(new IllegalArgumentException("Invalid booking data"));

        // Envia a requisição HTTP e verifica se o status é 400 (Bad Request)
        mockMvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content("{\"name\":null, \"checkInDate\":\"2024-12-01\", \"checkOutDate\":null}"))
                .andExpect(status().isBadRequest());

        // Verifica se o método createBooking do BookingService foi chamado
        verify(bookingService, times(1)).createBooking(any(Booking.class));
    }

    @Test
    void testCreateBooking_ServiceThrowsException() throws Exception {
        // Simula o comportamento quando o serviço lança uma exceção (por exemplo, falha interna)
        when(bookingService.createBooking(any(Booking.class))).thenThrow(new RuntimeException("Internal server error"));

        // Envia a requisição HTTP e verifica se o status é 500 (Internal Server Error)
        mockMvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content("{\"name\":\"John Doe\", \"checkInDate\":\"2024-12-01\", \"checkOutDate\":\"2024-12-05\"}"))
                .andExpect(status().isInternalServerError());

        // Verifica se o método createBooking do BookingService foi chamado
        verify(bookingService, times(1)).createBooking(any(Booking.class));
    }
}