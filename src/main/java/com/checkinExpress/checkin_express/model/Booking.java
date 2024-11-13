package com.checkinExpress.checkin_express.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "bookings")
public class Booking {
    @Id
    private String id;
    private String guestId;
    private String roomId;
    private Date bookingDate;
    private List<Expense> expenses;

    public Booking() {}

    public Booking(String id, String guestId, String roomId, Date bookingDate, List<Expense> expenses) {
        this.id = id;
        this.guestId = guestId;
        this.roomId = roomId;
        this.bookingDate = bookingDate;
        this.expenses = expenses;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }
}