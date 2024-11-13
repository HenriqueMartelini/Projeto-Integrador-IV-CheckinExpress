package com.checkinExpress.checkin_express.repository;

import com.checkinExpress.checkin_express.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String> {
}