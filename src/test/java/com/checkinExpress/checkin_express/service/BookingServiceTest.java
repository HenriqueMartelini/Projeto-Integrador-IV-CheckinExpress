package com.checkinExpress.checkin_express.service;

import com.checkinExpress.checkin_express.model.Booking;
import com.checkinExpress.checkin_express.model.Expense;
import com.checkinExpress.checkin_express.model.ExpenseSummary;
import com.checkinExpress.checkin_express.repository.BookingRepository;
import com.checkinExpress.checkin_express.exception.BookingNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    private Booking booking;
    private Expense expense;

    @BeforeEach
    public void setUp() {
        expense = new Expense("Café", 100);
        expense.setAmount(100.0);
        expense.setDescription("Café");

        booking = new Booking();
        booking.setId("1");
        booking.setExpenses(Collections.singletonList(expense));
    }

    @Test
    public void testGetBookingById_Success() {
        Mockito.when(bookingRepository.findById("1")).thenReturn(Optional.of(booking));

        Booking foundBooking = bookingService.getBookingById("1");

        assertNotNull(foundBooking);
        assertEquals("1", foundBooking.getId());
    }

    @Test
    public void testGetBookingById_NotFound() {
        Mockito.when(bookingRepository.findById("2")).thenReturn(Optional.empty());

        BookingNotFoundException thrown = assertThrows(BookingNotFoundException.class, () -> {
            bookingService.getBookingById("2");
        });

        assertEquals("Booking not found with ID: 2", thrown.getMessage());
    }

    @Test
    public void testGetExpensesByBookingId() {
        Mockito.when(bookingRepository.findById("1")).thenReturn(Optional.of(booking));

        ExpenseSummary expenseSummary = bookingService.getExpensesByBookingId("1");

        assertNotNull(expenseSummary);
        assertEquals(100.0, expenseSummary.getTotal());
        assertEquals(1, expenseSummary.getExpenses().size());
    }
}
