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
    private int rideType;
    private double estimatedFare;
    private double fare;
    private int rideStatus;
    private Integer paymentMethod; // TODO : Move Proper attributes to Ride Request
    private ArrayList<Location> stops;

    public Ride() {
        rider = new Rider();
        driver = new Driver();
        //vehicle = new Vehicle();
        payment = new Payment();
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
    public int getRideType() {
        return rideType;
    }
    public void setRideType(int rideType) {
        this.rideType = rideType;
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

    public void setPaymentMethod(Integer paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Integer getPaymentMethod() {
        return paymentMethod;
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
        return 2150.89;
    }

    // ---------------------------------------------------------------------------------
    //                          System interaction methods
    // ---------------------------------------------------------------------------------
    public Integer fetchRideStatus() {
        return OracleDBHandler.getInstance().getRideStatus(this);
    }
    public boolean markDriverArrival() {
        return OracleDBHandler.getInstance().markDriverArrival(this);
    }
    public boolean startRideDriver() { return OracleDBHandler.getInstance().startRideDriver(this); }


    // Acknowledge end of ride (final call)
    public boolean acknowledgeEndOfRide() {

        if (OracleDBHandler.getInstance().acknowledgeEndOfRide(this)) {
            return rider.reset();
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
    public boolean endRideWithPayment(double amountPaid)
    {
        // TODO: Payment policy, where does payment sheningans happen?
        payment = new Payment();
        payment.setAmountPaid(amountPaid);
        return (driver.resetDriver() && OracleDBHandler.getInstance().endRideWithPayment(this));
    }

    public boolean recordRide() {

        Payment newPayment = OracleDBHandler.getInstance().addPayment();

        if (newPayment == null) {
            System.out.println("Ride" + ":recordRide: payment is null");
            return false;
        }
        else {
            // Payment created, set it in ride & record ride
            setPayment(newPayment);
            OracleDBHandler.getInstance().recordRide(this);
            return true;
        }
    }
}
