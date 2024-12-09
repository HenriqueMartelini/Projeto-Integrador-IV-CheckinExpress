package com.checkinExpress.checkin_express.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Document(collection = "bookings")
public class Booking {
    @Id
    private String id;
    private String reservationNumber;
    private String guestId;
    private String guestName;
    private String roomId;

    private String bookingDateString;
    private List<Expense> expenses;
    private String checkInDateString;
    private String checkOutDateString;
    private double dailyValue;
    private int totalDays;
    private boolean breakfastIncluded;

    // Construtor padrão
    public Booking() {
    }

    // Construtor com todos os campos
    public Booking(String id, String reservationNumber, String guestId, String guestName, String roomId, String bookingDateString, List<Expense> expenses, String checkInDateString, String checkOutDateString, double dailyValue, int totalDays, boolean breakfastIncluded) {
        this.id = id;
        this.reservationNumber = reservationNumber;
        this.guestId = guestId;
        this.guestName = guestName;
        this.roomId = roomId;
        this.bookingDateString = bookingDateString;
        this.expenses = expenses;
        this.checkInDateString = checkInDateString;
        this.checkOutDateString = checkOutDateString;
        this.dailyValue = dailyValue;
        this.totalDays = totalDays;
        this.breakfastIncluded = breakfastIncluded;
    }

    public <T> Booking(String id, String reservationNumber, String roomId, Date bookingDate, List<T> list) {
    }

    public Booking(String number, String number1, LocalDate now, LocalDate localDate) {
    }

    public Booking(String guestName, String date, String date1) {
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getBookingDateString() {
        return bookingDateString;
    }

    public void setBookingDateString(String bookingDateString) {
        this.bookingDateString = bookingDateString;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public String getCheckInDateString() {
        return checkInDateString;
    }

    public void setCheckInDateString(String checkInDateString) {
        this.checkInDateString = checkInDateString;
    }

    public String getCheckOutDateString() {
        return checkOutDateString;
    }

    public void setCheckOutDateString(String checkOutDateString) {
        this.checkOutDateString = checkOutDateString;
    }

    // Métodos de conversão para Date
    public Date getBookingDate() {
        return convertStringToDate(bookingDateString);
    }

    public Date getCheckInDate() {
        return convertStringToDate(checkInDateString);
    }

    public Date getCheckOutDate() {
        return convertStringToDate(checkOutDateString);
    }

    public Date convertStringToDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null; // ou use uma data default, como o currentTime
        }
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(dateString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Data inválida", e);
        }
    }

    // Método para definir a data de check-in como Date
    public void setCheckInDate(Date checkInDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        this.checkInDateString = formatter.format(checkInDate);  // Converte o Date para String no formato correto
    }

    // Método para definir a data de check-out como Date
    public void setCheckOutDate(Date checkOutDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        this.checkOutDateString = formatter.format(checkOutDate);  // Converte o Date para String no formato correto
    }

    // Getters e Setters para as novas propriedades
    public double getDailyValue() {
        return dailyValue;
    }

    public void setDailyValue(double dailyValue) {
        this.dailyValue = dailyValue;
    }

    public int getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }

    public boolean isBreakfastIncluded() {
        return breakfastIncluded;
    }

    public void setBreakfastIncluded(boolean breakfastIncluded) {
        this.breakfastIncluded = breakfastIncluded;
    }
}