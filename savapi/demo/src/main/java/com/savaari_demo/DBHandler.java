package com.savaari_demo;

import com.savaari_demo.entity.*;

import org.json.JSONArray;
import org.json.JSONObject;

public interface DBHandler {
    Boolean addRider(Rider rider);
    Boolean addDriver(Driver driver);
    Integer loginRider(Rider rider);
    Integer loginDriver(Driver driver);
    JSONArray riderDetails();
    JSONArray driverDetails();
    Rider riderData(Rider rider);
    Driver driverData(Driver driver);
    Boolean updateRider();
    Boolean deleteRider();
    Boolean deleteDriver();

    boolean markDriverActive(Driver driver);
    Ride checkRideRequestStatus(Driver driver);
    boolean confirmRideRequest(Ride ride);

    /* Rider-side matchmaking DB calls*/
    JSONArray searchDriverForRide();
    boolean sendRideRequest(Ride rideRequest);
    JSONObject checkFindStatus(Rider rider);
    boolean recordRide(Ride ride);
    /* End of seciton*/


    /* In-ride DB calls */

    // Retrieve FIND_STATUS and DRIVER_ID
    Ride checkRideRequestStatus(Rider rider);

    //Retrieve complete ride details
    JSONObject getRide(Ride ride);

    // Retrieve STATUS from RIDES
    JSONObject getRideStatus(Ride ride);

    /* End of section*/

    boolean saveRiderLocation(Rider rider);
    boolean saveDriverLocation(Driver driver);
    Rider getRiderLocation(Rider rider);
    Driver getDriverLocation(Driver driver);
    JSONArray getRiderLocations();
    JSONArray getDriverLocations();
}
