package com.savaari_demo.controllers;

import com.savaari_demo.DBHandler;
import com.savaari_demo.DBHandlerFactory;
import com.savaari_demo.entity.Driver;
import com.savaari_demo.entity.Location;
import com.savaari_demo.entity.Ride;
import com.savaari_demo.entity.Rider;
import org.json.JSONArray;
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

        //TODO: Policy? Where does this come from?
        int MAX_ATTEMPTS = 60, MAX_REJECTED_ATTEMPTS = 5;
        boolean requestSent = false, driverPaired = false;


        // Create Ride Object
        Ride ride = new Ride();
        ride.setRider(new Rider());
        ride.setDriver(new Driver());

        // Package param into Ride Object
        ride.getRider().setUserID(Integer.valueOf(riderID));
        ride.setPickupLocation(new Location(Double.valueOf(sourceLatitude), Double.valueOf(sourceLongitude), null));
        ride.setDropoffLocation(new Location(Double.valueOf(destinationLatitude), Double.valueOf(destinationLongitude), null));


        // Search for drivers that match criteria, TODO: Add criteria
        JSONArray drivers = dbHandler.searchDriverForRide();

        // Send request to next best driver
        int driverCount = drivers.length();
        for (int driverOption = 0 ; driverOption < driverCount && !requestSent; ++driverOption) {

            ride.getDriver().setUserID(((JSONObject) drivers.get(driverOption)).getInt("USER_ID"));

            requestSent = dbHandler.sendRideRequest(ride);
        }

        JSONObject findStatusResult = new JSONObject();
        if (requestSent) {
            int attempts = 0;
            int rejectedAttempts = 0;

            while (attempts < MAX_ATTEMPTS && rejectedAttempts < MAX_REJECTED_ATTEMPTS) {

                // Check find status corresponding to previous ride request
                findStatusResult = dbHandler.checkFindStatus(ride.getRider());

                // Compare return status
                if (findStatusResult.get("STATUS") == "ERROR") {
                    System.out.println("findDriver() : checkFindStatus() : STATUS ERROR");
                }
                else if (findStatusResult.get("STATUS") == "NO_CHANGE") {
                    System.out.println("findDriver() : checkFindStatus() : NO_CHANGE");
                }
                else if (findStatusResult.get("STATUS") == "REJECTED") {
                    System.out.println("findDriver() : checkFindStatus() : REJECTED. Attempting again...");
                    ++rejectedAttempts;
                }
                else if (findStatusResult.get("STATUS") == "FOUND") {
                    System.out.println("findDriver() : checkFindStatus() : DRIVER FOUND!");
                    driverPaired = true;
                    break;
                }
                else {
                    System.out.println("findDriver STATUS UNDEFINED ERROR");
                }
                ++attempts;

                try {
                    Thread.sleep(2000);
                }
                catch (Exception e) {
                    System.out.println(LOG_TAG + "findDriver: Thread.sleep() exception");
                }

            }
        }

        if (driverPaired) {
            findStatusResult.put("STATUS", "FOUND");
        }
        else {
            findStatusResult.put("STATUS", "NOT_FOUND");
        }

        return findStatusResult;
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

        if (dbHandler.markDriverActive(driver))
        {
            JSONObject json = checkRideStatus(driver);
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

    private JSONObject checkRideStatus(Driver driver)
    {
        // TODO: Implement policy of checking rides
        while(true)
        {
            Ride ride = dbHandler.checkRideStatus(driver);
            if (ride != null)
            {
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("STATUS", 200);
                jsonObject.put("USER_NAME", ride.getRider().getUsername());
                jsonObject.put("RIDER_ID", ride.getRider().getUserID());
                jsonObject.put("SOURCE_LAT", ride.getPickupLocation().getLatitude());
                jsonObject.put("SOURCE_LONG", ride.getPickupLocation().getLongitude());
                jsonObject.put("DEST_LAT", ride.getDropoffLocation().getLatitude());
                jsonObject.put("DEST_LONG", ride.getDropoffLocation().getLongitude());

                return jsonObject;
            }
            else
            {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    System.out.println("checkRideStatus(): Exception thrown at sleep()");
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }

    public JSONObject confirmRideRequest(String userID, String found_status, String rider_id) {
        Ride ride = new Ride();
        Driver driver = new Driver();
        Rider rider = new Rider();

        driver.setUserID(Integer.valueOf(userID));
        rider.setUserID(Integer.valueOf(rider_id));

        ride.setDriver(driver);
        ride.setRider(rider);
        ride.setRideStatus(Integer.parseInt(found_status) == 1 ? 2 : 0);
        ride.setFindStatus(Integer.parseInt(found_status) + 1);

        JSONObject jsonObject = new JSONObject();
        if (dbHandler.confirmRideRequest(ride)) {
            jsonObject.put("STATUS", 200);

            // Record Ride
            dbHandler.recordRide(ride);
        } else {
            jsonObject.put("STATUS", 404);
        }
        return jsonObject;
    }

    //TODO: Check if already taking ride, and return ride object
    //TODO: Check ride status continuously (cancelled? arrived at pickup? arrived at dest?)
}
