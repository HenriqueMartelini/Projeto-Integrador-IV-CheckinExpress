package com.checkinExpress.checkin_express.service;

import com.checkinExpress.checkin_express.model.Booking;
import com.checkinExpress.checkin_express.model.Expense;
import com.checkinExpress.checkin_express.model.ExpenseSummary;
import com.checkinExpress.checkin_express.repository.BookingRepository;
import com.checkinExpress.checkin_express.exception.BookingNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {
    private BookingRepository bookingRepository = null;

    public BookingService() {
        this.bookingRepository = bookingRepository;
    }

    public Booking createBooking(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null");
        }
        return bookingRepository.save(booking);
    }

    public Booking getBookingById(String id) {
        return bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException("Booking not found with ID: " + id));
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public void deleteBookingById(String id) {
        if (!bookingRepository.existsById(id)) {
            throw new BookingNotFoundException("Booking not found with ID: " + id);
        }
        bookingRepository.deleteById(id);
    }

    public ExpenseSummary getExpensesByBookingId(String id) {
        Booking booking = getBookingById(id);
        double totalExpenses = booking.getExpenses().stream().mapToDouble(Expense::getAmount).sum();
        return new ExpenseSummary(booking.getExpenses(), totalExpenses);
    }
}