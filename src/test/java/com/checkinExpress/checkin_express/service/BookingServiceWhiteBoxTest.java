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

public class BookingServiceWhiteBoxTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa os mocks
    }

    @Test
    void testGetBookingById_ValidId() {
        // Criação de uma reserva de exemplo
        Date bookingDate = new Date();
        Booking booking = new Booking("booking123", "123456", "room123", bookingDate, Arrays.asList(new Expense("Minibar", 50), new Expense("Room Service", 100), new Expense("Lavanderia", 60)));

        // Mock do comportamento do repository
        when(bookingRepository.findById("booking123")).thenReturn(Optional.of(booking));

        // Chama o método de buscar a reserva por ID
        Booking result = bookingService.getBookingById("booking123");

        // Verifica se o método findById foi chamado
        verify(bookingRepository, times(1)).findById("booking123");

        // Assegura que o retorno é o esperado
        assertNotNull(result);
        assertEquals("123456", result.getGuestId());
        assertEquals("room123", result.getRoomId());
        assertEquals(bookingDate, result.getBookingDate());
    }

    @Test
    void testGetBookingById_BookingNotFound() {
        // Mocka a ausência de uma reserva
        when(bookingRepository.findById("1")).thenReturn(Optional.empty());

        // Chama o método e espera que lance uma BookingNotFoundException
        assertThrows(BookingNotFoundException.class, () -> {
            bookingService.getBookingById("1");
        });
    }

    @Test
    void testGetExpensesByBookingId() {
        // Criação de uma reserva com despesas associadas
        Expense expense1 = new Expense("Food", 100.0);
        Expense expense2 = new Expense("Transport", 50.0);
        Date bookingDate = new Date();
        Booking booking = new Booking("1", "123", "A1", bookingDate, Arrays.asList(expense1, expense2));

        // Mock do comportamento do repository
        when(bookingRepository.findById("1")).thenReturn(Optional.of(booking));

        // Chama o método para obter o resumo das despesas
        ExpenseSummary result = bookingService.getExpensesByBookingId("1");

        // Verifica se o método findById foi chamado
        verify(bookingRepository, times(1)).findById("1");

        // Assegura que o total de despesas é calculado corretamente
        assertNotNull(result);
        assertEquals(150.0, result.getTotal());
        assertEquals(2, result.getExpenses().size());
    }
}