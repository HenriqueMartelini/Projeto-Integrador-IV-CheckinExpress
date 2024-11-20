package com.checkinExpress.checkin_express;

import com.checkinExpress.checkin_express.model.Expense;
import com.checkinExpress.checkin_express.model.ExpenseSummary;
import com.checkinExpress.checkin_express.model.Booking;
import com.checkinExpress.checkin_express.repository.BookingRepository;
import com.checkinExpress.checkin_express.service.ExpenseSummaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Collections;

public class ExpenseSummaryServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private ExpenseSummaryService expenseSummaryService;

    private Expense expense;

    @BeforeEach
    public void setUp() {
        expense = new Expense("Café", 100.0);
    }

    @Test
    public void testCalculateTotalExpense() {
        ExpenseSummary expenseSummary = new ExpenseSummary(Collections.singletonList(expense), 100.0);  // Passando as despesas e total

        double total = expenseSummaryService.calculateTotalExpense(expenseSummary);

        assertEquals(100.0, total);
    }

    @Test
    public void testGetExpenseSummary() {
        Booking booking = new Booking();
        booking.setId("1");
        booking.setExpenses(Collections.singletonList(expense));

        Mockito.when(bookingRepository.findById("1")).thenReturn(java.util.Optional.of(booking));

        ExpenseSummary result = expenseSummaryService.getExpenseSummary("1");

        assertNotNull(result);
        assertEquals(100.0, result.getTotal());
    }
}