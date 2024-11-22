package com.checkinExpress.checkin_express.model;

import java.util.List;

public class ExpenseSummary {
    private List<Expense> expenses;
    private double total;

    public ExpenseSummary(List<Expense> expenses, double total) {
        this.expenses = expenses;
        this.total = total;
    }

    public ExpenseSummary() {

    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}