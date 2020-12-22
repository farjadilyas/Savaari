package com.example.savaari.ride.entity;

public class Vehicle
{
    public static final int
            DEFAULT_ID = 0;

    public static final int
            VH_DEFAULT = 0,
            VH_REQ_SENT = 1,
            VH_REQ_REJECTED = 2,
            VH_REQ_ACCEPTED = 3, // Dirty record
            VH_ACCEPTANCE_ACK = 4;

    // Main Attributes
    private int vehicleID;
    private int vehicleTypeID;
    private String make;
    private String model;
    private String year;
    private int rideTypeID;
    private String numberPlate;
    private int status;
    private String color;

    // Main Methods
    public Vehicle() {
        // Empty Constructor
    }

    public Vehicle(int vehicleID, String make, String model, String year, int rideTypeID, String numberPlate,
                   int status, String color) {
        this.vehicleID = vehicleID;
        this.make = make;
        this.model = model;
        this.year = year;
        this.rideTypeID = rideTypeID;
        this.numberPlate = numberPlate;
        this.status = status;
        this.color = color;
    }

    public int getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getRideTypeID() {
        return rideTypeID;
    }

    public void setRideTypeID(int rideTypeID) {
        this.rideTypeID = rideTypeID;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getVehicleTypeID() {
        return vehicleTypeID;
    }

    public void setVehicleTypeID(int vehicleTypeID) {
        this.vehicleTypeID = vehicleTypeID;
    }
}

