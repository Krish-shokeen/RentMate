package com.krish.rentmate.models;

import java.io.Serializable;
import java.util.List;

public class Roommate implements Serializable {
    private String id;
    private String name;
    private int age;
    private String profession;
    private String location;
    private int minBudget;
    private int maxBudget;
    private int compatibility;
    private List<String> tags;
    private String bio;
    private boolean isConnected;
    private int avatarResId;

    public Roommate(String id, String name, int age, String profession, String location, 
                    int minBudget, int maxBudget, int compatibility, List<String> tags, 
                    String bio, boolean isConnected, int avatarResId) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.profession = profession;
        this.location = location;
        this.minBudget = minBudget;
        this.maxBudget = maxBudget;
        this.compatibility = compatibility;
        this.tags = tags;
        this.bio = bio;
        this.isConnected = isConnected;
        this.avatarResId = avatarResId;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getProfession() { return profession; }
    public String getLocation() { return location; }
    public int getMinBudget() { return minBudget; }
    public int getMaxBudget() { return maxBudget; }
    public int getCompatibility() { return compatibility; }
    public List<String> getTags() { return tags; }
    public String getBio() { return bio; }
    public boolean isConnected() { return isConnected; }
    public void setConnected(boolean connected) { isConnected = connected; }
    public int getAvatarResId() { return avatarResId; }
}