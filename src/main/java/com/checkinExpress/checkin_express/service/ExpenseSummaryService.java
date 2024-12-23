package com.checkinExpress.checkin_express.service;

import com.checkinExpress.checkin_express.exception.BookingNotFoundException;
import com.checkinExpress.checkin_express.model.Booking;
import com.checkinExpress.checkin_express.model.Expense;
import com.checkinExpress.checkin_express.model.ExpenseSummary;
import com.checkinExpress.checkin_express.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseSummaryService {

    private final BookingRepository bookingRepository;

    @Autowired
    public ExpenseSummaryService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    /**
     * Método que calcula o total das despesas de uma reserva.
     *
     * @param expenses Lista de despesas a serem somadas.
     * @return O total das despesas.
     */
    public double calculateTotalExpense(List<Expense> expenses) {
        if (expenses == null || expenses.isEmpty()) {
            return 0.0;  // Se a lista for vazia ou null, retornamos 0
        }
        return expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    /**
     * Método que recupera um resumo de despesas de uma reserva com base no ID da reserva.
     *
     * @param bookingId ID da reserva.
     * @return O resumo das despesas (ExpenseSummary).
     * @throws BookingNotFoundException Se a reserva não for encontrada.
     */
    public ExpenseSummary getExpenseSummary(String bookingId) {
        // Buscando a reserva pelo ID
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);

        // Se a reserva não for encontrada, lança uma exceção BookingNotFoundException
        if (!bookingOptional.isPresent()) {
            throw new BookingNotFoundException("Reserva não encontrada com ID: " + bookingId);
        }

        // Recuperando a reserva e calculando o total das despesas
        Booking booking = bookingOptional.get();
        double totalExpenses = calculateTotalExpense(booking.getExpenses());

        // Criando o objeto de resumo de despesas
        ExpenseSummary expenseSummary = new ExpenseSummary(booking.getExpenses(), totalExpenses);

        return expenseSummary;  // Retornando o resumo das despesas
    }
}