package com.savaari_demo.controllers;

import com.savaari_demo.DBHandlerFactory;
import com.savaari_demo.entity.Driver;
import com.savaari_demo.entity.Location;
import com.savaari_demo.entity.Rider;

import java.util.ArrayList;

public class LocationController
{
    // Main Attributes
    private static final String LOG_TAG = LocationController.class.getSimpleName();

    /* Location update & retrieval methods*/


    public boolean saveRiderLocation(Rider rider) {
        return rider.saveLocation();
    }

    public boolean saveDriverLocation(Driver driver) {

        return driver.saveDriverLocation();
    }

    public void getDriverLocation(Driver driver) {
        driver.getDriverLocation();
    }

    public void getRiderLocation(Rider rider) {
        rider.fetchLocation();
    }

    public ArrayList<Location> getDriverLocations() {
        return DBHandlerFactory.getInstance().createDBHandler().getDriverLocations();
    }
    public ArrayList<Location> getRiderLocations() {
        return DBHandlerFactory.getInstance().createDBHandler().getRiderLocations();
    }
    /* End of section */
}
