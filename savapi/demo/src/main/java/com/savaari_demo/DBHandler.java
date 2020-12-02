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
    JSONObject checkFindStatus(Rider rider);
    JSONArray searchDriverForRide();
    boolean sendRideRequest();
}
