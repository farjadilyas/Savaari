package com.example.savaari.ride.entity;

import com.google.android.gms.maps.model.LatLng;

public class RideRequest {

    // Match status - used in business logic
    public static final int
            NO_CHANGE = 0,
            REJECTED = 1,
            FOUND = 2;

    // Match return status  - returned to clients
    public static final int
            DEFAULT = 50,
            PAIRED = 51,
            ALREADY_PAIRED = 52,
            NOT_PAIRED = 53,
            NOT_FOUND = 54,
            STATUS_ERROR = 55;

    protected Rider rider;
    protected Driver driver;
    protected Location pickupLocation;
    private String pickupTitle;
    protected Location dropoffLocation;
    private String dropoffTitle;
    private int findStatus;

    public RideRequest() {
        rider = new Rider();
        driver = new Driver();
        pickupLocation = null;
        dropoffLocation = null;
        findStatus = DEFAULT;
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
        return new LatLng(pickupLocation.getLatitude(), pickupLocation.getLongitude());
    }

    public String getPickupTitle() {
        return pickupTitle;
    }

    public void setPickupLocation(LatLng pickupLocation, String pickupTitle) {
        this.pickupLocation = new Location(pickupLocation);
        this.pickupTitle = pickupTitle;
    }

    public LatLng getDropoffLocation() {
        return new LatLng(dropoffLocation.getLatitude(), dropoffLocation.getLongitude());
    }

    public String getDropoffTitle() {
        return dropoffTitle;
    }

    public void setDropoffLocation(LatLng dropoffLocation, String dropoffTitle) {
        this.dropoffLocation = new Location(dropoffLocation);
        this.dropoffTitle = dropoffTitle;
    }

    public int getFindStatus() {
        return findStatus;
    }

    public void setFindStatus(int findStatus) {
        this.findStatus = findStatus;
    }
}
