package com.example.savaari.user;

import com.google.android.gms.maps.model.LatLng;

public class Driver {
    int userID;
    String name;
    LatLng currentLocation;
    String status;

    public Driver() {
        reset();
    }

    public Driver(int userID, String name, LatLng currentLocation, String status) {
        Initialize(userID, name, currentLocation, status);
    }

    public void Initialize(int userID, String name, LatLng currentLocation, String status) {
        this.userID = userID;
        this.name = name;
        this.currentLocation = currentLocation;
        this.status = status;
    }

    public void reset() {
        userID = -1;
        name = "";
        currentLocation = null;
        status = "NOT_FOUND";
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(LatLng currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
