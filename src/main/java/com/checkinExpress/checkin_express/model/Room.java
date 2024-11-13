package com.checkinExpress.checkin_express.model;

public class Room {
    private String id;
    private String type;
    private double price;

    public Room(String id, String type, double price) {
        this.id = id;
        this.type = type;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}