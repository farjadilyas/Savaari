package com.savaari_demo.database;

import com.savaari_demo.entity.*;

import org.json.JSONArray;

import java.util.ArrayList;

public interface DBHandler {

    boolean loadRideTypes(ArrayList<RideType> rideTypes);

    /* CRUD Methods*/
    Boolean addRider(String username, String emailAddress, String password);
    Boolean addDriver(String username, String emailAddress, String password);
    Integer loginRider(Rider rider);
    int loginDriver(Driver driver);
    boolean fetchRiderData(Rider rider);
    boolean fetchDriverData(Driver driver);

    /* Registration Methods */
    boolean sendRegistrationRequest(Driver driver);

    /*Driver-Vehicle methods*/
    boolean sendVehicleRegistrationRequest(Driver driver, Vehicle currentVehicleRequest);
    boolean respondToVehicleRegistrationRequest(Vehicle currentVehicleRequest);
    boolean setActiveVehicle(Driver driver);

    /* Unused CRUD methods */
    JSONArray riderDetails();
    JSONArray driverDetails();
    Boolean updateRider();
    Boolean deleteRider();
    Boolean deleteDriver();

    /* Driver Side Matchmaking Database Calls*/
    boolean markDriverActive(Driver driver);
    RideRequest checkRideRequestStatus(Driver driver, int timeout);
    boolean rejectRideRequest(RideRequest rideRequest);
    boolean confirmRideRequest(Ride ride);
    boolean markDriverArrival(Ride ride);
    boolean startRide(Ride ride);
    boolean markArrivalAtDestination(Ride ride);

    /* Rider-side matchmaking DB calls*/
    ArrayList<Driver> searchDriverForRide(RideRequest rideRequest);
    boolean sendRideRequest(RideRequest rideRequest);
    Integer checkFindStatus(Rider rider);
    boolean recordRide(Ride ride);
    void recordPayment(Payment payment);

    /* In-ride DB calls */
    RideRequest checkRideRequestStatus(Rider rider);
    Ride getRide(RideRequest rideRequest);
    void getRideStatus(Ride ride);
    boolean endRideWithPayment(Ride ride);
    boolean acknowledgeEndOfRide(Ride ride);
    boolean resetDriver(Driver driver);
    boolean resetRider(Rider rider, boolean checkForResponse);
    boolean giveFeedbackForDriver(Ride ride, float rating);
    boolean giveFeedbackForRider(Ride ride, float rating);

    /* Location method calls */
    boolean saveRiderLocation(Rider rider);
    boolean saveDriverLocation(Driver driver);
    Location getRiderLocation(Rider rider);
    Location getDriverLocation(Driver driver);
    ArrayList<Location> getRiderLocations();
    ArrayList<Location> getDriverLocations();

    boolean respondToDriverRegistrationRequest(Driver driver);

    boolean addAdmin(Administrator admin);
    boolean loginAdmin(Administrator admin);
    ArrayList<Vehicle> getVehicleRequests();
    ArrayList<Driver> getDriverRequests();
}
