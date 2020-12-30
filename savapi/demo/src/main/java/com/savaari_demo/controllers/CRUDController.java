package com.savaari_demo.controllers;

import com.savaari_demo.database.DBHandlerFactory;
import com.savaari_demo.entity.Driver;
import com.savaari_demo.entity.Rider;
import com.savaari_demo.entity.User;
import com.savaari_demo.entity.Vehicle;
import org.json.JSONObject;

public class CRUDController
{
    // Main Attributes
    private static final String LOG_TAG = CRUDController.class.getSimpleName();
    Driver driver;
    Rider rider;

    public CRUDController ()
    {

    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    /* User CRUD methods */

    /* Login Rider */
    public Integer loginRider(Rider rider) {
        rider.login();
        this.rider = rider;
        return this.rider.getUserID();
    }

    // Login Driver
    public Integer loginDriver(Driver driver) {
        driver.login();
        this.driver = driver;
        return this.driver.getUserID();
    }

    public void persistDriverLogin(Driver driver) {
        this.driver = driver;
    }

    public void persistRiderLogin(Rider rider) {
        this.rider = rider;
    }
    // Adding a new Rider Account
    public boolean addRider(String username, String email_address, String password)
    {
        return DBHandlerFactory.getInstance().createDBHandler().addRider(username, email_address,
                User.hashPassword(password));
    }

    // Add a new inactive Driver method
    public boolean addDriver(String username, String email_address, String password) {
        return DBHandlerFactory.getInstance().createDBHandler().addDriver(username, email_address,
                User.hashPassword(password));
    }


    public Rider riderData() {
        if (rider.fetchData()) {
            return rider;
        }
        else {
            return null;
        }
    }

    public Driver driverData() {
        if (driver.fetchData()) {
            return driver;
        }
        return null;
    }

    public boolean setMarkActive(boolean activeStatus)
    {
        driver.setActive(activeStatus);
        return driver.setMarkActive();
    }


    // TODO Handle them in API
    public JSONObject updateRide() {
        return null;
    }

    public JSONObject deleteRider() {
        return null;
    }

    public JSONObject deleteDriver() {
        return null;
    }
    /* End of section */

    /* Vehicle methods */

    public boolean setActiveVehicle(Vehicle vehicle) {
        return driver.selectActiveVehicle(vehicle);
    }

    public boolean registerDriver(Driver driver) {
        this.driver.setRegistrationDetails(driver.getFirstName(),
                driver.getLastName(),
                driver.getPhoneNo(),
                driver.getCNIC(),
                driver.getLicenseNumber());

        return driver.sendRegistrationRequest();
    }

    public boolean sendVehicleRegistrationRequest(Vehicle vehicle) {
        return driver.sendVehicleRegistrationRequest(vehicle);
    }
}
