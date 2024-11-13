package com.checkinExpress.checkin_express.service;

import com.checkinExpress.checkin_express.model.Booking;
import com.checkinExpress.checkin_express.model.Expense;
import com.checkinExpress.checkin_express.model.ExpenseSummary;
import com.checkinExpress.checkin_express.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Booking getBookingById(String id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public void deleteBookingById(String id) {
        bookingRepository.deleteById(id);
    }

    public ExpenseSummary getExpensesByBookingId(String id) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking != null) {
            double totalExpenses = booking.getExpenses().stream().mapToDouble(Expense::getAmount).sum();
            return new ExpenseSummary(booking.getExpenses(), totalExpenses);
        }
        return null;
    }
}