package com.example.savaari.user;

import com.google.android.gms.maps.model.LatLng;

public class Rider {
    int userID;
    String username;
    String emailAddress;
    LatLng currentLocation;

    public Rider() {
        userID = -1;
        username = "";
        emailAddress = "";
        currentLocation = new LatLng(0, 0);
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public LatLng getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(LatLng currentLocation) {
        this.currentLocation = currentLocation;
    }
}
