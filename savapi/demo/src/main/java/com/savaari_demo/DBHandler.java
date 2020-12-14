package com.savaari_demo;

import com.savaari_demo.entity.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public interface DBHandler {

    /* CRUD Methods*/
    Boolean addRider(Rider rider);
    Boolean addDriver(Driver driver);
    Integer loginRider(Rider rider);
    Integer loginDriver(Driver driver);
    boolean fetchRiderData(Rider rider);
    boolean fetchDriverData(Driver driver);

    /* Unused CRUD methods */
    JSONArray riderDetails();
    JSONArray driverDetails();
    Boolean updateRider();
    Boolean deleteRider();
    Boolean deleteDriver();

    /* Driver Side Matchmaking Database Calls*/
    boolean markDriverActive(Driver driver);
    RideRequest checkRideRequestStatus(Driver driver);
    boolean confirmRideRequest(Ride ride);
    boolean markDriverArrival(Ride ride);
    boolean startRideDriver(Ride ride);
    boolean markArrivalAtDestination(Ride ride);

    /* Rider-side matchmaking DB calls*/
    ArrayList<Driver> searchDriverForRide();
    boolean sendRideRequest(RideRequest rideRequest);
    Integer checkFindStatus(Rider rider);
    boolean recordRide(Ride ride);
    Payment addPayment();

    /* In-ride DB calls */
    RideRequest checkRideRequestStatus(Rider rider);
    Ride getRide(RideRequest rideRequest);
    JSONObject getRideStatus(Ride ride);
    boolean endRideWithPayment(Ride ride);
    boolean acknowledgeEndOfRide(Ride ride);
    boolean resetDriver(Driver driver);
    boolean resetRider(Rider rider);

    /* Location method calls */
    boolean saveRiderLocation(Rider rider);
    boolean saveDriverLocation(Driver driver);
    Location getRiderLocation(Rider rider);
    Location getDriverLocation(Driver driver);
    JSONArray getRiderLocations();
    JSONArray getDriverLocations();
}
