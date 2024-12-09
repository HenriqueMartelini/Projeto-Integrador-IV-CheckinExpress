package com.checkinExpress.checkin_express.controller;

import com.checkinExpress.checkin_express.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping("/connection")
    public ResponseEntity<String> checkDatabaseConnection() {
        try {
            long count = bookingRepository.count();
            String message = "Conectado ao banco de dados com sucesso. Número total de reservas: " + count;
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }
}
