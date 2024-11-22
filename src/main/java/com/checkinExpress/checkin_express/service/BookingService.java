package com.checkinExpress.checkin_express.service;

import com.checkinExpress.checkin_express.exception.BookingNotFoundException;
import com.checkinExpress.checkin_express.exception.GuestNotFoundException;
import com.checkinExpress.checkin_express.model.Booking;
import com.checkinExpress.checkin_express.model.Expense;
import com.checkinExpress.checkin_express.model.ExpenseSummary;
import com.checkinExpress.checkin_express.model.Guest;
import com.checkinExpress.checkin_express.repository.BookingRepository;
import com.checkinExpress.checkin_express.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final GuestRepository guestRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, GuestRepository guestRepository) {
        this.bookingRepository = bookingRepository;
        this.guestRepository = guestRepository;
    }

    public Booking createBooking(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null");
        }
        return bookingRepository.save(booking);
    }

    public Booking getBookingById(String id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with ID: " + id));
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

    public boolean validateBooking(String reservationNumber, String guestName) {
        return bookingRepository.findByReservationNumberAndGuestName(reservationNumber, guestName).isPresent();
    }

    public Booking checkInBooking(String reservationNumber, String guestName, String documentType, String documentNumber) {
        // Valida a reserva
        Booking booking = bookingRepository.findByReservationNumberAndGuestName(reservationNumber, guestName)
                .orElseThrow(() -> new BookingNotFoundException("Reserva não encontrada para os dados fornecidos."));

        // Verifica se o hóspede existe
        Guest guest = guestRepository.findById(booking.getGuestId())
                .orElseThrow();

        // Valida o documento (CPF ou passaporte)
        if ("CPF".equalsIgnoreCase(documentType)) {
            if (!isValidCPF(documentNumber)) {
                throw new IllegalArgumentException("CPF inválido.");
            }
        } else if ("Passaporte".equalsIgnoreCase(documentType)) {
            if (!isValidPassport(documentNumber)) {
                throw new IllegalArgumentException("Passaporte inválido.");
            }
        } else {
            throw new IllegalArgumentException("Tipo de documento inválido.");
        }

        // Atualiza a data de check-in
        booking.setCheckInDate(new Date());
        bookingRepository.save(booking);

        return booking;
    }

    private boolean isValidCPF(String cpf) {
        // Validação básica do CPF (apenas formato, sem cálculo de dígitos verificadores)
        return cpf != null && cpf.matches("\\d{11}");
    }

    private boolean isValidPassport(String passport) {
        // Validação básica do passaporte (apenas formato)
        return passport != null && passport.matches("\\d{6,9}");
    }

    public ExpenseSummary checkOutBooking(String reservationNumber, String guestName) {
        // Valida a reserva
        Booking booking = bookingRepository.findByReservationNumberAndGuestName(reservationNumber, guestName)
                .orElseThrow(() -> new BookingNotFoundException("Reserva não encontrada para os dados fornecidos."));

        // Verifica se o check-in foi realizado
        if (booking.getCheckInDate() == null) {
            throw new IllegalStateException("O hóspede ainda não fez o check-in.");
        }

        // Calcula o total das despesas
        double totalExpenses = booking.getExpenses().stream().mapToDouble(Expense::getAmount).sum();

        // Atualiza a data de check-out
        booking.setCheckOutDateString(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date()));
        bookingRepository.save(booking);

        // Retorna o resumo de despesas
        return new ExpenseSummary(booking.getExpenses(), totalExpenses);
    }
}