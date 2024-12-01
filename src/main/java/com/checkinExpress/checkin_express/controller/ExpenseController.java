package com.checkinExpress.checkin_express.controller;

import com.checkinExpress.checkin_express.model.Expense;
import com.checkinExpress.checkin_express.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

        private final ExpenseService expenseService;

        @Autowired
        public ExpenseController(ExpenseService expenseService) {
            this.expenseService = expenseService;
        }

        // Endpoint para adicionar uma despesa
        @PostMapping
        public ResponseEntity<Expense> addExpense(@RequestBody Expense expense) {
            Expense newExpense = expenseService.createExpense(expense);
            return ResponseEntity.status(HttpStatus.CREATED).body(newExpense);
        }

        // Endpoint para pegar as despesas de uma reserva específica
        @GetMapping("/{bookingId}")
        public ResponseEntity<List<Expense>> getExpenses(@PathVariable String bookingId) {
            List<Expense> expenses = expenseService.getExpensesByBookingId(bookingId);
            return ResponseEntity.ok(expenses);
        }

        // Endpoint para excluir uma despesa
        @DeleteMapping("/{expenseId}")
        public ResponseEntity<Void> deleteExpense(@PathVariable String expenseId) {
            expenseService.deleteExpense(expenseId);
            return ResponseEntity.noContent().build();
        }

        // Endpoint para atualizar uma despesa
        @PutMapping("/{expenseId}")
        public ResponseEntity<Expense> updateExpense(
                @PathVariable String expenseId, @RequestBody Expense expenseDetails) {
            Expense updatedExpense = expenseService.updateExpense(expenseId, expenseDetails);
            if (updatedExpense != null) {
                return ResponseEntity.ok(updatedExpense);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }