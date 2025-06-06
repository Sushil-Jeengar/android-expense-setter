package com.sushil.myapplication;

public class Expense {
    private long id;
    private String description;
    private double amount;
    private String date;
    private String paidBy; // Changed to String to match your DB's paid_by TEXT column
    private long groupId;

    public Expense(long id, String description, double amount, String date, String paidBy, long groupId) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.paidBy = paidBy;
        this.groupId = groupId;
    }

    // Getters
    public long getId() { return id; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public String getDate() { return date; }
    public String getPaidBy() { return paidBy; }
    public long getGroupId() { return groupId; }

    // Setters
    public void setId(long id) { this.id = id; }
    public void setDescription(String description) { this.description = description; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setDate(String date) { this.date = date; }
    public void setPaidBy(String paidBy) { this.paidBy = paidBy; }
    public void setGroupId(long groupId) { this.groupId = groupId; }

    @Override
    public String toString() {
        return description + " - ₹" + String.format("%.2f", amount) + " on " + date + " paid by " + paidBy;
    }
}
