package com.checkinExpress.checkin_express.service;

import com.checkinExpress.checkin_express.exception.GuestNotFoundException;
import com.checkinExpress.checkin_express.model.Booking;
import com.checkinExpress.checkin_express.model.Expense;
import com.checkinExpress.checkin_express.model.ExpenseSummary;
import com.checkinExpress.checkin_express.model.Guest;
import com.checkinExpress.checkin_express.repository.BookingRepository;
import com.checkinExpress.checkin_express.repository.GuestRepository;
import com.checkinExpress.checkin_express.exception.BookingNotFoundException;
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
                .orElseThrow(() -> new GuestNotFoundException("Hóspede não encontrado."));

        // Valida o documento (CPF ou passaporte)
        if (documentType.equals("CPF")) {
            if (!isValidCPF(documentNumber)) {
                throw new IllegalArgumentException("CPF inválido.");
            }
        } else if (documentType.equals("Passaporte")) {
            if (!isValidPassport(documentNumber)) {
                throw new IllegalArgumentException("Passaporte inválido.");
            }
        } else {
            throw new IllegalArgumentException("Tipo de documento inválido.");
        }

        // Atualiza a data de check-in
        booking.setCheckInDate(new Date()); // Define a data atual como a data de check-in
        bookingRepository.save(booking);

        return booking;
    }


    private boolean isValidCPF(String cpf) {
        // Implementação básica da validação do CPF
        // A validação real pode ser mais complexa, incluindo a verificação de dígitos de verificação
        return cpf.matches("[0-9]{11}");
    }

    private boolean isValidPassport(String passport) {
        // Implementação básica da validação do passaporte
        // A validação real pode incluir verificar o formato, o país de origem, etc.
        return passport.matches("[0-9]{6,9}");
    }

    public ExpenseSummary checkOutBooking(String reservationNumber, String guestName) {
        // Lógica para verificar se a reserva existe
        Booking booking = bookingRepository.findByReservationNumberAndGuestName(reservationNumber, guestName)
                .orElseThrow(() -> new BookingNotFoundException("Reserva não encontrada para os dados fornecidos."));

        // Verificar se o check-in foi realizado
        if (booking.getCheckInDate() == null) {
            throw new IllegalStateException("O hóspede ainda não fez o check-in.");
        }

        // Buscar as despesas da reserva
        double totalExpenses = booking.getExpenses().stream().mapToDouble(Expense::getAmount).sum();

        // Atualizar a data de check-out
        booking.setCheckOutDateString(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date()));  // Atualizando a data do check-out
        bookingRepository.save(booking);

        // Criar o resumo de despesas
        ExpenseSummary expenseSummary = new ExpenseSummary(booking.getExpenses(), totalExpenses);

        // Retornar o resumo das despesas
        return expenseSummary;
    }

}
