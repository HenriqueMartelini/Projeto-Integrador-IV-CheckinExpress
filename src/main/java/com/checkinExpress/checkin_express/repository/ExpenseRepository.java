package com.checkinExpress.checkin_express.repository;

import com.checkinExpress.checkin_express.model.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ExpenseRepository extends MongoRepository<Expense, String> {
    List<Expense> findByBookingId(String bookingId);
}