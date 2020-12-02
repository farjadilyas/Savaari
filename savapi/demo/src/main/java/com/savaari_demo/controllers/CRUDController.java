package com.savaari_demo.controllers;

import com.savaari_demo.*;
import com.savaari_demo.entity.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class CRUDController
{
    // Main Attributes
    private static final String LOG_TAG = CRUDController.class.getSimpleName();
    private static DBHandler dbHandler;

    public CRUDController () {
        dbHandler = DBHandlerFactory.getDBHandler("Oracle");
    }

    /* User CRUD methods */

    /* Login Rider */
    public JSONObject loginRider(String emailAddress, String password) {

        Rider rider = new Rider();
        rider.setEmailAddress(emailAddress);
        rider.setPassword(password);

        System.out.println("LOGIN QUERY2: " + rider.getEmailAddress() + " " + rider.getPassword());

        JSONObject result = new JSONObject();
        Integer userID = dbHandler.loginRider(rider);

        if (userID == null) {
            result.put("STATUS_CODE", 404);
            result.put("USER_ID", -1);
        }
        else {
            result.put("STATUS_CODE", 200);
            result.put("USER_ID", userID);
        }

        return result;
    }

    // Login Driver
    public JSONObject loginDriver(String emailAddress, String password) {

        Driver driver = new Driver();
        driver.setEmailAddress(emailAddress);
        driver.setPassword(password);

        JSONObject result = new JSONObject();
        Integer userID = dbHandler.loginDriver(driver);

        if (userID == null) {
            result.put("STATUS_CODE", 404);
            result.put("USER_ID", -1);
        } else {
            result.put("STATUS_CODE", 200);
            result.put("USER_ID", userID);
        }

        return result;
    }
    // Adding a new Rider Account
    public JSONObject addRider(String username, String email_address, String password)
    {
        // TODO add logic of if Rider Exists already
        System.out.println("Username: " + username + ", password: " + password);
        Rider rider = new Rider();
        rider.setUsername(username);
        rider.setEmailAddress(email_address);
        rider.setPassword(password);

        JSONObject result = new JSONObject();
        if (dbHandler.addRider(rider)) {
            result.put("STATUS_CODE", 200);
        }
        else {
            result.put("STATUS_CODE", 404);
        }

        return result;
    }

    // Add a new inactive Driver method
    public JSONObject addDriver(String username, String email_address, String password) {

        // TODO Add logic of if Driver exists already
        Driver driver = new Driver();
        driver.setUsername(username);
        driver.setEmailAddress(email_address);
        driver.setPassword(password);

        JSONObject result = new JSONObject();
        if (dbHandler.addDriver(driver)) {
            result.put("STATUS_CODE", 200);
        }
        else {
            result.put("STATUS_CODE", 404);
        }

        return result;
    }
    public JSONArray riderDetails()
    {
        System.out.println("Rider deets called2");
        JSONArray jsonArray =  dbHandler.riderDetails();
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
        JSONArray jsonArray =  dbHandler.driverDetails();
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

    public JSONObject riderData(String userID)
    {
        Rider rider = new Rider();
        rider.setUserID(Integer.valueOf(userID));

        rider = dbHandler.riderData(rider);
        JSONObject returnObj = new JSONObject();
        if (rider != null)
        {
            returnObj.put("STATUS", 200);
            returnObj.put("USER_NAME", rider.getUsername());
            returnObj.put("EMAIL_ADDRESS", rider.getEmailAddress());

        }
        else
        {
            returnObj.put("STATUS", 404);
        }
        return returnObj;
    }

    public JSONObject driverData(String userID) {
        Driver driver = new Driver();
        driver.setUserID(Integer.valueOf(userID));

        driver = dbHandler.driverData(driver);
        JSONObject returnObj = new JSONObject();

        if (driver != null)
        {
            returnObj.put("STATUS", 200);
            returnObj.put("USER_NAME", driver.getUsername());
            returnObj.put("EMAIL_ADDRESS", driver.getEmailAddress());
        }
        else
        {
            returnObj.put("STATUS", 404);
        }
        return returnObj;
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
}
