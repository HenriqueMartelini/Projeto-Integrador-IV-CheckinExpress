package com.checkinExpress.checkin_express.repository;

import com.checkinExpress.checkin_express.model.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class RoomRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    private Room room;

    @BeforeEach
    void setUp() {
        // Limpa a base de dados antes de cada teste
        roomRepository.deleteAll();

        // Criando um objeto de teste
        room = new Room("A1", "Single");
        roomRepository.save(room);
    }

    @Test
    void testFindByRoomNumber() {
        // Verifica se a consulta por número de quarto retorna o quarto correto
        Optional<Room> foundRoom = roomRepository.findByRoomNumber("A1");

        assertTrue(foundRoom.isPresent());
        assertEquals(room.getRoomNumber(), foundRoom.get().getRoomNumber());
    }

    @Test
    void testFindByRoomNumberNoResults() {
        // Verifica se retorna um Optional vazio quando não encontrar o quarto
        Optional<Room> foundRoom = roomRepository.findByRoomNumber("B1");

        assertFalse(foundRoom.isPresent());
    }
}