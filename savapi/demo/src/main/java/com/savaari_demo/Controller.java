package com.savaari_demo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;

public class Controller
{
    // Main Attributes
    private static final String LOG_TAG = Controller.class.getSimpleName();
    private static DBHandler dbHandler;

    // Main Constructor
    public Controller()
    {
        dbHandler = new DBHandlerFactory().getDBHandler("Oracle");
    }



    /* User CRUD methods */
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

    public JSONObject loginDriver(String emailAddress, String password) {

        Driver driver = new Driver();
        driver.setEmailAddress(emailAddress);
        driver.setPassword(password);

        JSONObject result = new JSONObject();
        Integer userID = dbHandler.loginDriver(driver);

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


    /*
    * Rider-side matchmaking method
    * Shortlists potential drivers, sends request to ideal
    * Waits for and returns response (driver accepts/declines)
    */
    public JSONObject findDriver(String riderID, String latitude, String longitude) {

        //TODO: Policy? Where does this come from?
        int MAX_ATTEMPTS = 60, MAX_REJECTED_ATTEMPTS = 5;
        boolean requestSent = false, driverPaired = false;


        // Create Ride Object
        Ride ride = new Ride();
        ride.setRider(new Rider());
        ride.setDriver(new Driver());

        // Package param into Ride Object
        ride.getRider().setUserID(Integer.valueOf(riderID));
        ride.setDropoffLocation(new Location(Double.valueOf(latitude), Double.valueOf(longitude), null));


        // Search for drivers that match criteria, TODO: Add criteria
        JSONArray drivers = dbHandler.searchDriverForRide();

        // Send request to next best driver
        int driverCount = drivers.length();
        for (int driverOption = 0 ; driverOption < driverCount && !requestSent; ++driverOption) {

            ride.getDriver().setUserID(((JSONObject) drivers.get(driverOption)).getInt("USER_ID"));

            //TODO: Decide and send param for DBHandler.sendRideRequest()
            requestSent = dbHandler.sendRideRequest(ride);
        }

        JSONObject findStatusResult = new JSONObject();
        if (requestSent) {
            int attempts = 0;
            int rejectedAttempts = 0;

            //TODO: Move this to a separate thread
            while (attempts < MAX_ATTEMPTS && rejectedAttempts < MAX_REJECTED_ATTEMPTS) {

                // Check find status corresponding to previous ride request
                findStatusResult = dbHandler.checkFindStatus(ride.getRider());

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

                try {
                    Thread.sleep(2000);
                }
                catch (Exception e) {
                    System.out.println(LOG_TAG + "findDriver: Thread.sleep() exception");
                }

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


    /* Driver-side matchmaking methods */
    // Setting Driver as Active
    // TODO: Policy on how a ride is sent to driver, implemented in checkRideStatus()
    public JSONObject setMarkActive(String userID, String activeStatus)
    {
        Driver driver = new Driver();
        driver.setIsActive(Boolean.valueOf(activeStatus));
        driver.setUserID(Integer.valueOf(userID));

        if (dbHandler.markDriverActive(driver))
        {
            JSONObject json = checkRideStatus(driver);
            if (json == null) {
                json = new JSONObject();
                json.put("STATUS", 404);
            }
            return json;
        }
        else
        {
            JSONObject json = new JSONObject();
            json.put("STATUS", 404);
            return json;
        }
    }

    private JSONObject checkRideStatus(Driver driver)
    {
        // TODO: Implement policy of checking rides
        while(true)
        {
            Ride ride = dbHandler.checkRideStatus(driver);
            if (ride != null)
            {
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("STATUS", 200);
                jsonObject.put("RIDER_ID", ride.getRider().getUserID());
                jsonObject.put("SOURCE_LAT", ride.getPickupLocation().getLatitude());
                jsonObject.put("SOURCE_LONG", ride.getPickupLocation().getLongitude());
                jsonObject.put("DEST_LAT", ride.getDropoffLocation().getLatitude());
                jsonObject.put("DEST_LONG", ride.getDropoffLocation().getLongitude());

                return jsonObject;
            }
            else
            {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    System.out.println("checkRideStatus(): Exception thrown at sleep()");
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }

    public JSONObject confirmRideRequest(String userID, String found_status, String rider_id)
    {
        Ride ride = new Ride();
        Driver driver = new Driver();
        Rider rider = new Rider();

        driver.setUserID(Integer.valueOf(userID));
        rider.setUserID(Integer.valueOf(rider_id));

        ride.setDriver(driver);
        ride.setRider(rider);
        ride.setRideStatus(Integer.parseInt(found_status) == 1? 2: 0);
        ride.setFindStatus(Integer.parseInt(found_status) + 1);

        JSONObject jsonObject = new JSONObject();
        if (dbHandler.confirmRideRequest(ride)) {
            jsonObject.put("STATUS", 200);
        }
        else {
            jsonObject.put("STATUS", 404);
        }
        return jsonObject;
    }
    /* End of section*/


    /* Location update & retreival methods*/
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
