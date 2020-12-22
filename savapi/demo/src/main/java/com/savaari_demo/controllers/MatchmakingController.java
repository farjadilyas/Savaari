package com.savaari_demo.controllers;

import com.savaari_demo.OracleDBHandler;
import com.savaari_demo.entity.*;

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
    public Ride findDriver(Rider rider, Location source, Location destination, int paymentMode) {
        return rider.findDriver(source, destination, paymentMode);
    }
    // End of method:findDriver()


    /* Driver-side matchmaking methods */
    // Setting Driver as Active
    // TODO: Policy on how a ride is sent to driver, implemented in checkRideStatus()
    public boolean setMarkActive(Driver driver)
    {
        return driver.setMarkActive();
    }
    public RideRequest startMatchmaking(Driver driver)
    {
        return driver.startMatchmaking();
    }
    public RideRequest checkRideRequestStatus(Driver driver) { return driver.checkRideRequestStatus(); }

    public boolean confirmRideRequest(RideRequest rideRequest)
    {
        if (rideRequest.getFindStatus() == 1) {

            Ride ride = new Ride(rideRequest);
            return OracleDBHandler.getInstance().confirmRideRequest(ride);
        }
        else {
            // Rider: FIND_STATUS = RideRequest.REJECTED
            // Driver: RIDE_STATUS = RideRequest.MS_DEFAULT

            return OracleDBHandler.getInstance().rejectRideRequest(rideRequest);
        }
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

    public void getRideStatus(Ride ride) {

        ride.fetchRideStatus();
    }

    public Ride getRideForDriver(RideRequest rideRequest){
        return rideRequest.getDriver().getRideForDriver(rideRequest);
    }

    public boolean endRideWithPayment(Ride ride, Double amountPaid, Double change)
    {
        return ride.endRideWithPayment(amountPaid, change);
    }
}
