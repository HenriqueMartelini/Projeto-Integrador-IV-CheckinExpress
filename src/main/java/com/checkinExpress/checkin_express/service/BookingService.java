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
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final GuestRepository guestRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, GuestRepository guestRepository) {
        this.bookingRepository = bookingRepository;
        this.guestRepository = guestRepository;
    }

    // Cria um novo booking
    public Booking createBooking(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null");
        }
        return bookingRepository.save(booking);
    }

    // Pega um booking pelo ID
    public Booking getBookingById(String id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with ID: " + id));
    }

    // Pega todos os bookings
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // Deletar um booking pelo ID
    public void deleteBookingById(String id) {
        if (!bookingRepository.existsById(id)) {
            throw new BookingNotFoundException("Booking not found with ID: " + id);
        }
        bookingRepository.deleteById(id);
    }

    // Pega as despesas de um booking pelo ID
    public ExpenseSummary getExpensesByBookingId(String id) {
        Booking booking = getBookingById(id);
        double totalExpenses = booking.getExpenses().stream().mapToDouble(Expense::getAmount).sum();
        return new ExpenseSummary(booking.getExpenses(), totalExpenses);
    }

    // Validar um booking
    public boolean validateBooking(String reservationNumber, String guestName) {
        return bookingRepository.findByReservationNumberAndGuestName(reservationNumber, guestName).isPresent();
    }

    // Realiza um checkin de um booking
    public Booking checkInBooking(String reservationNumber, String guestName, String documentType, String documentNumber) {
        System.out.println("Check-in iniciado com: " + reservationNumber + ", " + guestName + ", " + documentType + ", " + documentNumber);

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
        return cpf != null && cpf.matches("\\d{11}");
    }

    private boolean isValidPassport(String passport) {
        return passport != null && passport.matches("\\d{6,9}");
    }

    // Pega os detalhes de um booking pelo número da reserva
    public Optional<Booking> getBookingDetailsByReservationNumber(String reservationNumber) {
        return bookingRepository.findByReservationNumber(reservationNumber);
    }

    // Pega um booking pelo número da reserva
    public Booking getBookingByReservationNumber(String reservationNumber) {
        return bookingRepository.findByReservationNumber(reservationNumber)
                .orElseThrow(() -> new BookingNotFoundException("Reserva não encontrada com o número da reserva: " + reservationNumber));
    }

    // Método para buscar despesas pelo número da reserva
    public ExpenseSummary getExpensesByReservationNumber(String reservationNumber) {
        Booking booking = getBookingByReservationNumber(reservationNumber);
        double totalExpenses = booking.getExpenses().stream()
                .mapToDouble(Expense::getAmount)
                .sum();
        return new ExpenseSummary(booking.getExpenses(), totalExpenses);
    }
}