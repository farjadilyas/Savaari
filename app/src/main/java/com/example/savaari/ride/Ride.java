package com.example.savaari.ride;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Ride {
    private LatLng pickupLocation;
    private LatLng dropoffLocation;
    private ArrayList<LatLng> stops;

    Ride() {
        pickupLocation = null;
        dropoffLocation = null;
        stops = new ArrayList<>();
    }

    public LatLng getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(LatLng pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public LatLng getDropoffLocation() {
        return dropoffLocation;
    }

    public void setDropoffLocation(LatLng dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
    }

    public ArrayList<LatLng> getStops() {
        return stops;
    }

    public void setStops(ArrayList<LatLng> stops) {
        this.stops = stops;
    }
}
