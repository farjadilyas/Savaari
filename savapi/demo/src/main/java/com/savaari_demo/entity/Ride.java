package com.savaari_demo.entity;

import com.savaari_demo.DBHandler;
import org.json.JSONObject;

import java.sql.Timestamp;

public class Ride {

    // Main Attributes
    private int rideID;
    private Rider rider;
    private Driver driver;
    private Vehicle vehicle;
    private Payment payment;
    private Location pickupLocation;
    private Location dropoffLocation;
    private Timestamp startTime;
    private Timestamp endTime;
    private double distanceTravelled;
    private int rideType;           //TODO: Decide type later (ride type class?)
    private double estimatedFare;
    private double fare;
    private int rideStatus;         //TODO: Ensure it's in correct range (ride status class?)
    private int findStatus;
    private Object paymentMethod;   //TODO: Ensure it's one of a few types

    // ---------------------------------------------------------------------------------
    //                          GETTER and SETTER
    // ---------------------------------------------------------------------------------
    public Timestamp getStartTime() {
        return startTime;
    }
    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }
    public Timestamp getEndTime() {
        return endTime;
    }
    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }
    public void setRideID(int rideID) { this.rideID = rideID; }
    public int getRideID() { return rideID; }
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
    public Object getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(Object paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public int getFindStatus()
    {
        return findStatus;
    }
    public void setFindStatus(int findStatus) {
        this.findStatus = findStatus;
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

    // ---------------------------------------------------------------------------------
    // PRIVATE METHODS for POLICIES
    private double calculateFare()
    {
        // TODO: FARE POLICY and stuff ?
        return 0.0;
    }

    // ---------------------------------------------------------------------------------
    //                          System interaction methods
    // ---------------------------------------------------------------------------------
    public JSONObject fetchRideStatus(DBHandler dbHandler) {
        return dbHandler.getRideStatus(this);
    }
    public JSONObject markDriverArrival(DBHandler dbHandler) {
        return dbHandler.markDriverArrival(this);
    }
    public JSONObject startRideDriver(DBHandler dbHandler) { return dbHandler.startRideDriver(this); }
    
    // Main End Ride with Driver Method
    public JSONObject endRideDriver(DBHandler dbHandler) { 

        JSONObject jsonObject = dbHandler.endRideDriver(this);

        if (jsonObject.getInt("STATUS") == 200) {
            fare = calculateFare();
            jsonObject.put("FARE", fare);
        }
        return jsonObject;
    }
}
