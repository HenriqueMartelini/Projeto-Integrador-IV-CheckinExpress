package com.checkinExpress.checkin_express.controller;

import com.checkinExpress.checkin_express.dto.CheckInRequest;
import com.checkinExpress.checkin_express.dto.CheckOutRequest;
import com.checkinExpress.checkin_express.model.Booking;
import com.checkinExpress.checkin_express.model.ExpenseSummary;
import com.checkinExpress.checkin_express.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // Create a new booking
    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        Booking createdBooking = bookingService.createBooking(booking);
        return ResponseEntity.ok(createdBooking);
    }

    // Get a booking by ID
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBooking(@PathVariable String id) {
        Booking foundBooking = bookingService.getBookingById(id);
        return foundBooking != null ? ResponseEntity.ok(foundBooking) : ResponseEntity.notFound().build();
    }

    // Get all bookings
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    // Delete a booking by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable String id) {
        bookingService.deleteBookingById(id);
        return ResponseEntity.noContent().build();
    }

    // Get expenses for a specific booking
    @GetMapping("/{id}/expenses")
    public ResponseEntity<?> getExpensesByBookingId(@PathVariable String id) {
        ExpenseSummary expenseSummary = bookingService.getExpensesByBookingId(id);
        return expenseSummary != null ? ResponseEntity.ok(expenseSummary) : ResponseEntity.notFound().build();
    }

    // Validate a booking
    @PostMapping("/validate")
    public ResponseEntity<Map<String, String>> validateBooking(@RequestBody Map<String, String> requestData) {
        Map<String, String> response = new HashMap<>();

        try {
            String reservationNumber = requestData.get("reservationNumber");
            String guestName = requestData.get("guestName");

            // Validação dos campos de entrada
            if (reservationNumber == null || reservationNumber.isEmpty() || guestName == null || guestName.isEmpty()) {
                response.put("status", "invalid");
                response.put("message", "Número da reserva ou nome do hóspede não fornecido.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Aqui você pode adicionar validações adicionais (por exemplo, verificar formato de CPF, passaporte ou outros dados)
            // Exemplo (caso o número da reserva seja numérico):
            if (!reservationNumber.matches("\\d+")) {
                response.put("status", "invalid");
                response.put("message", "Número da reserva inválido.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Chama o serviço de validação
            boolean isValid = bookingService.validateBooking(reservationNumber, guestName);

            // Se a reserva for válida
            if (isValid) {
                response.put("status", "valid");
                response.put("message", "Reserva validada com sucesso!");
                return ResponseEntity.ok(response);
            } else {
                // Caso a reserva não seja encontrada
                response.put("status", "invalid");
                response.put("message", "Reserva não encontrada ou dados incorretos.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            // Retorno de erro genérico
            response.put("status", "error");
            response.put("message", "Erro interno do servidor: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Check-in a booking
    @PostMapping("/checkin")
    public ResponseEntity<Booking> checkIn(@RequestBody CheckInRequest checkInRequest) {
        try {
            Booking booking = bookingService.checkInBooking(
                    checkInRequest.getReservationNumber(),
                    checkInRequest.getGuestName(),
                    checkInRequest.getDocumentType(),
                    checkInRequest.getDocumentNumber()
            );
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Check-out a booking
    @PostMapping("/checkout")
    public ResponseEntity<ExpenseSummary> checkOutBooking(@RequestBody CheckOutRequest checkOutRequest) {
        ExpenseSummary expenseSummary = bookingService.checkOutBooking(
                checkOutRequest.getGuestName(),
                checkOutRequest.getReservationNumber()
        );
        return ResponseEntity.ok(expenseSummary);
    }

}