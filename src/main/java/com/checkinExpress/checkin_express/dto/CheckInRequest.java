package com.checkinExpress.checkin_express.dto;

import jakarta.validation.constraints.NotBlank;

public class CheckInRequest {
    @NotBlank
    private String reservationNumber;
    @NotBlank
    private String guestName;
    @NotBlank
    private String documentType;
    @NotBlank
    private String documentNumber;

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

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }
}