package com.checkinExpress.checkin_express;

import com.checkinExpress.checkin_express.controller.BookingController;
import com.checkinExpress.checkin_express.model.Booking;
import com.checkinExpress.checkin_express.model.Expense;
import com.checkinExpress.checkin_express.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

@SpringBootTest
@AutoConfigureMockMvc
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private Booking booking;

    @BeforeEach
    public void setUp() {
        Expense expense = new Expense("Café", 100);
        expense.setAmount(100.0);
        expense.setDescription("Café");

        booking = new Booking();
        booking.setId("1");
        booking.setExpenses(Collections.singletonList(expense));
    }

    @Test
    public void testCreateBooking() throws Exception {
        // Mocking the behavior of bookingService
        when(bookingService.createBooking(any(Booking.class))).thenReturn(booking);

        // Performing the POST request and validating the response
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"1\", \"expenses\":[{\"amount\":100.0, \"description\":\"Café\"}]}"))
                .andExpect(status().isOk()) // Verifying the status code
                .andExpect(jsonPath("$.id").value("1")) // Verifying the booking ID
                .andExpect(jsonPath("$.expenses[0].amount").value(100.0)); // Verifying the expense amount
    }

    @Test
    public void testGetBookingById_Success() throws Exception {
        // Mocking the behavior of bookingService
        when(bookingService.getBookingById("1")).thenReturn(booking);

        // Performing the GET request and validating the response
        mockMvc.perform(get("/api/bookings/1"))
                .andExpect(status().isOk()) // Verifying the status code
                .andExpect(jsonPath("$.id").value("1")) // Verifying the booking ID
                .andExpect(jsonPath("$.expenses[0].amount").value(100.0)); // Verifying the expense amount
    }

    @Test
    public void testGetBookingById_NotFound() throws Exception {
        // Mocking the behavior of bookingService to return null (not found)
        when(bookingService.getBookingById("2")).thenReturn(null);

        // Performing the GET request and verifying a 404 Not Found response
        mockMvc.perform(get("/api/bookings/2"))
                .andExpect(status().isNotFound()); // Verifying the 404 status code
    }

    @Test
    public void testGetAllBookings() throws Exception {
        // Mocking the behavior of bookingService
        when(bookingService.getAllBookings()).thenReturn(Collections.singletonList(booking));

        // Performing the GET request and verifying the response
        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk()) // Verifying the status code
                .andExpect(jsonPath("$[0].id").value("1")) // Verifying the booking ID in the list
                .andExpect(jsonPath("$[0].expenses[0].amount").value(100.0)); // Verifying the expense amount in the list
    }

    @Test
    public void testDeleteBooking_Success() throws Exception {
        // Mocking the behavior of bookingService
        doNothing().when(bookingService).deleteBookingById("1");

        // Performing the DELETE request and verifying the response
        mockMvc.perform(delete("/api/bookings/1"))
                .andExpect(status().isNoContent()); // Verifying the status code 204 No Content
    }

    @Test
    public void testDeleteBooking_NotFound() throws Exception {
        // Mocking the behavior of bookingService
        doThrow(new RuntimeException("Booking not found")).when(bookingService).deleteBookingById("2");

        // Performing the DELETE request and verifying the response
        mockMvc.perform(delete("/api/bookings/2"))
                .andExpect(status().isNotFound()); // Verifying the status code 404 Not Found
    }
}