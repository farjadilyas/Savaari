package com.example.savaari.ride.entity;

public class Driver extends User{

    // Main Attributes
    private static final String LOG_TAG = Driver.class.getSimpleName();

    Boolean active;
    Boolean takingRide;
    int rideRequestStatus;

    // Main Constructors
    public Driver() {
        super();
    }
    public Driver(Location currentLocation, Boolean active, Boolean takingRide) {
        super();
        setCurrentLocation(currentLocation);
        setActive(active);
        setTakingRide(takingRide);
    }

    // Getters and Setters
    public Boolean getActive() {
        return active;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }
    public Boolean getTakingRide() {
        return takingRide;
    }
    public void setTakingRide(Boolean takingRide) {
        this.takingRide = takingRide;
    }
    public int getRideRequestStatus() {
        return rideRequestStatus;
    }
    public void setRideRequestStatus(int rideRequestStatus) {
        this.rideRequestStatus = rideRequestStatus;
    }
}
