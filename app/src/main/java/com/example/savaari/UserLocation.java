package com.example.savaari;

public class UserLocation
{
    // Main Attributes
    int userID;
    double latitude;
    double longitude;
    String timestamp;

    // Main Methods
    public UserLocation()
    {
        // Empty Constructor
        userID = 0;
        latitude = 0;
        longitude = 0;
        timestamp = "";
    }
    public UserLocation(int userID, double latitude, double longitude, String timestamp)
    {
        this.userID = userID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }
    // Getters and Setters
    public int getUserID()
    {
        return userID;
    }
    public void setUserID(int userID)
    {
        this.userID = userID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
