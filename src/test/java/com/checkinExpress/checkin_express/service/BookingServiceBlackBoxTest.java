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
}