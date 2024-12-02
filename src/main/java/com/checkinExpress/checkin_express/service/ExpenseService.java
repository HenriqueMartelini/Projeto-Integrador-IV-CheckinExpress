package com.checkinExpress.checkin_express.service;

import com.checkinExpress.checkin_express.model.Expense;
import com.checkinExpress.checkin_express.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    // Criar uma nova despesa
    public Expense createExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    // Recuperar todas as despesas de uma reserva
    public List<Expense> getExpensesByBookingId(String bookingId) {
        return expenseRepository.findByBookingId(bookingId);
    }

    // Recuperar uma despesa específica pelo seu ID
    public Optional<Expense> getExpenseById(String expenseId) {
        return expenseRepository.findById(expenseId);
    }

    // Atualizar uma despesa (caso necessário)
    public Expense updateExpense(String expenseId, Expense expenseDetails) {
        // Buscar a despesa
        Optional<Expense> existingExpense = expenseRepository.findById(expenseId);

        if (existingExpense.isPresent()) {
            Expense expense = existingExpense.get();
            expense.setDescription(expenseDetails.getDescription());
            expense.setAmount(expenseDetails.getAmount());
            return expenseRepository.save(expense);  // Atualiza e salva no banco
        }
        return null;  // Retorna null ou pode lançar uma exceção caso a despesa não exista
    }

    // Excluir uma despesa
    public void deleteExpense(String expenseId) {
        expenseRepository.deleteById(expenseId);
    }
}
