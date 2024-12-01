package com.checkinExpress.checkin_express.repository;

import com.checkinExpress.checkin_express.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String> {
    Optional<Booking> findByReservationNumberAndGuestName(String reservationNumber, String guestName);
    Optional<Booking> findByReservationNumber(String reservationNumber);


}