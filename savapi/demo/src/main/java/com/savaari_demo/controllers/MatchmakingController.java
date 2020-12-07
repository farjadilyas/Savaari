package com.savaari_demo.controllers;

import com.savaari_demo.DBHandler;
import com.savaari_demo.DBHandlerFactory;
import com.savaari_demo.entity.Driver;
import com.savaari_demo.entity.Location;
import com.savaari_demo.entity.Ride;
import com.savaari_demo.entity.Rider;
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
    public JSONObject findDriver(String riderID, String sourceLatitude, String sourceLongitude,
                                 String destinationLatitude, String destinationLongitude) {

        Rider rider = new Rider();
        rider.setUserID(Integer.valueOf(riderID));

        return rider.findDriver(dbHandler,
                new Location(Double.valueOf(sourceLatitude), Double.valueOf(sourceLongitude), null),
                new Location(Double.valueOf(destinationLatitude), Double.valueOf(destinationLongitude), null));
    }
    // End of method:findDriver()


    /* Driver-side matchmaking methods */
    // Setting Driver as Active
    // TODO: Policy on how a ride is sent to driver, implemented in checkRideStatus()
    public JSONObject setMarkActive(String userID, String activeStatus)
    {
        Driver driver = new Driver();
        driver.setIsActive(Boolean.valueOf(activeStatus));
        driver.setUserID(Integer.valueOf(userID));

        if (driver.setMarkActive(dbHandler))
        {
            JSONObject json = driver.checkRideRequestStatus(dbHandler);
            if (json == null) {
                json = new JSONObject();
                json.put("STATUS", 404);
            }
            return json;
        }
        else
        {
            JSONObject json = new JSONObject();
            json.put("STATUS", 404);
            return json;
        }
    }

    public JSONObject confirmRideRequest(String userID, String found_status, String rider_id)
    {
        Driver driver = new Driver();
        Rider rider = new Rider();

        driver.setUserID(Integer.valueOf(userID));
        rider.setUserID(Integer.valueOf(rider_id));

        return driver.confirmRideRequest(dbHandler, rider, Integer.parseInt(found_status));
    }

    public JSONObject markDriverArrival(String rideID) {
        Ride ride = new Ride();
        ride.setRideID(Integer.parseInt(rideID));

        return ride.markDriverArrival(dbHandler);
    }

    public JSONObject startRideDriver(String rideID) {
        Ride ride = new Ride();
        ride.setRideID(Integer.parseInt(rideID));

        return ride.startRideDriver(dbHandler);
    }

    public JSONObject endRideDriver(String rideID, String dist_travelled) {
        Ride ride = new Ride();
        ride.setRideID(Integer.parseInt(rideID));
        ride.setDistanceTravelled(Double.parseDouble(dist_travelled));

        return ride.endRideDriver(dbHandler);
    }

    public JSONObject getRideForRider(String riderID) {
        Rider rider = new Rider();
        rider.setUserID(Integer.valueOf(riderID));

        return rider.getRideForRider(dbHandler);
    }

    /*
    public JSONObject getRide(Ride ride) {
        JSONObject result = new JSONObject();
        result = dbHandler.getRide(ride);
        result.put("IS_TAKING_RIDE", (result.getInt("STATUS_CODE") == 200));
        return result;
    }*/

    public JSONObject getRideStatus(String rideID) {
        Ride ride = new Ride();
        ride.setRideID(Integer.parseInt(rideID));
        return ride.fetchRideStatus(dbHandler);
    }

    public JSONObject getRideForDriver(String driverID){
        Driver driver = new Driver();
        driver.setUserID(Integer.valueOf(driverID));

        return driver.getRideForDriver(dbHandler);
    }
}
