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
        return expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    /**
     * Método que recupera um resumo de despesas de uma reserva com base no ID da reserva.
     *
     * @param bookingId ID da reserva.
     * @return O resumo das despesas (ExpenseSummary).
     */
    public ExpenseSummary getExpenseSummary(String bookingId) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);

        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();

            // Calculando o total das despesas
            double totalExpenses = calculateTotalExpense(booking.getExpenses());

            // Criando o objeto de resumo de despesas
            ExpenseSummary expenseSummary = new ExpenseSummary(booking.getExpenses(), totalExpenses);

            // Retornando o resumo das despesas
            return expenseSummary;
        }

        // Caso a reserva não seja encontrada, lançar uma exceção ou retornar um erro adequado
        throw new BookingNotFoundException("Reserva não encontrada com ID: " + bookingId);
    }
}