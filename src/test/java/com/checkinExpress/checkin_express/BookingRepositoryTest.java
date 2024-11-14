package com.checkinExpress.checkin_express;

import com.checkinExpress.checkin_express.model.Booking;
import com.checkinExpress.checkin_express.model.Expense;
import com.checkinExpress.checkin_express.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    private Booking booking;

    @BeforeEach
    public void setUp() {
        booking = new Booking();
        booking.setId("1");
        booking.setExpenses(Collections.singletonList(new Expense("Café", 100.0)));
    }

    @Test
    public void testSaveBooking() {
        Booking savedBooking = bookingRepository.save(booking);
        assertNotNull(savedBooking);
        assertEquals("1", savedBooking.getId());
    }

    @Test
    public void testFindBookingById() {
        bookingRepository.save(booking);
        Booking foundBooking = bookingRepository.findById("1").orElse(null);
        assertNotNull(foundBooking);
        assertEquals("Café", foundBooking.getExpenses().get(0).getDescription());
    }
}