package com.krish.rentmate.models;

import java.io.Serializable;

public class User implements Serializable {
    private String uid;
    private String name;
    private String email;
    private String phone;
    private String profileImageUrl;
    private String college;
    private String occupation;
    private String location;
    private int budgetMin;
    private int budgetMax;
    private String bio;
    private long createdAt;
    private long updatedAt;

    public User() {
        // Required empty constructor for Firestore
    }

    public User(String uid, String name, String email, String phone) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.occupation = "Student";
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public String getCollege() { return college; }
    public void setCollege(String college) { this.college = college; }

    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getBudgetMin() { return budgetMin; }
    public void setBudgetMin(int budgetMin) { this.budgetMin = budgetMin; }

    public int getBudgetMax() { return budgetMax; }
    public void setBudgetMax(int budgetMax) { this.budgetMax = budgetMax; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
}