package com.checkinExpress.checkin_express.controller;

import com.checkinExpress.checkin_express.model.ExpenseSummary;
import com.checkinExpress.checkin_express.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expenses")
public class ExpenseSummaryController {

    private final BookingService bookingService;

    @Autowired
    public ExpenseSummaryController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Endpoint para obter o resumo de despesas de uma reserva.
     *
     * @param bookingId ID da reserva.
     * @return Resumo das despesas.
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<ExpenseSummary> getExpenseSummary(@PathVariable String bookingId) {
        try {
            ExpenseSummary expenseSummary = bookingService.getExpensesByBookingId(bookingId);
            return ResponseEntity.ok(expenseSummary);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}