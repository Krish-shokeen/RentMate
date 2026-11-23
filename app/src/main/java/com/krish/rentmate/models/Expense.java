package com.krish.rentmate.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Expense implements Serializable {
    private String expenseId;
    private String createdBy;
    private String title;
    private String category;
    private double totalAmount;
    private String paidBy;
    private String splitType;
    private List<String> participantIds;
    private String date;
    private String status; // "Paid" or "Pending"
    private int iconResId;
    private long createdAt;
    private long updatedAt;

    public Expense() {
        // Required for Firestore
        this.participantIds = new ArrayList<>();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public Expense(String expenseId, String title, double totalAmount, String date, String category, 
                   String status, String paidBy, String splitType, int iconResId) {
        this();
        this.expenseId = expenseId;
        this.title = title;
        this.totalAmount = totalAmount;
        this.date = date;
        this.category = category;
        this.status = status;
        this.paidBy = paidBy;
        this.splitType = splitType;
        this.iconResId = iconResId;
    }

    public String getExpenseId() { return expenseId; }
    public void setExpenseId(String expenseId) { this.expenseId = expenseId; }

    public String getId() { return expenseId; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public double getAmount() { return totalAmount; }

    public String getPaidBy() { return paidBy; }
    public void setPaidBy(String paidBy) { this.paidBy = paidBy; }

    public String getSplitType() { return splitType; }
    public void setSplitType(String splitType) { this.splitType = splitType; }

    public String getSplitBetween() { return splitType; }

    public List<String> getParticipantIds() { return participantIds; }
    public void setParticipantIds(List<String> participantIds) { this.participantIds = participantIds; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getIconResId() { return iconResId; }
    public void setIconResId(int iconResId) { this.iconResId = iconResId; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
}