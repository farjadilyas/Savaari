package com.savaari_demo.controllers;

import com.savaari_demo.DBHandlerFactory;
import com.savaari_demo.entity.Driver;
import com.savaari_demo.entity.Rider;

import org.json.JSONArray;
import org.json.JSONObject;

public class CRUDController
{
    // Main Attributes
    private static final String LOG_TAG = CRUDController.class.getSimpleName();

    public CRUDController () {
    }

    /* User CRUD methods */

    /* Login Rider */
    public Integer loginRider(Rider rider) {
        return rider.login();
    }

    // Login Driver
    public Integer loginDriver(Driver driver) {
        return driver.login();
    }
    // Adding a new Rider Account
    public boolean addRider(String username, String email_address, String password)
    {
        // TODO add logic of if Rider Exists already
        Rider rider = new Rider();
        rider.setUsername(username);
        rider.setEmailAddress(email_address);
        rider.setPassword(password);

        return rider.signup();
    }

    // Add a new inactive Driver method
    public boolean addDriver(String username, String email_address, String password) {

        // TODO Add logic of if Driver exists already
        Driver driver = new Driver();
        driver.setUsername(username);
        driver.setEmailAddress(email_address);
        driver.setPassword(password);

        return driver.signup();
    }


    public JSONArray riderDetails()
    {
        JSONArray jsonArray =  DBHandlerFactory.getInstance().createDBHandler().riderDetails();
        if (jsonArray != null)
        {
            return jsonArray;
        }
        else
        {
            JSONArray temp = new JSONArray();
            JSONObject temp1 = new JSONObject();
            temp1.put("STATUS", 404);
            temp.put(temp1);
            return temp;
        }
    }

    public JSONArray driverDetails()
    {
        JSONArray jsonArray =  DBHandlerFactory.getInstance().createDBHandler().driverDetails();
        if (jsonArray != null)
        {
            return jsonArray;
        }
        else
        {
            JSONArray temp = new JSONArray();
            JSONObject temp1 = new JSONObject();
            temp1.put("STATUS", 404);
            temp.put(temp1);
            return temp;
        }
    }


    public boolean riderData(Rider rider) {
        return rider.fetchData();
    }

    public boolean driverData(Driver driver) {
        return driver.fetchData();
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

    public boolean setActiveVehicle(Driver driver) {
        return driver.setActiveVehicle();
    }

    public boolean respondToVehicleRequest(Driver driver) {
        return driver.respondToVehicleRequest();
    }

    public boolean registerDriver(Driver driver) {
        return driver.sendRegistrationRequest();
    }

    public boolean sendVehicleRequest(Driver driver) {
        return driver.sendVehicleRequest();
    }
    public boolean respondToDriverRequest(Driver driver)
    {
        return DBHandlerFactory.getInstance().createDBHandler().respondToDriverRequest(driver);
    }
}
