package com.krish.rentmate.models;

import java.io.Serializable;

public class Expense implements Serializable {
    private String id;
    private String title;
    private double amount;
    private String date;
    private String category;
    private String status; // "Paid" or "Pending"
    private String paidBy;
    private String splitBetween;
    private int iconResId;

    public Expense(String id, String title, double amount, String date, String category, 
                   String status, String paidBy, String splitBetween, int iconResId) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.status = status;
        this.paidBy = paidBy;
        this.splitBetween = splitBetween;
        this.iconResId = iconResId;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public double getAmount() { return amount; }
    public String getDate() { return date; }
    public String getCategory() { return category; }
    public String getStatus() { return status; }
    public String getPaidBy() { return paidBy; }
    public String getSplitBetween() { return splitBetween; }
    public int getIconResId() { return iconResId; }
}