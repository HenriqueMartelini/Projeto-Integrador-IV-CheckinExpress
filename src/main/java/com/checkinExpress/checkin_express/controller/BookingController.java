package com.checkinExpress.checkin_express.controller;

import com.checkinExpress.checkin_express.dto.CheckInRequest;
import com.checkinExpress.checkin_express.model.*;
import com.checkinExpress.checkin_express.repository.GuestRepository;
import com.checkinExpress.checkin_express.repository.RoomRepository;
import com.checkinExpress.checkin_express.service.BookingService;
import com.checkinExpress.checkin_express.service.GuestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private GuestService guestService;

    // Cria um novo booking
    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        Booking createdBooking = bookingService.createBooking(booking);
        return ResponseEntity.ok(createdBooking);
    }

    // Pega um booking pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBooking(@PathVariable String id) {
        Booking foundBooking = bookingService.getBookingById(id);
        return foundBooking != null ? ResponseEntity.ok(foundBooking) : ResponseEntity.notFound().build();
    }

    // Pega todos os bookings
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    // Validar um booking
    @PostMapping("/validate")
    public ResponseEntity<Map<String, String>> validateBooking(@RequestBody Map<String, String> requestData) {
        Map<String, String> response = new HashMap<>();

        try {
            String reservationNumber = requestData.get("reservationNumber");
            String guestName = requestData.get("guestName");

            if (reservationNumber == null || reservationNumber.isEmpty() || guestName == null || guestName.isEmpty()) {
                response.put("status", "invalid");
                response.put("message", "Número da reserva ou nome do hóspede não fornecido.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Verifica se o número da reserva são dígitos numéricos
            if (!reservationNumber.matches("\\d+")) {
                response.put("status", "invalid");
                response.put("message", "Número da reserva inválido.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            boolean isValid = bookingService.validateBooking(reservationNumber, guestName);

            if (isValid) {
                response.put("status", "valid");
                response.put("message", "Reserva validada com sucesso!");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "invalid");
                response.put("message", "Reserva não encontrada ou dados incorretos.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Erro interno do servidor: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Realiza o checkin de um booking
    @PostMapping("/checkin")
    public ResponseEntity<Booking> checkIn(@Valid @RequestBody CheckInRequest checkInRequest) {
        try {
            Booking booking = bookingService.checkInBooking(
                    checkInRequest.getReservationNumber(),
                    checkInRequest.getGuestName(),
                    checkInRequest.getDocumentType(),
                    checkInRequest.getDocumentNumber()
            );
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Pega os detalhes do booking
    @GetMapping("/details/{reservationNumber}")
    public ResponseEntity<Map<String, Object>> getBookingDetails(@PathVariable String reservationNumber) {
        System.out.println("Buscando reserva com o número: " + reservationNumber);
        Optional<Booking> booking = bookingService.getBookingDetailsByReservationNumber(reservationNumber);

        if (booking.isPresent()) {
            Booking b = booking.get();

            // Buscar o hóspede relacionado
            Optional<Guest> guest = guestService.getGuestById(b.getGuestId());

            System.out.println("Buscando quarto com ID: " + b.getRoomId());

            // Buscar o quarto relacionado pelo ID
            Optional<Room> room = roomRepository.findById(b.getRoomId());

            if (room.isEmpty()) {
                System.out.println("Nenhum quarto encontrado com o ID: " + b.getRoomId());
            }

            if (guest.isPresent()) {
                Guest g = guest.get();
                Map<String, Object> response = new HashMap<>();

                // Formatar datas
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String checkInDate = dateFormat.format(b.getCheckInDate());
                String checkOutDate = dateFormat.format(b.getCheckOutDate());

                // Recuperando o total de dias diretamente do banco
                long totalDays = b.getTotalDays();

                // Calcular o total com as despesas
                double totalAmount = totalDays * b.getDailyValue();

                // Adicionar os dados ao response
                response.put("guestName", g.getName());
                response.put("documentType", g.getDocumentType());
                response.put("documentNumber", g.getDocumentNumber());
                response.put("roomId", b.getRoomId());
                response.put("roomNumber", room.map(Room::getRoomNumber).orElse("Não disponível"));
                response.put("roomType", room.map(Room::getRoomType).orElse("Não disponível"));
                response.put("roomDescription", room.map(Room::getDescription).orElse("Não disponível"));
                response.put("checkInDate", checkInDate);
                response.put("checkOutDate", checkOutDate);
                response.put("reservationNumber", b.getReservationNumber());
                response.put("dailyValue", b.getDailyValue());
                response.put("totalAmount", totalAmount);
                response.put("totalDays", totalDays);
                response.put("breakfast", b.isBreakfastIncluded() ? "Incluído" : "Não incluído");

                return ResponseEntity.ok(response);
            }

            return ResponseEntity.status(404).body(Map.of("message", "Guest not found"));
        }

        return ResponseEntity.status(404).body(Map.of("message", "Booking not found"));
    }

    //  as despesas de um booking
    @GetMapping("/{reservationNumber}/expenses")
    public ResponseEntity<ExpenseSummary> getExpensesByReservationNumber(@PathVariable String reservationNumber) {
        try {
            ExpenseSummary expenseSummary = bookingService.getExpensesByReservationNumber(reservationNumber);
            return ResponseEntity.ok(expenseSummary);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}