package com.krish.rentmate.models;

import java.io.Serializable;

public class RoommateRequest implements Serializable {
    private String requestId;
    private String senderId;
    private String senderName;
    private String receiverId;
    private String status; // "pending", "accepted", "rejected"
    private long createdAt;
    private long updatedAt;

    public RoommateRequest() {
        // Required for Firestore
    }

    public RoommateRequest(String requestId, String senderId, String senderName, String receiverId, String status) {
        this.requestId = requestId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.receiverId = receiverId;
        this.status = status;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public String getReceiverId() { return receiverId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
}