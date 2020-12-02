package com.savaari_demo;

import org.json.JSONArray;
import org.json.JSONObject;

public class Controller
{
    // Main Attributes
    private static DBHandler dbHandler;

    // Main Constructor
    public Controller()
    {
        dbHandler = new DBHandlerFactory().getDBHandler("Oracle");
    }


    /* Networking Operation Methods */
    public JSONObject addRider(String username, String email_address, String password)
    {
        Rider rider = new Rider();
        rider.setUsername(username);
        rider.setEmailAddress(email_address);
        rider.setPassword(password);

        JSONObject result = new JSONObject();
        if (dbHandler.addRider(rider)) {
            result.append("STATUS", 200);
        }
        else {
            result.append("STATUS", 404);
        }

        return result;
    }

    // Add a new inactive Driver method
    public JSONObject addDriver(String username, String email_address, String password) {

        Driver driver = new Driver();
        driver.setUsername(username);
        driver.setEmailAddress(email_address);
        driver.setPassword(password);

        JSONObject result = new JSONObject();
        if (dbHandler.addDriver(driver)) {
            result.append("STATUS", 200);
        }
        else {
            result.append("STATUS", 404);
        }

        return result;
    }

    public JSONObject loginRider(String emailAddress, String password) {

        Rider rider = new Rider();
        rider.setEmailAddress(emailAddress);
        rider.setPassword(password);

        JSONObject result = new JSONObject();
        result.append("USER_ID", dbHandler.loginRider(rider));

        return result;
    }

    public JSONObject loginDriver(String emailAddress, String password) {

        Driver driver = new Driver();
        driver.setEmailAddress(emailAddress);
        driver.setPassword(password);

        JSONObject result = new JSONObject();
        result.append("USER_ID", dbHandler.loginDriver(driver));

        return result;
    }

    public JSONArray riderDetails() { return dbHandler.riderDetails(); }

    public JSONArray driverDetails() { return dbHandler.driverDetails(); }

    public JSONObject riderData() {
        return null;
    }

    public JSONObject driverData() {
        return null;
    }

    public JSONObject updateRide() {
        return null;
    }

    public JSONObject deleteRider() {
        return null;
    }

    public JSONObject deleteDriver() {
        return null;
    }

    public JSONObject checkFindStatus(int riderID) {
        Rider rider = new Rider();
        rider.setUserID(riderID);
        return dbHandler.checkFindStatus(rider);
    }

    /*
    * Rider-side matchmaking method
    * Shortlists potential drivers, sends request to ideal
    * Waits for and returns response (driver accepts/declines)
    */
    public JSONObject findDriver(int userID, double latitude, double longitude) {

        //TODO: Policy? Where does this come from?
        int MAX_ATTEMPTS = 60, MAX_REJECTED_ATTEMPTS = 5;
        boolean requestSent = false, driverPaired = false;

        // Search for drivers that match criteria, TODO: Add criteria
        JSONArray drivers = dbHandler.searchDriverForRide();

        // Send request to next best driver
        int driverCount = drivers.length();
        for (int driverOption = 0 ; driverOption < driverCount && !requestSent; ++driverOption) {

            ((JSONObject) drivers.get(driverOption)).get("USER_ID");

            //TODO: Decide and send param for DBHandler.sendRideRequest()
            requestSent = dbHandler.sendRideRequest();
        }

        JSONObject findStatusResult = new JSONObject();
        if (requestSent) {
            int attempts = 0;
            int rejectedAttempts = 0;

            //TODO: Move this to a separate thread
            while (attempts < MAX_ATTEMPTS && rejectedAttempts < MAX_REJECTED_ATTEMPTS) {

                // Check find status corresponding to previous ride request
                findStatusResult = checkFindStatus(userID);

                // Compare return status
                if (findStatusResult.get("STATUS") == "ERROR") {
                    System.out.println("findDriver() : checkFindStatus() : STATUS ERROR");
                }
                else if (findStatusResult.get("STATUS") == "NO_CHANGE") {
                    System.out.println("findDriver() : checkFindStatus() : NO_CHANGE");
                }
                else if (findStatusResult.get("STATUS") == "REJECTED") {
                    System.out.println("findDriver() : checkFindStatus() : REJECTED. Attempting again...");
                            ++rejectedAttempts;
                }
                else if (findStatusResult.get("STATUS") == "FOUND") {
                    System.out.println("findDriver() : checkFindStatus() : DRIVER FOUND!");
                    driverPaired = true;
                    break;
                }
                else {
                    System.out.println("findDriver STATUS UNDEFINED ERROR");
                }

                ++attempts;
            }
        }

        if (driverPaired) {
            findStatusResult.put("STATUS", "FOUND");
        }
        else {
            findStatusResult.put("STATUS", "NOT_FOUND");
        }

        return findStatusResult;
    }
    // End of method:findDriver()


    public JSONObject checkRideStatus() {
        return null;
    }

    public JSONObject confirmRideRequest() {
        return null;
    }

    public void saveRiderLocation() {

    }

    public void saveDriverLocation() {

    }

    public JSONObject getDriverLocation() {
        return null;
    }
    public JSONObject getRiderLocation() {
        return null;
    }

}
