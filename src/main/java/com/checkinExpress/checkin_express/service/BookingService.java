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

    // Criação de uma reserva
    public Booking createBooking(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("A reserva não pode ser nula.");
        }
        return bookingRepository.save(booking);
    }

    // Busca uma reserva pelo ID
    public Booking getBookingById(String id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Reserva não encontrada com o ID: " + id));
    }

    // Lista todas as reservas
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // Exclui uma reserva pelo ID
    public void deleteBookingById(String id) {
        if (!bookingRepository.existsById(id)) {
            throw new BookingNotFoundException("Reserva não encontrada com o ID: " + id);
        }
        bookingRepository.deleteById(id);
    }

    // Busca as despesas associadas a uma reserva pelo ID
    public ExpenseSummary getExpensesByBookingId(String id) {
        Booking booking = getBookingById(id);
        double totalExpenses = booking.getExpenses().stream()
                .mapToDouble(Expense::getAmount)
                .sum();
        return new ExpenseSummary(booking.getExpenses(), totalExpenses);
    }

    // Valida a reserva com base no número da reserva e nome do hóspede
    public boolean validateBooking(String reservationNumber, String guestName) {
        return bookingRepository.findByReservationNumberAndGuestName(reservationNumber, guestName).isPresent();
    }

    // Realiza o check-in de uma reserva com base no número da reserva e nome do hóspede
    public Booking checkInBooking(String reservationNumber, String guestName, String documentType, String documentNumber) {
        // Busca a reserva pelo número da reserva e nome do hóspede
        Booking booking = bookingRepository.findByReservationNumberAndGuestName(reservationNumber, guestName)
                .orElseThrow(() -> new BookingNotFoundException("Reserva não encontrada para o hóspede: " + guestName));

        // Valida o hóspede com base no número e tipo de documento
        Guest guest = guestRepository.findByDocumentNumberAndDocumentType(documentNumber, documentType)
                .orElseThrow(() -> new GuestNotFoundException("Hóspede não encontrado com o documento: " + documentNumber));

        // Verifica se o hóspede da reserva corresponde ao hóspede encontrado
        if (!guest.getName().equalsIgnoreCase(booking.getGuestName())) {
            throw new IllegalArgumentException("O hóspede não corresponde ao nome na reserva.");
        }

        // Define a data de check-in
        booking.setCheckInDate(new Date());

        // Salva a reserva com a data de check-in
        bookingRepository.save(booking);

        return booking;
    }


    // Realiza o check-out de uma reserva pelo ID da reserva
    public ExpenseSummary checkOutBooking(String bookingId) {
        Booking booking = getBookingById(bookingId);

        if (booking.getCheckInDate() == null) {
            throw new IllegalStateException("O hóspede ainda não fez o check-in.");
        }

        // Calcular as despesas do hóspede
        double totalExpenses = booking.getExpenses().stream()
                .mapToDouble(Expense::getAmount)
                .sum();

        // Define a data de check-out
        if (booking.getCheckOutDate() == null) {
            booking.setCheckOutDate(new Date());
        }

        bookingRepository.save(booking);

        return new ExpenseSummary(booking.getExpenses(), totalExpenses);
    }

    // Validação do documento (CPF ou Passaporte)
    private void validateDocument(Guest guest, String documentType, String documentNumber) {
        // Tipo de documento inválido
        if (!guest.getDocumentType().equalsIgnoreCase(documentType)) {
            throw new IllegalArgumentException("Tipo de documento inválido. Esperado: " + guest.getDocumentType());
        }

        // Número do documento inválido
        if (!guest.getDocumentNumber().equals(documentNumber)) {
            throw new IllegalArgumentException("Número do documento inválido.");
        }

        // Valida o número do documento
        if ("CPF".equalsIgnoreCase(documentType) && !isValidCPF(documentNumber)) {
            throw new IllegalArgumentException("CPF inválido.");
        }
        if ("Passaporte".equalsIgnoreCase(documentType) && !isValidPassport(documentNumber)) {
            throw new IllegalArgumentException("Passaporte inválido.");
        }
    }

    // Validação básica de CPF
    private boolean isValidCPF(String cpf) {
        return cpf != null && cpf.matches("\\d{11}");
    }

    // Validação básica de passaporte
    private boolean isValidPassport(String passport) {
        return passport != null && passport.matches("\\d{6,9}");
    }

    public Optional<Booking> getBookingDetailsByReservationNumber(String reservationNumber) {
        return bookingRepository.findByReservationNumber(reservationNumber);
    }
}
