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
    void testCreateBooking_ValidBooking() {
        // Criação de uma booking válida com todos os campos necessários
        Date bookingDate = new Date();
        Booking booking = new Booking("1", "123", "A1", bookingDate, Arrays.asList(new Expense("Food", 100.0), new Expense("Transport", 50.0)));

        // Mock do comportamento do repository
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        // Chama o método de criação
        Booking result = bookingService.createBooking(booking);

        // Verifica se o método save do repository foi chamado
        verify(bookingRepository, times(1)).save(booking);

        // Assegura que o retorno é o esperado
        assertNotNull(result);
        assertEquals("123", result.getGuestId());
        assertEquals("A1", result.getRoomId());
        assertEquals(bookingDate, result.getBookingDate());
        assertEquals(2, result.getExpenses().size());
    }

    @Test
    void testCreateBooking_NullBooking() {
        // Testa a exceção lançada quando a reserva é nula
        assertThrows(IllegalArgumentException.class, () -> {
            bookingService.createBooking(null);
        });
    }

    @Test
    void testGetBookingById_ValidId() {
        // Criação de uma reserva de exemplo
        Date bookingDate = new Date();
        Booking booking = new Booking("1", "123", "A1", bookingDate, Arrays.asList(new Expense("Food", 100.0), new Expense("Transport", 50.0)));

        // Mock do comportamento do repository
        when(bookingRepository.findById("1")).thenReturn(Optional.of(booking));

        // Chama o método de buscar a reserva por ID
        Booking result = bookingService.getBookingById("1");

        // Verifica se o método findById foi chamado
        verify(bookingRepository, times(1)).findById("1");

        // Assegura que o retorno é o esperado
        assertNotNull(result);
        assertEquals("123", result.getGuestId());
        assertEquals("A1", result.getRoomId());
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
    void testDeleteBookingById_ValidId() {
        // Mocka que a reserva existe
        when(bookingRepository.existsById("1")).thenReturn(true);

        // Chama o método para deletar
        bookingService.deleteBookingById("1");

        // Verifica se o método deleteById foi chamado
        verify(bookingRepository, times(1)).deleteById("1");
    }

    @Test
    void testDeleteBookingById_BookingNotFound() {
        // Mocka a ausência de uma reserva
        when(bookingRepository.existsById("1")).thenReturn(false);

        // Chama o método e espera que lance uma BookingNotFoundException
        assertThrows(BookingNotFoundException.class, () -> {
            bookingService.deleteBookingById("1");
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