package com.krish.rentmate.models;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

public class Roommate implements Serializable {
    private String uid;
    private String name;
    private int age;
    private String college;
    private String occupation;
    private String preferredLocation;
    private int minBudget;
    private int maxBudget;
    private int compatibility;
    private List<String> lifestyleTags;
    private String foodPreference;
    private String smokingPreference;
    private String sleepSchedule;
    private String cleanliness;
    private String profileImageUrl;
    private String bio;
    private boolean isConnected;
    private int avatarResId;
    private long createdAt;
    private long updatedAt;

    public Roommate() {
        // Required for Firestore
        this.lifestyleTags = new ArrayList<>();
    }

    public Roommate(String uid, String name, int age, String occupation, String preferredLocation, 
                    int minBudget, int maxBudget, int compatibility, List<String> lifestyleTags, 
                    String bio, boolean isConnected, int avatarResId) {
        this();
        this.uid = uid;
        this.name = name;
        this.age = age;
        this.occupation = occupation;
        this.preferredLocation = preferredLocation;
        this.minBudget = minBudget;
        this.maxBudget = maxBudget;
        this.compatibility = compatibility;
        this.lifestyleTags = lifestyleTags;
        this.bio = bio;
        this.isConnected = isConnected;
        this.avatarResId = avatarResId;
    }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getCollege() { return college; }
    public void setCollege(String college) { this.college = college; }

    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }

    public String getProfession() { return occupation; }

    public String getPreferredLocation() { return preferredLocation; }
    public void setPreferredLocation(String preferredLocation) { this.preferredLocation = preferredLocation; }

    public String getLocation() { return preferredLocation; }

    public int getMinBudget() { return minBudget; }
    public void setMinBudget(int minBudget) { this.minBudget = minBudget; }

    public int getMaxBudget() { return maxBudget; }
    public void setMaxBudget(int maxBudget) { this.maxBudget = maxBudget; }

    public int getCompatibility() { return compatibility; }
    public void setCompatibility(int compatibility) { this.compatibility = compatibility; }

    public List<String> getLifestyleTags() { return lifestyleTags; }
    public void setLifestyleTags(List<String> lifestyleTags) { this.lifestyleTags = lifestyleTags; }

    public List<String> getTags() { return lifestyleTags; }

    public String getFoodPreference() { return foodPreference; }
    public void setFoodPreference(String foodPreference) { this.foodPreference = foodPreference; }

    public String getSmokingPreference() { return smokingPreference; }
    public void setSmokingPreference(String smokingPreference) { this.smokingPreference = smokingPreference; }

    public String getSleepSchedule() { return sleepSchedule; }
    public void setSleepSchedule(String sleepSchedule) { this.sleepSchedule = sleepSchedule; }

    public String getCleanliness() { return cleanliness; }
    public void setCleanliness(String cleanliness) { this.cleanliness = cleanliness; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public boolean isConnected() { return isConnected; }
    public void setConnected(boolean connected) { isConnected = connected; }

    public int getAvatarResId() { return avatarResId; }
    public void setAvatarResId(int avatarResId) { this.avatarResId = avatarResId; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
}