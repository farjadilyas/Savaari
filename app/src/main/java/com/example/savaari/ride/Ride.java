package com.example.savaari.ride;

import com.example.savaari.user.Driver;
import com.example.savaari.user.Rider;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Ride {
    Rider rider;
    Driver driver;
    private LatLng pickupLocation;
    private String pickupTitle;
    private LatLng dropoffLocation;
    private String dropoffTitle;
    private ArrayList<LatLng> stops;

    Ride() {
        rider = new Rider();
        driver = new Driver();
        pickupLocation = null;
        dropoffLocation = null;
        stops = new ArrayList<>();
    }

    public Rider getRider() {
        return rider;
    }

    public void setRider(Rider rider) {
        this.rider = rider;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public LatLng getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(LatLng pickupLocation, String pickupTitle) {
        this.pickupLocation = pickupLocation;
    }

    public LatLng getDropoffLocation() {
        return dropoffLocation;
    }

    public void setDropoffLocation(LatLng dropoffLocation, String dropoffTitle) {
        this.dropoffLocation = dropoffLocation;
    }

    public ArrayList<LatLng> getStops() {
        return stops;
    }

    public void setStops(ArrayList<LatLng> stops) {
        this.stops = stops;
    }
}
