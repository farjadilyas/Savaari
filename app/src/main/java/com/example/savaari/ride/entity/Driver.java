package com.example.savaari.ride.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Driver extends User{

    // Status flags values
    public static final int
            DV_DEFAULT = 0,
            DV_REQ_SENT = 1,
            DV_REQ_REJECTED= 2,
            DV_REQ_APPROVED = 3;

    // Main Attributes
    private static final String LOG_TAG = Driver.class.getSimpleName();
    private Boolean active;
    private Boolean takingRide;
    private int rideRequestStatus;
    @JsonProperty("CNIC")
    private String CNIC;
    private String licenseNumber;
    private int status;
    private Vehicle activeVehicle;
    private ArrayList<Vehicle> vehicles;

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

    public String getCNIC() {
        return CNIC;
    }

    public void setCNIC(String CNIC) {
        this.CNIC = CNIC;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Vehicle getActiveVehicle() {
        return activeVehicle;
    }

    public void setActiveVehicle(Vehicle activeVehicle) {
        this.activeVehicle = activeVehicle;
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(ArrayList<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }
}
