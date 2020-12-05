package com.example.savaari.ride;

import com.example.savaari.ride.entity.Payment;
import com.example.savaari.ride.entity.Vehicle;
import com.example.savaari.user.Driver;
import com.example.savaari.user.Rider;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Ride {
    public static final int
            PICKUP = 11,
            DRIVER_ARRIVED = 12,
            CANCELLED = 13,
            STARTED = 14,
            COMPLETED = 15;

    public static final int
            DEFAULT = 0,
            PAIRED = 1,
            ALREADY_PAIRED = 2,
            NOT_PAIRED = 3,
            NOT_FOUND = 4,
            STATUS_ERROR = 5;

    int rideID;
    private Rider rider;
    private Driver driver;
    private Vehicle vehicle;
    private Payment payment;
    private LatLng pickupLocation;
    private String pickupTitle;
    private LatLng dropoffLocation;
    private String dropoffTitle;
    private long startTime;
    private long endTime;
    private int rideType;
    private double estimatedFare;
    private int rideStatus;
    private int matchStatus;
    private Object paymentMethod;
    private ArrayList<LatLng> stops;

    Ride() {
        rider = new Rider();
        driver = new Driver();
        payment = new Payment();
        pickupLocation = null;
        dropoffLocation = null;
        stops = new ArrayList<>();
        matchStatus = DEFAULT;
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

    public LatLng getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(LatLng pickupLocation, String pickupTitle) {
        this.pickupLocation = pickupLocation;
    }

    public LatLng getDropoffLocation() {
        return dropoffLocation;
    }

    public void setDropoffLocation(LatLng dropoffLocation, String dropoffTitle) {
        this.dropoffLocation = dropoffLocation;
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

    public int getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(int matchStatus) {
        this.matchStatus = matchStatus;
    }

    public Object getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Object paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public ArrayList<LatLng> getStops() {
        return stops;
    }

    public void setStops(ArrayList<LatLng> stops) {
        this.stops = stops;
    }
}
