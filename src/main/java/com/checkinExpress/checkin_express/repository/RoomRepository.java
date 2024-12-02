package com.checkinExpress.checkin_express.repository;

import com.checkinExpress.checkin_express.model.Room;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;


public interface RoomRepository extends MongoRepository<Room, String> {
    Optional<Room> findByRoomNumber(String roomNumber);
}
