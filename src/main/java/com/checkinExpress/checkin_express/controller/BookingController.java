package com.checkinExpress.checkin_express.controller;

import com.checkinExpress.checkin_express.model.Booking;
import com.checkinExpress.checkin_express.model.ExpenseSummary;
import com.checkinExpress.checkin_express.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        Booking createdBooking = bookingService.createBooking(booking);
        return ResponseEntity.ok(createdBooking);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable String id) {
        Booking booking = bookingService.getBookingById(id);
        return booking != null ? ResponseEntity.ok(booking) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookingById(@PathVariable String id) {
        bookingService.deleteBookingById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/expenses")
    public ResponseEntity<ExpenseSummary> getExpensesByBookingId(@PathVariable String id) {
        ExpenseSummary expenseSummary = bookingService.getExpensesByBookingId(id);
        return expenseSummary != null ? ResponseEntity.ok(expenseSummary) : ResponseEntity.notFound().build();
    }
}