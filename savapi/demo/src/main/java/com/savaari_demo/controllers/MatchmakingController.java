package com.savaari_demo.controllers;

import com.savaari_demo.entity.*;
import org.json.JSONObject;

public class MatchmakingController {
    // Main Attributes
    private static final String LOG_TAG = MatchmakingController.class.getSimpleName();

    // Main Constructor
    public MatchmakingController()
    {
        // Empty Constructor
    }

    /*
     * Rider-side matchmaking method
     * Shortlists potential drivers, sends request to ideal
     * Waits for and returns response (driver accepts/declines)
     */
    public Ride findDriver(Rider rider, Location source, Location destination) {
        return rider.findDriver(source, destination);
    }
    // End of method:findDriver()


    /* Driver-side matchmaking methods */
    // Setting Driver as Active
    // TODO: Policy on how a ride is sent to driver, implemented in checkRideStatus()
    public boolean setMarkActive(Driver driver)
    {
        return driver.setMarkActive();
    }
    public RideRequest checkRideRequestStatus(Driver driver)
    {
        return driver.checkRideRequestStatus();
    }
    public boolean confirmRideRequest(RideRequest rideRequest)
    {
        return rideRequest.getDriver().confirmRideRequest(rideRequest);
    }

    public boolean markDriverArrival(Ride ride) {
        return ride.markDriverArrival();
    }

    public boolean startRideDriver(Ride ride) {
        return ride.startRideDriver();
    }

    public double markArrivalAtDestination(Ride ride) {
        return ride.markArrivalAtDestination();
    }

    public Ride getRideForRider(Rider rider) {
        return rider.getRideForRider();
    }

    public boolean acknowledgeEndOfRide(Ride ride) {
        return ride.acknowledgeEndOfRide();
    }

    /*
    public JSONObject getRide(Ride ride) {
        JSONObject result = new JSONObject();
        result = dbHandler.getRide(ride);
        result.put("IS_TAKING_RIDE", (result.getInt("STATUS_CODE") == 200));
        return result;
    }*/

    public Integer getRideStatus(Ride ride) {

        return ride.fetchRideStatus();
    }

    public Ride getRideForDriver(RideRequest rideRequest){
        return rideRequest.getDriver().getRideForDriver(rideRequest);
    }

    public boolean endRideWithPayment(Ride ride, double amount_paid)
    {
        return ride.endRideWithPayment(amount_paid);
    }
}
