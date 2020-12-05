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

    public Driver(int userID, String name, LatLng currentLocation) {
        Initialize(userID, name, currentLocation);
    }

    public void Initialize(int userID, String name, LatLng currentLocation) {
        this.userID = userID;
        this.name = name;
        this.currentLocation = currentLocation;
    }

    public void reset() {
        userID = -1;
        name = "";
        currentLocation = null;
        status = "DEFAULT";
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
