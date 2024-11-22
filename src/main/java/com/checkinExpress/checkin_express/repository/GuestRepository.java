package com.checkinExpress.checkin_express.repository;
import com.checkinExpress.checkin_express.model.Guest;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface GuestRepository extends MongoRepository<Guest, String> {
    Optional<Guest> findByDocumentNumberAndDocumentType(String documentNumber, String documentType);
}
