/**
 * 
 */
package com.savaari_demo.entity;

import com.savaari_demo.DBHandler;
import org.json.JSONObject;

import java.util.ArrayList;

public class Rider extends User
{
	// Main Attributes
    int findStatus;

    // Main Methods
    public int getFindStatus() {
        return findStatus;
    }

    public void setFindStatus(int findStatus) {
        this.findStatus = findStatus;
    }


    /* Methods for system interactions */

    //Rider-side CRUD methods
    public boolean reset(DBHandler dbHandler) {
        return dbHandler.resetRider(this);
    }

    public boolean fetchData(DBHandler dbHandler) {

        return dbHandler.fetchRiderData(this);
    }

    // Rider-side Match-making methods
    public JSONObject getRideForRider(DBHandler dbHandler) {
        Ride ride = dbHandler.checkRideRequestStatus(this);

        JSONObject result = new JSONObject();

        if (ride == null) {
            result.put("STATUS_CODE", 404);
            result.put("IS_TAKING_RIDE", false);
        }
        else if (ride.getRider().getFindStatus() != 2) {
            result.put("STATUS_CODE", 200);
            result.put("IS_TAKING_RIDE", false);
        }
        else {
            result = getRide(dbHandler, ride);
        }

        return result;
    }


    public JSONObject findDriver(DBHandler dbHandler, Location source,
                                 Location destination) {

        //TODO: Policy? Where does this come from?
        int MAX_ATTEMPTS = 60, MAX_REJECTED_ATTEMPTS = 5;
        boolean requestSent = false, driverPaired = false;


        // Create Ride Object
        Ride ride = new Ride();
        ride.setRider(this);

        // Package param into Ride Object
        ride.setPickupLocation(source);
        ride.setDropoffLocation(destination);

        // Search for drivers that match criteria, TODO: Add criteria
        ArrayList<Driver> drivers = dbHandler.searchDriverForRide();

        // Send request to next best driver
        for (Driver currentDriver : drivers) {

            ride.setDriver(currentDriver);
            requestSent = dbHandler.sendRideRequest(ride);

            if (requestSent) {
                break;
            }
        }

        JSONObject findStatusResult = new JSONObject();
        if (requestSent) {
            int attempts = 0;
            int rejectedAttempts = 0;

            while (attempts < MAX_ATTEMPTS && rejectedAttempts < MAX_REJECTED_ATTEMPTS) {

                // Check find status corresponding to previous ride request
                findStatusResult = dbHandler.checkFindStatus(this);

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

                    // Make a Ride object and retrieve complete ride info
                    ride.setDriver(new Driver());
                    ride.getDriver().setUserID(findStatusResult.getInt("DRIVER_ID"));
                    findStatusResult = getRide(dbHandler, ride);

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
                    System.out.println("Rider: findDriver: Thread.sleep() exception");
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
    /* End of Rider-side Match-making methods*/


    /* Rider location methods */
    public boolean saveLocation(DBHandler dbHandler) {
        return dbHandler.saveRiderLocation(this);
    }

    public void fetchLocation(DBHandler dbHandler) {
        setLastLocation(dbHandler.getRiderLocation(this));
    }
}
