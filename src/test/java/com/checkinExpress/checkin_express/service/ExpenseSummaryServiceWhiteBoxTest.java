package com.checkinExpress.checkin_express.service;

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

public class ExpenseSummaryServiceWhiteBoxTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private ExpenseSummaryService expenseSummaryService;

    @BeforeEach
    void setUp() {
        // Inicializa os mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetExpenseSummaryInternalLogic() {
        // Criação dos dados de entrada
        Expense expense1 = new Expense("Food", 100.0);
        Expense expense2 = new Expense("Transport", 50.0);
        Date bookingDate = new Date();
        Booking booking = new Booking("1", "123", "A1", bookingDate, Arrays.asList(expense1, expense2));

        // Mock do comportamento do repository
        when(bookingRepository.findById("1")).thenReturn(Optional.of(booking));

        // Chama o serviço para obter o resumo das despesas
        ExpenseSummary summary = expenseSummaryService.getExpenseSummary("1");

        // Verifica que o método de cálculo de total de despesas no serviço está correto
        assertNotNull(summary);
        assertEquals(150.0, summary.getTotal());
        assertEquals(2, summary.getExpenses().size());

        // Verifica se a lógica interna do método getExpenseSummary foi executada corretamente
        assertEquals("Food", summary.getExpenses().get(0).getDescription());
        assertEquals(100.0, summary.getExpenses().get(0).getAmount());
    }

    @Test
    void testGetExpenseSummaryHandlesEmptyExpenses() {
        // Criação de uma reserva com lista de despesas vazia
        Date bookingDate = new Date();
        Booking booking = new Booking("1", "123", "A1", bookingDate, Arrays.asList());

        // Mock do comportamento do repository
        when(bookingRepository.findById("1")).thenReturn(Optional.of(booking));

        // Chama o serviço para obter o resumo de despesas
        ExpenseSummary summary = expenseSummaryService.getExpenseSummary("1");

        // Verifica que o total é 0 quando não há despesas
        assertNotNull(summary);
        assertEquals(0.0, summary.getTotal());
        assertEquals(0, summary.getExpenses().size());
    }

    @Test
    void testGetExpenseSummaryHandlesBookingNotFound() {
        // Mock do comportamento do repository para não encontrar a reserva
        when(bookingRepository.findById("nonexistent-id")).thenReturn(Optional.empty());

        // Chama o serviço para obter o resumo de despesas com um ID inexistente
        ExpenseSummary summary = expenseSummaryService.getExpenseSummary("nonexistent-id");

        // Verifica se o resultado é nulo ou retorna um valor de erro adequado (dependendo da implementação)
        assertNull(summary);
    }
}