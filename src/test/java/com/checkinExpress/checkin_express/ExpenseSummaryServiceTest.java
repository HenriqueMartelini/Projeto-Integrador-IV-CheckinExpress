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
        // Criar o ExpenseSummary passando a lista de despesas diretamente no construtor
        ExpenseSummary expenseSummary = new ExpenseSummary(Collections.singletonList(expense), 100.0);  // Passando as despesas e total

        // Calcular o total
        double total = expenseSummaryService.calculateTotalExpense(expenseSummary);

        // Verificar se o total é 100.0
        assertEquals(100.0, total);
    }

    @Test
    public void testGetExpenseSummary() {
        // Criar a reserva com uma lista de despesas
        Booking booking = new Booking();
        booking.setId("1");
        booking.setExpenses(Collections.singletonList(expense));

        // Simular a busca no repositório
        Mockito.when(bookingRepository.findById("1")).thenReturn(java.util.Optional.of(booking));

        // Chamar o método de serviço para obter o ExpenseSummary
        ExpenseSummary result = expenseSummaryService.getExpenseSummary("1");

        // Verificar se o resultado não é nulo e o total das despesas
        assertNotNull(result);
        assertEquals(100.0, result.getTotal());
    }
}