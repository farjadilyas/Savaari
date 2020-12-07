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
    Ride checkRideRequestStatus(Driver driver);
    boolean confirmRideRequest(Ride ride);
    JSONObject markDriverArrival(Ride ride);
    JSONObject startRideDriver(Ride ride);
    JSONObject markArrivalAtDestination(Ride ride);

    /* Rider-side matchmaking DB calls*/
    ArrayList<Driver> searchDriverForRide();
    boolean sendRideRequest(Ride rideRequest);
    JSONObject checkFindStatus(Rider rider);
    boolean recordRide(Ride ride);
    Payment addPayment();

    /* In-ride DB calls */
    Ride checkRideRequestStatus(Rider rider);
    JSONObject getRide(Ride ride);
    JSONObject getRideStatus(Ride ride);
    boolean endRideWithPayment(Ride ride);
    JSONObject resetDriver(Driver driver);

    /* Location method calls */
    boolean saveRiderLocation(Rider rider);
    boolean saveDriverLocation(Driver driver);
    Location getRiderLocation(Rider rider);
    Location getDriverLocation(Driver driver);
    JSONArray getRiderLocations();
    JSONArray getDriverLocations();
}
