package com.savaari_demo;

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
    JSONObject checkFindStatus(Rider rider);
    Ride checkRideStatus(Driver driver);
    boolean confirmRideRequest(Ride ride);
    JSONArray searchDriverForRide();
    boolean sendRideRequest(Ride rideRequest);

    boolean saveRiderLocation(Rider rider);
    boolean saveDriverLocation(Driver driver);
    Rider getRiderLocation(Rider rider);
    Driver getDriverLocation(Driver driver);
    JSONArray getRiderLocations();
    JSONArray getDriverLocations();
}
