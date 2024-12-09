package com.checkinExpress.checkin_express.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "expenses")
public class Expense {

    @Id
    private String id;
    private String description;
    private double amount;
    private String bookingId;
    private Date expenseDate;
    private String category;

    // Construtor padrão
    public Expense() {
    }

    // Construtor completo
    public Expense(String id, String description, double amount, String bookingId, Date expenseDate, String category) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.bookingId = bookingId;
        this.expenseDate = expenseDate;
        this.category = category;
    }

    // Construtor dos arquivos de testes
    public Expense(String description, double amount) {
        this.description = description;
        this.amount = amount;
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public Date getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(Date expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
