package com.farjad.savaari.ride.entity;


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
    private VehicleType vehicleType;
    private int rideTypeID;
    private String numberPlate;
    private int status;
    private String color;

    // Main Methods
    public Vehicle() {
        // Default Constructor
        vehicleType = new VehicleType();
        vehicleID = DEFAULT_ID;
    }

    public Vehicle(int vehicleID) {
        vehicleType = new VehicleType();
        this.vehicleID = vehicleID;
    }

    public Vehicle(int vehicleID, String make, String model, String year, int rideTypeID, String numberPlate,
                   int status, String color) {

        vehicleType = new VehicleType();
        this.vehicleID = vehicleID;
        vehicleType.setMake(make);
        vehicleType.setModel(model);
        vehicleType.setYear(year);
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

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public int getVehicleTypeID() {
        return vehicleType.getVehicleTypeID();
    }

    public void setVehicleTypeID(int vehicleTypeID) {
        vehicleType.setVehicleTypeID(vehicleTypeID);
    }

    public String getMake() {
        return vehicleType.getMake();
    }

    public void setMake(String make) {
        vehicleType.setMake(make);
    }

    public String getModel() {
        return vehicleType.getModel();
    }

    public void setModel(String model) {
        vehicleType.setModel(model);
    }

    public String getYear() {
        return vehicleType.getYear();
    }

    public void setYear(String year) {
        vehicleType.setYear(year);
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
}
