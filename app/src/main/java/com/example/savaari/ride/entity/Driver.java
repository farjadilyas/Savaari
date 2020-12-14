package com.example.savaari.ride.entity;

public class Driver extends User{

    // Main Attributes
    private static final String LOG_TAG = Driver.class.getSimpleName();
    Boolean isActive;
    Boolean isTakingRide;
    int rideRequestStatus;

    // Main Constructors
    public Driver() {
        super();
    }
    public Driver(Location currentLocation, Boolean isActive, Boolean isTakingRide) {
        super();
        setCurrentLocation(currentLocation);
        setIsActive(isActive);
        setIsTakingRide(isTakingRide);
    }

    // Getters and Setters
    public Boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    public Boolean getIsTakingRide() {
        return isTakingRide;
    }
    public void setIsTakingRide(Boolean isTakingRide) {
        this.isTakingRide = isTakingRide;
    }
    public int getRideRequestStatus() {
        return rideRequestStatus;
    }
    public void setRideRequestStatus(int rideRequestStatus) {
        this.rideRequestStatus = rideRequestStatus;
    }
}
