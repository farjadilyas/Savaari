package com.savaari_demo.controllers;

import com.savaari_demo.DBHandler;
import com.savaari_demo.DBHandlerFactory;
import com.savaari_demo.entity.*;

import org.json.JSONObject;

public class MatchmakingController {
    // Main Attributes
    private static final String LOG_TAG = MatchmakingController.class.getSimpleName();
    private static DBHandler dbHandler;

    // Main Constructor
    public MatchmakingController()
    {
        dbHandler = DBHandlerFactory.getDBHandler("Oracle");
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
        //return driver.setMarkActive();
        if (driver.setMarkActive())
        {
            JSONObject json = driver.checkRideRequestStatus();
            if (json == null) {
                return false;
            }
            return true;
        }
        else
        {
            JSONObject json = new JSONObject();
            json.put("STATUS", 404);
            return false;
        }
    }

    public JSONObject confirmRideRequest(String userID, String found_status, String rider_id)
    {
        Driver driver = new Driver();
        Rider rider = new Rider();

        driver.setUserID(Integer.parseInt(userID));
        rider.setUserID(Integer.parseInt(rider_id));

        return driver.confirmRideRequest(rider, Integer.parseInt(found_status));
    }

    public JSONObject markDriverArrival(String rideID) {
        Ride ride = new Ride();
        ride.setRideID(Integer.parseInt(rideID));

        return ride.markDriverArrival();
    }

    public JSONObject startRideDriver(String rideID) {
        Ride ride = new Ride();
        ride.setRideID(Integer.parseInt(rideID));

        return ride.startRideDriver();
    }

    public JSONObject markArrivalAtDestination(String rideID, String dist_travelled, String driver_id) {
        Ride ride = new Ride();
        ride.setRideID(Integer.parseInt(rideID));
        ride.setDistanceTravelled(Double.parseDouble(dist_travelled));

        ride.setDriver(new Driver());
        ride.getDriver().setUserID(Integer.parseInt(driver_id));

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

    public JSONObject getRideForDriver(String driverID){
        Driver driver = new Driver();
        driver.setUserID(Integer.parseInt(driverID));

        return driver.getRideForDriver();
    }

    public JSONObject endRideWithPayment(String ride_id, String amount_paid, String driver_id)
    {
        Ride ride = new Ride();
        ride.setRideID(Integer.parseInt(ride_id));

        ride.setPayment(new Payment());
        ride.getPayment().setAmountPaid(Double.parseDouble(amount_paid));

        ride.setDriver(new Driver());
        ride.getDriver().setUserID(Integer.parseInt(driver_id));

        return ride.endRideWithPayment();
    }
}
