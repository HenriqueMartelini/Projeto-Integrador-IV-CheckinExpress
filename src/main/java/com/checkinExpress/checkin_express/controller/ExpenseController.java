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

    // Endpoint para pegar as despesas de uma reserva específica
    @GetMapping("/{bookingId}")
    public ResponseEntity<List<Expense>> getExpenses(@PathVariable String bookingId) {
        List<Expense> expenses = expenseService.getExpensesByBookingId(bookingId);
        return ResponseEntity.ok(expenses);
    }
}