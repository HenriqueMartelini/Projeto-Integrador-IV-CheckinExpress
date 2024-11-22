package com.checkinExpress.checkin_express.controller;

import com.checkinExpress.checkin_express.model.ExpenseSummary;
import com.checkinExpress.checkin_express.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExpenseSummaryController.class)
public class ExpenseSummaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    private ExpenseSummary mockExpenseSummary;

    @BeforeEach
    public void setUp() {
        mockExpenseSummary = new ExpenseSummary();
        mockExpenseSummary.setTotal(150.0); // Configura um valor fictício
    }

    @Test
    public void testGetExpenseSummarySuccess() throws Exception {
        when(bookingService.getExpensesByBookingId(anyString())).thenReturn(mockExpenseSummary);

        mockMvc.perform(get("/expenses/12345")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(150.0));
    }

    @Test
    public void testGetExpenseSummaryNotFound() throws Exception {
        when(bookingService.getExpensesByBookingId(anyString()))
                .thenThrow(new IllegalArgumentException("Booking not found"));

        mockMvc.perform(get("/expenses/12345")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}