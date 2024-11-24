package com.checkinExpress.checkin_express.controller;

import com.checkinExpress.checkin_express.dto.CheckInRequest;
import com.checkinExpress.checkin_express.exception.BookingNotFoundException;
import com.checkinExpress.checkin_express.exception.GuestNotFoundException;
import com.checkinExpress.checkin_express.model.Booking;
import com.checkinExpress.checkin_express.model.ExpenseSummary;
import com.checkinExpress.checkin_express.repository.GuestRepository;
import com.checkinExpress.checkin_express.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private GuestRepository guestRepository;

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
            // Lógica de check-in, onde a data de check-in será definida no backend
            Booking booking = bookingService.checkInBooking(
                    checkInRequest.getReservationNumber(),
                    checkInRequest.getGuestName(),
                    checkInRequest.getDocumentType(),
                    checkInRequest.getDocumentNumber()
            );

            // Retorna a reserva com status 200 (OK) se o check-in for bem-sucedido
            return ResponseEntity.ok(booking);

        } catch (BookingNotFoundException e) {
            // Retorna 404 se a reserva não for encontrada
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (GuestNotFoundException e) {
            // Retorna 404 se o hóspede não for encontrado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IllegalArgumentException e) {
            // Retorna 400 se houver um erro de argumento inválido (ex.: dados inválidos de documento)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            // Captura quaisquer outras exceções inesperadas e retorna 500 (Erro interno do servidor)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    // Get booking details
    @GetMapping("/details/{reservationNumber}")
    public ResponseEntity<Map<String, Object>> getBookingDetails(@PathVariable String reservationNumber) {
        System.out.println("Buscando reserva com o número: " + reservationNumber);
        Optional<Booking> booking = bookingService.getBookingDetailsByReservationNumber(reservationNumber);

        if (booking.isPresent()) {
            Booking b = booking.get();
            Map<String, Object> response = new HashMap<>();
            response.put("guestName", b.getGuestName());
            response.put("roomId", b.getRoomId());
            response.put("checkInDate", b.getCheckInDate());
            response.put("checkOutDate", b.getCheckOutDate());
            response.put("reservationNumber", b.getReservationNumber());
            response.put("dailyValue", b.getDailyValue());
            response.put("totalAmount", b.getTotalDays() * b.getDailyValue());
            response.put("breakfast", b.isBreakfastIncluded() ? "Incluído" : "Não incluído");
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(404).body(Map.of("message", "Booking not found"));
    }


    // Get expenses for a specific booking
    @GetMapping("/{id}/expenses")
    public ResponseEntity<ExpenseSummary> getExpensesByBookingId(@PathVariable String id) {
        try {
            ExpenseSummary expenseSummary = bookingService.getExpensesByBookingId(id);
            return ResponseEntity.ok(expenseSummary);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    // Check-out a booking and calculate expenses
    @PostMapping("/{id}/checkout")
    public ResponseEntity<ExpenseSummary> checkOutBooking(@PathVariable String id) {
        try {
            ExpenseSummary expenseSummary = bookingService.checkOutBooking(id);
            return ResponseEntity.ok(expenseSummary);
        } catch (IllegalStateException e) {
            // Retorna 400 se o hóspede não fez o check-in
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (BookingNotFoundException e) {
            // Retorna 404 se a reserva não for encontrada
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            // Retorna 500 para erros internos inesperados
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
