package com.example.savaari.ride.entity;

import java.util.ArrayList;

public class Ride extends RideRequest {

    // Main Attributes
    // Main Attributes
    public static final int
            RS_DEFAULT = 10,
            PICKUP = 11,
            DRIVER_ARRIVED = 12,
            CANCELLED = 13,
            STARTED = 14,
            ARRIVED_AT_DEST = 15,
            PAYMENT_MADE = 16,
            END_ACKED = 20;

    int rideID;
    private Vehicle vehicle;
    private Payment payment;
    private long startTime;
    private long endTime;
    private double distanceTravelled;
    private double estimatedFare;
    private double fare;
    private int rideStatus;
    private ArrayList<Location> stops;

    public Ride() {
        //TODO: Don't allocate them here
        super();
        payment = new Payment();
        stops = new ArrayList<>();
        rideStatus = -1;
        setPaymentMethod(1);
        setRideType(1);
    }

    public int getRideID() {
        return rideID;
    }

    public void setRideID(int rideID) {
        this.rideID = rideID;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public boolean closeToPickup() {
        return (driver.getCurrentLocation().latitude - pickupLocation.getLatitude() < 0.2
        && driver.getCurrentLocation().longitude - pickupLocation.getLongitude() < 0.2);
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public double getEstimatedFare() {
        return estimatedFare;
    }

    public void setEstimatedFare(double estimatedFare) {
        this.estimatedFare = estimatedFare;
    }

    public int getRideStatus() {
        return rideStatus;
    }

    public void setRideStatus(int rideStatus) {
        this.rideStatus = rideStatus;
    }

    public ArrayList<Location> getStops() {
        return stops;
    }

    public void setStops(ArrayList<Location> stops) {
        this.stops = stops;
    }

    public double getDistanceTravelled() {
        return distanceTravelled;
    }

    public void setDistanceTravelled(double distanceTravelled) {
        this.distanceTravelled = distanceTravelled;
    }

    public double getFare() {
        return fare;
    }

    public void setFare(double fare) {
        this.fare = fare;
    }
}
