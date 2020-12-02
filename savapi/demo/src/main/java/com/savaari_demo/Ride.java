package com.savaari_demo;

public class Ride {
    Rider rider;
    Driver driver;
    Vehicle vehicle;
    Payment payment;
    Location pickupLocation;
    Location dropoffLocation;
    String rideType;        //TODO: Decide type later (ride type class?)
    double estimatedFare;
    int rideStatus;         //TODO: Ensure it's in correct range (ride status class?)
    Object paymentMethod;   //TODO: Ensure it's one of a few types

}
