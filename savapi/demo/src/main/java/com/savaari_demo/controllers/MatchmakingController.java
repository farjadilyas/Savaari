package com.savaari_demo.controllers;

import com.savaari_demo.database.DBHandlerFactory;
import com.savaari_demo.entity.*;

public class MatchmakingController {
    // Main Attributes
    private static final String LOG_TAG = MatchmakingController.class.getSimpleName();
    Ride ride;
    RideRequest rideRequest;

    // Main Constructor
    public MatchmakingController() {
        // Empty
    }

    public Ride getRide() {
        return ride;
    }

    /*
     * Rider-side matchmaking method
     * Shortlists potential drivers, sends request to ideal
     * Waits for and returns response (driver accepts/declines)
     */
    public Ride searchForRide(Rider rider, Location source, Location destination, int paymentMode, RideType rideType) {
        ride = rider.searchForRide(source, destination, paymentMode, rideType);
        return ride;
    }
    // End of method:findDriver()

    /* Rider-side in-ride methods*/

    public Ride getRideForRider(Rider rider) {
        ride = rider.getRideForRider();
        return ride;
    }

    public boolean acknowledgeEndOfRide() {
        return ride.acknowledgeEndOfRide();
    }

    public void getRideStatus() {
        ride.fetchRideStatus();
    }

    public boolean giveFeedbackForDriver(Ride ride, float rating) {
        return ride.giveFeedbackForDriver(rating);
    }

    public boolean giveFeedbackForRider(Ride ride, float rating) {
        return ride.giveFeedbackForRider(rating);
    }
    /* End of section */



    /* Driver-side matchmaking methods */
    // Setting Driver as Active
    // TODO: Policy on how a ride is sent to driver, implemented in checkRideStatus()
    public RideRequest startMatchmaking(Driver driver)
    {
        ride.getRideParameters().setDriver(driver);
        return ride.getRideParameters().getDriver().startMatchmaking();
    }
    public RideRequest checkRideRequestStatus() {
        return ride.getRideParameters().getDriver().checkRideRequestStatus();
    }

    public boolean confirmRideRequest(int found_status)
    {
        if (found_status == 1) {
            ride = new Ride(ride.getRideParameters());
            return DBHandlerFactory.getInstance().createDBHandler().confirmRideRequest(ride);
        }
        else {
            // Rider: FIND_STATUS = RideRequest.REJECTED
            // Driver: RIDE_STATUS = RideRequest.MS_DEFAULT
            return DBHandlerFactory.getInstance().createDBHandler().rejectRideRequest(rideRequest);
        }
    }

    public boolean markArrivalAtPickup(Ride ride) {
        return ride.markDriverArrival();
    }

    public boolean startRide() {
        return ride.startRide();
    }

    public double markArrivalAtDestination(long endTime, double distanceTravelled) {
        ride.setEndTime(endTime);
        ride.setDistanceTravelled(distanceTravelled);
        return ride.markArrivalAtDestination();
    }

    public Ride getRideForDriver() {
        return ride.getRideParameters().getDriver().getRideForDriver(ride.getRideParameters());
    }

    public boolean endRideWithPayment(Double amountPaid, Double change)
    {
        return ride.endRideWithPayment(amountPaid, change);
    }
}
