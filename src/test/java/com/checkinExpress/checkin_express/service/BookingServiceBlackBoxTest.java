package com.checkinExpress.checkin_express.service;

import com.checkinExpress.checkin_express.model.Booking;
import com.checkinExpress.checkin_express.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceBlackBoxTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    public BookingServiceBlackBoxTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBooking() {
        Booking mockBooking = new Booking("1", "101", LocalDate.now(), LocalDate.now().plusDays(5));
        when(bookingRepository.save(mockBooking)).thenReturn(mockBooking);

        Booking createdBooking = bookingService.createBooking(mockBooking);

        assertNotNull(createdBooking);
        assertEquals("101", createdBooking.getRoomId());
        verify(bookingRepository, times(1)).save(mockBooking);
    }
}