package com.savaari_demo.controllers;

import com.savaari_demo.DBHandler;
import com.savaari_demo.DBHandlerFactory;
import com.savaari_demo.entity.Driver;
import com.savaari_demo.entity.Location;
import com.savaari_demo.entity.Rider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;

public class LocationController
{
    // Main Attributes
    private static final String LOG_TAG = LocationController.class.getSimpleName();
    private static DBHandler dbHandler = DBHandlerFactory.getDBHandler("Oracle");

    /* Location update & retrieval methods*/

    public boolean saveRiderLocations(String riderID, String latitude, String longitude, String timestamp) {

        // Package parameters into Rider instance
        Rider rider = new Rider();
        rider.setUserID(Integer.valueOf(riderID));
        rider.setLastLocation(new Location(Double.valueOf(latitude), Double.valueOf(longitude),
                new Timestamp(Long.valueOf(timestamp))));

        return dbHandler.saveRiderLocation(rider);
    }

    public boolean saveDriverLocations(String driverID, String latitude, String longitude, String timestamp) {

        // Package parameters into Rider instance
        Driver driver = new Driver();
        driver.setUserID(Integer.valueOf(driverID));
        driver.setLastLocation(new Location(Double.valueOf(latitude), Double.valueOf(longitude),
                new Timestamp(Long.valueOf(timestamp))));

        return dbHandler.saveDriverLocation(driver);
    }

    public JSONObject getDriverLocation(String driverID) {
        Driver driver = new Driver();
        driver.setUserID(Integer.valueOf(driverID));

        driver = dbHandler.getDriverLocation(driver);
        JSONObject result = new JSONObject();

        if (driver == null) {
            result.put("STATUS_CODE", 404);
        }
        else {
            result.put("STATUS_CODE", 200);
            result.put("LATITUDE", driver.getLastLocation().getLatitude());
            result.put("LONGITUDE", driver.getLastLocation().getLongitude());
        }
        return result;
    }

    public JSONObject getRiderLocation(String riderID) {
        Rider rider = new Rider();
        rider.setUserID(Integer.valueOf(riderID));

        rider = dbHandler.getRiderLocation(rider);
        JSONObject result = new JSONObject();

        if (rider == null) {
            result.put("STATUS_CODE", 404);
        }
        else {
            result.put("STATUS_CODE", 200);
            result.put("LATITUDE", rider.getLastLocation().getLatitude());
            result.put("LONGITUDE", rider.getLastLocation().getLongitude());
        }
        return result;
    }

    public JSONArray getDriverLocations() {
        return dbHandler.getDriverLocations();
    }
    public JSONArray getRiderLocations() {
        return dbHandler.getRiderLocations();
    }
    /* End of section */
}
