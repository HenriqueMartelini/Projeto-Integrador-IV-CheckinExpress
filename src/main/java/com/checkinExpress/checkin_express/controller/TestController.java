package com.checkinExpress.checkin_express.controller;

<<<<<<< HEAD
public class TestController {
=======
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
    public ResponseEntity<String> testConnection() {
        try {
            long count = bookingRepository.count(); // Conta os documentos na coleção de reservas
            return ResponseEntity.ok("Conexão com o banco de dados bem-sucedida! Total de reservas: " + count);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }
>>>>>>> remotes/origin/main
}
