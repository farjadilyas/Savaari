package com.savaari_demo.entity;

import com.savaari_demo.OracleDBHandler;

import java.util.ArrayList;

public class Ride extends RideRequest {

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
        payment = new Payment();
    }

    public Ride(RideRequest rideRequest) {
        setDriver(rideRequest.getDriver());
        setRider(rideRequest.getRider());
        setFindStatus(RideRequest.FOUND);
        setRideStatus(RideRequest.MS_REQ_ACCEPTED);
    }

    // ---------------------------------------------------------------------------------
    //                          GETTER and SETTER
    // ---------------------------------------------------------------------------------
    public Long getStartTime() {
        return startTime;
    }
    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }
    public Long getEndTime() {
        return endTime;
    }
    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
    public void setRideID(int rideID) { this.rideID = rideID; }
    public int getRideID() { return rideID; }
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
    public Location getPickupLocation() {
        return pickupLocation;
    }
    public void setPickupLocation(Location pickupLocation) {
        this.pickupLocation = pickupLocation;
    }
    public Location getDropoffLocation() {
        return dropoffLocation;
    }
    public void setDropoffLocation(Location dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
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

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public ArrayList<Location> getStops() {
        return stops;
    }

    public void setStops(ArrayList<Location> stops) {
        this.stops = stops;
    }

    // ---------------------------------------------------------------------------------
    // PRIVATE METHODS for POLICIES
    private double calculateFare()
    {
        // TODO: FARE POLICY and stuff ?
        return (distanceTravelled / 1000) * 40;
    }

    // ---------------------------------------------------------------------------------
    //                          System interaction methods
    // ---------------------------------------------------------------------------------
    public void fetchRideStatus() {
        OracleDBHandler.getInstance().getRideStatus(this);
    }
    public boolean markDriverArrival() {
        return OracleDBHandler.getInstance().markDriverArrival(this);
    }
    public boolean startRideDriver() { return OracleDBHandler.getInstance().startRideDriver(this); }


    // Acknowledge end of ride (final call)
    public boolean acknowledgeEndOfRide() {

        if (OracleDBHandler.getInstance().acknowledgeEndOfRide(this)) {
            return rider.reset(false);
        }

        return false;
    }

    // TODO: Methods need to get more data from tables

    // Main End Ride with Driver Method
    public double markArrivalAtDestination() {
        fare = calculateFare();
        if (OracleDBHandler.getInstance().markArrivalAtDestination(this)) {
            return fare;
        }
        return -1;
    }

    // Main Method for Ending Ride with Payment: Cash Mode
    public boolean endRideWithPayment(Double amountPaid, Double change)
    {
        //TODO: handle credit card payment

        payment = new Payment(amountPaid, change, getPaymentMethod());
        payment.record();

        // Add Payment to DB
        if (payment.getPaymentID() > 0) {
            return OracleDBHandler.getInstance().endRideWithPayment(this);
        }
        else {
            System.out.println("addPayment returned false!");
            return false;
        }
    }
}
