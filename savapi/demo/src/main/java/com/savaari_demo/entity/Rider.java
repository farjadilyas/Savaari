/**
 * 
 */
package com.savaari_demo.entity;

import com.savaari_demo.OracleDBHandler;

import java.util.ArrayList;

public class Rider extends User
{
    public Rider() {

    }

    // Main Methods



    /* Methods for system interactions */

    //Rider-side CRUD methods
    public Integer login() {
        return OracleDBHandler.getInstance().loginRider(this);
    }

    public boolean reset() {
        return OracleDBHandler.getInstance().resetRider(this);
    }

    public boolean fetchData() {
        return OracleDBHandler.getInstance().fetchRiderData(this);
    }

    // Rider-side Match-making methods
    public Ride getRideForRider() {
        RideRequest rideRequest = OracleDBHandler.getInstance().checkRideRequestStatus(this);

        /*
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
            result = getRide(ride);
        }
        return result;*/

        if (rideRequest == null) {
            return null;
        }

        if (rideRequest.getFindStatus() == RideRequest.FOUND) {
            return getRide(rideRequest);
        }
        else if (rideRequest.getFindStatus() == 1) {
            // Rejected
            return null
        }
        else {
            // Check if request sent
            return null;
        }
    }


    public Ride findDriver(Location source,
                                 Location destination) {

        // TODO: Policy? Where does this come from?
        int MAX_ATTEMPTS = 60, MAX_REJECTED_ATTEMPTS = 5;
        boolean requestSent = false;


        // Create RideRequest Object
        RideRequest rideRequest = new RideRequest();
        rideRequest.setRider(this);
        rideRequest.setPickupLocation(source);
        rideRequest.setDropoffLocation(destination);

        // Search for drivers that match criteria, TODO: Add criteria
        ArrayList<Driver> drivers = OracleDBHandler.getInstance().searchDriverForRide();

        // Send request to next best driver
        for (Driver currentDriver : drivers) {

            rideRequest.setDriver(currentDriver);
            requestSent = OracleDBHandler.getInstance().sendRideRequest(rideRequest);

            if (requestSent) {
                break;
            }
        }

        Ride fetchedRide = null;

        Integer findStatusResult;
        if (requestSent) {
            int attempts = 0;
            int rejectedAttempts = 0;

            while (attempts < MAX_ATTEMPTS && rejectedAttempts < MAX_REJECTED_ATTEMPTS) {

                // Check find status corresponding to previous ride request
                findStatusResult = OracleDBHandler.getInstance().checkFindStatus(this);

                // Compare return status
                if (findStatusResult == RideRequest.STATUS_ERROR) {
                    System.out.println("findDriver() : checkFindStatus() : STATUS ERROR");
                }
                else if (findStatusResult == RideRequest.NO_CHANGE) {
                    System.out.println("findDriver() : checkFindStatus() : NO_CHANGE");
                }
                else if (findStatusResult == RideRequest.NOT_PAIRED) {
                    System.out.println("findDriver() : checkFindStatus() : REJECTED. Attempting again...");
                    ++rejectedAttempts;
                }
                else if (findStatusResult== RideRequest.PAIRED) {

                    // Make a Ride object and retrieve complete ride info
                    fetchedRide = getRide(rideRequest);
                    System.out.println("findDriver() : checkFindStatus() : DRIVER FOUND!");
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

        return fetchedRide;
    }
    /* End of Rider-side Match-making methods*/


    /* Rider location methods */
    public boolean saveLocation() {
        return OracleDBHandler.getInstance().saveRiderLocation(this);
    }

    public void fetchLocation() {
        setCurrentLocation(OracleDBHandler.getInstance().getRiderLocation(this));
    }
}
