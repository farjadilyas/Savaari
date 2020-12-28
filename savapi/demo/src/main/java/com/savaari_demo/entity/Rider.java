/**
 * 
 */
package com.savaari_demo.entity;

import com.savaari_demo.database.DBHandlerFactory;

import java.util.ArrayList;

public class Rider extends User
{
    public Rider() {

    }

    // Main Methods



    /* Methods for system interactions */

    //Rider-side CRUD methods

    public void login() {
        setUserID(DBHandlerFactory.getInstance().createDBHandler().loginRider(this));
    }

    public boolean reset(boolean checkForResponse) {
        return DBHandlerFactory.getInstance().createDBHandler().resetRider(this, false);
    }

    public boolean fetchData() {
        return DBHandlerFactory.getInstance().createDBHandler().fetchRiderData(this);
    }

    // Rider-side Match-making methods
    public Ride getRideForRider() {
        RideRequest rideRequest = DBHandlerFactory.getInstance().createDBHandler().checkRideRequestStatus(this);

        if (rideRequest == null) {
            return null;
        }

        if (rideRequest.getFindStatus() == RideRequest.FOUND) {
            return getRide(rideRequest);
        }
        else if (rideRequest.getFindStatus() == RideRequest.REJECTED) {
            // Reset rider if previous request was rejected
            reset(false);
            return null;
        }
        else if (rideRequest.getFindStatus() == RideRequest.NO_CHANGE) {
            // TODO: Request was sent but no response, loop till timeout then reset
            return null;
        }

        return null;
    }


    public Ride searchForRide(Location source,
                              Location destination, int paymentMode, RideType rideType) {

        // TODO: Policy? Where does this come from?
        int MAX_REJECTED_ATTEMPTS = 5;
        boolean requestSent = false;


        // Create RideRequest Object
        RideRequest rideRequest = new RideRequest();
        rideRequest.setRider(this);
        rideRequest.setPickupLocation(source);
        rideRequest.setDropoffLocation(destination);
        rideRequest.setPaymentMethod(paymentMode);
        rideRequest.setRideType(rideType);

        // Search for drivers that match criteria, TODO: Add criteria
        ArrayList<Driver> drivers = DBHandlerFactory.getInstance().createDBHandler().searchDriverForRide(rideRequest);

        Ride fetchedRide = null;
        Integer findStatusResult = RideRequest.NOT_SENT;

        int attempts;
        int rejectedAttempts;

        // Send request to next best driver
        for (Driver currentDriver : drivers) {

            System.out.println("Sending ride request");

            rideRequest.setDriver(currentDriver);
            requestSent = DBHandlerFactory.getInstance().createDBHandler().sendRideRequest(rideRequest);

            if (requestSent) {
                rejectedAttempts = 0;

                // Check find status corresponding to previous ride request
                findStatusResult = DBHandlerFactory.getInstance().createDBHandler().checkFindStatus(this);

                // Compare return status
                if (findStatusResult== RideRequest.PAIRED) {

                    System.out.println("findDriver() : checkFindStatus() : DRIVER FOUND!");
                    fetchedRide = getRide(rideRequest);
                    break;
                }

                // No response, reset rider's flags
                else if (findStatusResult == RideRequest.NO_CHANGE || findStatusResult == RideRequest.STATUS_ERROR) {

                    // Reset rider's flags if no response to ride request (atomic operation)
                    if (!reset(true)) {
                        if (DBHandlerFactory.getInstance().createDBHandler().checkFindStatus(this) == RideRequest.PAIRED) {
                            System.out.println("findDriver() : checkFindStatus() : DRIVER FOUND!");
                            fetchedRide = getRide(rideRequest);
                            break;
                        }
                    }
                    else {
                        System.out.println("findDriver DRIVER NOT FOUND, trying another driver...");
                    }
                }
                else {
                    // Rejected / Not sent, reset without checking for response
                    reset(false);
                    System.out.println("findDriver DRIVER NOT FOUND, trying another driver...");
                }
            }
        }

        return fetchedRide;
    }
    /* End of Rider-side Match-making methods*/


    /* Rider location methods */
    public boolean saveLocation() {
        return DBHandlerFactory.getInstance().createDBHandler().saveRiderLocation(this);
    }

    public void fetchLocation() {
        setCurrentLocation(DBHandlerFactory.getInstance().createDBHandler().getRiderLocation(this));
    }
}
