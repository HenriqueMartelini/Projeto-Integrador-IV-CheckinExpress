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

    @Autowired
    private BookingService bookingService;

    // Create a new booking
    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        Booking createdBooking = bookingService.createBooking(booking); // Usando o método correto
        return ResponseEntity.ok(createdBooking);
    }

    // Get a booking by ID
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBooking(@PathVariable String id) {
        Booking foundBooking = bookingService.getBookingById(id); // Usando o método correto
        return foundBooking != null ? ResponseEntity.ok(foundBooking) : ResponseEntity.notFound().build();
    }

    // Get all bookings
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings(); // Usando o método correto
        return ResponseEntity.ok(bookings);
    }

    // Delete a booking by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable String id) {
        bookingService.deleteBookingById(id); // Método já não retorna boolean, portanto, removido o check de retorno
        return ResponseEntity.noContent().build();
    }

    // Get expenses for a specific booking
    @GetMapping("/{id}/expenses")
    public ResponseEntity<?> getExpensesByBookingId(@PathVariable String id) {
        ExpenseSummary expenseSummary = bookingService.getExpensesByBookingId(id);
        return expenseSummary != null ? ResponseEntity.ok(expenseSummary) : ResponseEntity.notFound().build();
    }
}