package com.savaari_demo.controllers;

import com.savaari_demo.TestJackson;
import com.savaari_demo.database.DBHandlerFactory;
import com.savaari_demo.entity.*;

public class MatchmakingController {
    // Main Attributes
    private static final String LOG_TAG = MatchmakingController.class.getSimpleName();
    TestJackson testJackson;

    // Main Constructor
    public MatchmakingController() {
        // Empty
    }

    public void setTestJackson(TestJackson testJackson) {
        this.testJackson = testJackson;
    }

    public TestJackson getJacksonTest() {
        return testJackson;
    }

    /*
     * Rider-side matchmaking method
     * Shortlists potential drivers, sends request to ideal
     * Waits for and returns response (driver accepts/declines)
     */
    public Ride searchForRide(Rider rider, Location source, Location destination, int paymentMode, RideType rideType) {
        return rider.searchForRide(source, destination, paymentMode, rideType);
    }
    // End of method:findDriver()

    /* Rider-side in-ride methods*/

    public Ride getRideForRider(Rider rider) {
        return rider.getRideForRider();
    }

    public boolean acknowledgeEndOfRide(Ride ride) {
        return ride.acknowledgeEndOfRide();
    }

    public void getRideStatus(Ride ride) {

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

    public boolean startRide(Ride ride) {
        return ride.startRide();
    }

    public double markArrivalAtDestination(Ride ride) {
        return ride.markArrivalAtDestination();
    }

    public Ride getRideForDriver(RideRequest rideRequest){
        return rideRequest.getDriver().getRideForDriver(rideRequest);
    }

    public boolean endRideWithPayment(Ride ride, Double amountPaid, Double change)
    {
        return ride.endRideWithPayment(amountPaid, change);
    }
}
