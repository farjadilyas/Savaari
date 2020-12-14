package com.savaari_demo.controllers;

import com.savaari_demo.DBHandler;
import com.savaari_demo.DBHandlerFactory;
import com.savaari_demo.OracleDBHandler;
import com.savaari_demo.entity.Driver;
import com.savaari_demo.entity.Location;
import com.savaari_demo.entity.Rider;

import org.json.JSONArray;
import org.json.JSONObject;

public class LocationController
{
    // Main Attributes
    private static final String LOG_TAG = LocationController.class.getSimpleName();
    private static DBHandler dbHandler = DBHandlerFactory.getDBHandler("Oracle");

    /* Location update & retrieval methods*/


    public boolean saveRiderLocation(Rider rider) {
        return rider.saveLocation();
    }

    public boolean saveDriverLocations(String driverID, String latitude, String longitude, String timestamp) {

        // Package parameters into Rider instance
        Driver driver = new Driver();
        driver.setUserID(Integer.parseInt(driverID));
        driver.setCurrentLocation(new Location(Double.valueOf(latitude), Double.valueOf(longitude),
                Long.parseLong(timestamp)));

        return driver.saveDriverLocation();
    }

    public JSONObject getDriverLocation(String driverID) {
        Driver driver = new Driver();
        driver.setUserID(Integer.parseInt(driverID));

        return driver.getDriverLocation();
    }

    public void getRiderLocation(Rider rider) {
        rider.fetchLocation();
    }

    public JSONArray getDriverLocations() {
        return OracleDBHandler.getInstance().getDriverLocations();
    }
    public JSONArray getRiderLocations() {
        return OracleDBHandler.getInstance().getRiderLocations();
    }
    /* End of section */
}
