package com.checkinExpress.checkin_express.service;

import com.checkinExpress.checkin_express.model.Booking;
import com.checkinExpress.checkin_express.model.Expense;
import com.checkinExpress.checkin_express.model.ExpenseSummary;
import com.checkinExpress.checkin_express.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * @param expenseSummary Objeto contendo a lista de despesas a serem somadas.
     * @return O total das despesas.
     */
    public double calculateTotalExpense(ExpenseSummary expenseSummary) {
        return expenseSummary.getExpenses().stream()
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
            // Passando a lista de despesas e o total para o construtor do ExpenseSummary
            ExpenseSummary expenseSummary = new ExpenseSummary(booking.getExpenses(), calculateTotalExpense(new ExpenseSummary(booking.getExpenses(), 0.0)));

            return expenseSummary;
        }

        return null;  // Caso a reserva não seja encontrada
    }
}