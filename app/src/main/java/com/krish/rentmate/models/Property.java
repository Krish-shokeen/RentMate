package com.krish.rentmate.models;

import java.io.Serializable;

public class Property implements Serializable {
    private String id;
    private String title;
    private String location;
    private int rent;
    private float rating;
    private String type;
    private int bedrooms;
    private int bathrooms;
    private String furnishing;
    private String description;
    private String amenities;
    private boolean isFavorite;
    private int imageResId;

    public Property(String id, String title, String location, int rent, float rating, String type, 
                    int bedrooms, int bathrooms, String furnishing, String description, 
                    String amenities, boolean isFavorite, int imageResId) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.rent = rent;
        this.rating = rating;
        this.type = type;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.furnishing = furnishing;
        this.description = description;
        this.amenities = amenities;
        this.isFavorite = isFavorite;
        this.imageResId = imageResId;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getLocation() { return location; }
    public int getRent() { return rent; }
    public float getRating() { return rating; }
    public String getType() { return type; }
    public int getBedrooms() { return bedrooms; }
    public int getBathrooms() { return bathrooms; }
    public String getFurnishing() { return furnishing; }
    public String getDescription() { return description; }
    public String getAmenities() { return amenities; }
    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
    public int getImageResId() { return imageResId; }
}