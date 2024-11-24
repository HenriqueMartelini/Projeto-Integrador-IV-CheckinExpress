package com.checkinExpress.checkin_express.service;

import com.checkinExpress.checkin_express.model.Expense;
import com.checkinExpress.checkin_express.model.ExpenseSummary;
import com.checkinExpress.checkin_express.repository.ExpenseRepository;
import com.checkinExpress.checkin_express.exception.BookingNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseSummaryService {

    private final ExpenseRepository expenseRepository;

    @Autowired
    public ExpenseSummaryService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public ExpenseSummary getExpenseSummaryById(String bookingId) {
        // Consultar a lista de despesas
        List<Expense> expenses = expenseRepository.findByBookingId(bookingId);

        if (expenses == null || expenses.isEmpty()) {
            throw new BookingNotFoundException("Não foram encontradas despesas para a reserva com ID: " + bookingId);
        }

        // Calcular o total das despesas
        double total = expenses.stream().mapToDouble(Expense::getAmount).sum();

        // Criar e retornar o resumo de despesas
        return new ExpenseSummary(expenses, total);
    }

}
