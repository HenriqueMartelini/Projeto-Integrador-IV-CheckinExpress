package com.checkinExpress.checkin_express.service;

import com.checkinExpress.checkin_express.exception.BookingNotFoundException;
import com.checkinExpress.checkin_express.model.Booking;
import com.checkinExpress.checkin_express.model.Expense;
import com.checkinExpress.checkin_express.model.ExpenseSummary;
import com.checkinExpress.checkin_express.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExpenseSummaryServiceBlackBoxTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private ExpenseSummaryService expenseSummaryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa os mocks
    }

    @Test
    void testGetExpenseSummaryForBooking() {
        // Dados de teste
        Expense expense1 = new Expense("Food", 100.0);
        Expense expense2 = new Expense("Transport", 50.0);
        Date bookingDate = new Date();
        Booking booking = new Booking("1", "123", "A1", bookingDate, Arrays.asList(expense1, expense2));

        // Mock do comportamento do repository
        when(bookingRepository.findById("1")).thenReturn(Optional.of(booking));

        // Chama o serviço para obter o resumo de despesas
        ExpenseSummary summary = expenseSummaryService.getExpenseSummary("1");

        // Verifica se o método findById foi chamado corretamente
        verify(bookingRepository, times(1)).findById("1");

        // Verifica se o cálculo do resumo foi feito corretamente
        assertNotNull(summary);
        assertEquals(150.0, summary.getTotal());
        assertEquals(2, summary.getExpenses().size());
        assertTrue(summary.getExpenses().contains(expense1)); // Verifica se a despesa "Food" está na lista
        assertTrue(summary.getExpenses().contains(expense2)); // Verifica se a despesa "Transport" está na lista
    }

    @Test
    void testGetExpenseSummary_BookingNotFound() {
        // Mocka a ausência de uma reserva
        when(bookingRepository.findById("1")).thenReturn(Optional.empty());

        // Chama o serviço e verifica se lança a exceção apropriada
        assertThrows(BookingNotFoundException.class, () -> {
            expenseSummaryService.getExpenseSummary("1");
        });
    }

    @Test
    void testCalculateTotalExpense() {
        // Verifica o método calculateTotalExpense isoladamente
        Expense expense1 = new Expense("Food", 100.0);
        Expense expense2 = new Expense("Transport", 50.0);

        double totalExpense = expenseSummaryService.calculateTotalExpense(Arrays.asList(expense1, expense2));

        assertEquals(150.0, totalExpense); // Verifica o cálculo do total de despesas
    }
}