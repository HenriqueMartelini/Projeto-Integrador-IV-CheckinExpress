package com.checkinExpress.checkin_express.dto;

public class CheckOutRequest {
<<<<<<< HEAD
=======

    private String reservationNumber;
    private String guestName;

    // Getters e setters
    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    // Construtores
    public CheckOutRequest() {}

    public CheckOutRequest(String reservationNumber, String guestName) {
        this.reservationNumber = reservationNumber;
        this.guestName = guestName;
    }
>>>>>>> remotes/origin/main
}
