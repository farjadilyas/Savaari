package com.example.savaari.user;

import com.google.android.gms.maps.model.LatLng;

public class Driver {
    int ID;
    String name;
    LatLng latLng;
    String status;

    public Driver() {
        reset();
    }

    public Driver(int ID, String name, LatLng latLng, String status) {
        Initialize(ID, name, latLng, status);
    }

    public void Initialize(int ID, String name, LatLng latLng, String status) {
        this.ID = ID;
        this.name = name;
        this.latLng = latLng;
        this.status = status;
    }

    public void reset() {
        ID = -1;
        name = "";
        latLng = null;
        status = "NOT_FOUND";
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
