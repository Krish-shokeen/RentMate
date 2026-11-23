package com.krish.rentmate.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Property implements Serializable {
    private String id;
    private String ownerId;
    private String title;
    private String description;
    private String address;
    private String city;
    private String locality;
    private double latitude;
    private double longitude;
    private int rent;
    private float rating;
    private String type;
    private int bedrooms;
    private int bathrooms;
    private String furnishing;
    private String amenities;
    private List<String> imageUrls;
    private boolean available;
    private boolean isFavorite;
    private int imageResId;
    private long createdAt;
    private long updatedAt;

    public Property() {
        // Required for Firestore
        this.imageUrls = new ArrayList<>();
        this.available = true;
    }

    public Property(String id, String title, String location, int rent, float rating, String type, 
                    int bedrooms, int bathrooms, String furnishing, String description, 
                    String amenities, boolean isFavorite, int imageResId) {
        this();
        this.id = id;
        this.title = title;
        this.locality = location;
        this.city = location;
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
    public void setId(String id) { this.id = id; }

    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getLocality() { return locality; }
    public void setLocality(String locality) { this.locality = locality; }

    public String getLocation() { 
        return locality != null ? locality : (city != null ? city : ""); 
    }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public int getRent() { return rent; }
    public void setRent(int rent) { this.rent = rent; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getBedrooms() { return bedrooms; }
    public void setBedrooms(int bedrooms) { this.bedrooms = bedrooms; }

    public int getBathrooms() { return bathrooms; }
    public void setBathrooms(int bathrooms) { this.bathrooms = bathrooms; }

    public String getFurnishing() { return furnishing; }
    public void setFurnishing(String furnishing) { this.furnishing = furnishing; }

    public String getAmenities() { return amenities; }
    public void setAmenities(String amenities) { this.amenities = amenities; }

    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public int getImageResId() { return imageResId; }
    public void setImageResId(int imageResId) { this.imageResId = imageResId; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
}