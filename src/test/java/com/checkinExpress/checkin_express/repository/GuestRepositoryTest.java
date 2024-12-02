package com.checkinExpress.checkin_express.repository;

import com.checkinExpress.checkin_express.model.Guest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class GuestRepositoryTest {

    @Autowired
    private GuestRepository guestRepository;

    private Guest guest;

    @BeforeEach
    void setUp() {
        // Limpa a base de dados antes de cada teste
        guestRepository.deleteAll();

        // Criando um objeto de teste
        guest = new Guest("John Doe", "123456789", "ID");
        guestRepository.save(guest);
    }

    @Test
    void testFindByDocumentNumberAndDocumentType() {
        // Verifica se a consulta por número de documento e tipo retorna o guest correto
        Optional<Guest> foundGuest = guestRepository.findByDocumentNumberAndDocumentType("123456789", "ID");

        assertTrue(foundGuest.isPresent());
        assertEquals(guest.getName(), foundGuest.get().getName());
    }

    @Test
    void testFindByDocumentNumberAndDocumentTypeNoResults() {
        // Verifica se retorna um Optional vazio quando não encontrar o guest
        Optional<Guest> foundGuest = guestRepository.findByDocumentNumberAndDocumentType("nonexistent", "ID");

        assertFalse(foundGuest.isPresent());
    }

    @Test
    void testFindByName() {
        // Verifica se a consulta por nome retorna o guest correto
        Optional<Guest> foundGuest = guestRepository.findByName("John Doe");

        assertTrue(foundGuest.isPresent());
        assertEquals(guest.getDocumentNumber(), foundGuest.get().getDocumentNumber());
    }

    @Test
    void testFindByNameNoResults() {
        // Verifica se retorna um Optional vazio quando não encontrar o guest
        Optional<Guest> foundGuest = guestRepository.findByName("Nonexistent");

        assertFalse(foundGuest.isPresent());
    }
}