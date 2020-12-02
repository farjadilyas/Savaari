package com.savaari_demo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class OracleDBHandler implements DBHandler {

    private static final String LOG_TAG = OracleDBHandler.class.getSimpleName();
    private Connection connect = null;
    private static OracleDBHandler instance = new OracleDBHandler();    //single instance

    // Return single instance
    public OracleDBHandler getInstance() {
        return instance;
    }

    OracleDBHandler() {
        try {
            // Get MySql driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to database
            connect = DriverManager.getConnection("jdbc:mysql://"
                    + "localhost/ride_hailing" + "?user=" + "root"
                    + "&password=" + "thisIsOurSDAProjectPassword" +
                    "&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
        }
        catch (Exception e) {
            System.out.println("Exception in drverManager.getConnection()");
            e.printStackTrace();
        }
    }

    //Add a new Rider
    @Override
    public Boolean addRider(Rider rider) {
        System.out.println("Username: " + rider.getUsername() + ", password: " + rider.getPassword());
        try {
            PreparedStatement sqlQuery = connect.prepareStatement("INSERT INTO `RIDER_DETAILS` (`USER_ID`, `" +
                    "USER_NAME`, `PASSWORD`, `EMAIL_ADDRESS`) VALUES (?, ?, ?, ?)");

            sqlQuery.setInt(1, 0);
            sqlQuery.setString(2, rider.getUsername());
            sqlQuery.setString(3, rider.getPassword());
            sqlQuery.setString(4, rider.getEmailAddress());

            sqlQuery.executeUpdate();
            return true;
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler:addRider()");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean addDriver(Driver driver) {
        try {
            PreparedStatement sqlQuery = connect.prepareStatement("INSERT INTO `DRIVER_DETAILS` (`USER_ID`, `" +
                    "USER_NAME`, `PASSWORD`, `EMAIL_ADDRESS`) VALUES (?, ?, ?, ?)");

            sqlQuery.setInt(1, 0);
            sqlQuery.setString(2, driver.getUsername());
            sqlQuery.setString(3, driver.getPassword());
            sqlQuery.setString(4, driver.getEmailAddress());

            sqlQuery.executeUpdate();
            return true;
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler:addDriver()");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Integer loginRider(Rider rider) {
        try {
            String sqlQuery = "SELECT USER_ID FROM RIDER_DETAILS WHERE EMAIL_ADDRESS = '" + rider.getEmailAddress()
                    + "' AND PASSWORD = '" + rider.getPassword() + "'";

            System.out.println("LOGIN QUERY3: " + sqlQuery);

            ResultSet resultSet = connect.createStatement().executeQuery(sqlQuery);

            if (resultSet.next()) {
                return resultSet.getInt("USER_ID");
            }
            else {
                return -1;
            }
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler:addRider()");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer loginDriver(Driver driver) {
        try {
            String sqlQuery = "SELECT USER_ID FROM DRIVER_DETAILS WHERE EMAIL_ADDRESS = '" + driver.getEmailAddress()
                    + "' AND PASSWORD = '" + driver.getPassword() + "'";

            ResultSet resultSet = connect.createStatement().executeQuery(sqlQuery);

            if (resultSet.next()) {
                return resultSet.getInt("USER_ID");
            }
            else {
                return null;
            }
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler:addRider()");
            e.printStackTrace();
            return null;
        }
    }

    /* CRUD Operations on User Object */
    @Override
    public JSONArray riderDetails() {
        System.out.println("Rider deets called3");
        try {
            String sqlQuery = "SELECT USER_ID, USER_NAME, PASSWORD, EMAIL_ADDRESS FROM RIDER_DETAILS";

            ResultSet resultSet = connect.createStatement().executeQuery(sqlQuery);

            int numColumns = resultSet.getMetaData().getColumnCount();
            JSONArray result = new JSONArray();
            JSONObject row = new JSONObject();

            while (resultSet.next()) {
                row.put("USER_ID", resultSet.getInt(1));
                row.put("USER_NAME", resultSet.getString(2));
                row.put("PASSWORD", resultSet.getString(3));
                row.put("EMAIL_ADDRESS", resultSet.getString(4));
                result.put(row);
                row = new JSONObject();
            }
            System.out.println("RESULT: " + result.toString());
            return result;
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler:addRider()");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JSONArray driverDetails() {
        try {
            String sqlQuery = "SELECT USER_ID, USER_NAME, PASSWORD, EMAIL_ADDRESS FROM DRIVER_DETAILS";

            ResultSet resultSet = connect.createStatement().executeQuery(sqlQuery);

            int numColumns = resultSet.getMetaData().getColumnCount();
            JSONArray result = new JSONArray();
            JSONObject row = new JSONObject();

            while (resultSet.next()) {
                row.put("USER_ID", resultSet.getInt(1));
                row.put("USER_NAME", resultSet.getString(2));
                row.put("PASSWORD", resultSet.getString(3));
                row.put("EMAIL_ADDRESS", resultSet.getString(4));
                result.put(row);
                row = new JSONObject();
            }

            return result;
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler:addRider()");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Rider riderData(Rider rider) {

        try {
            String sqlQuery = "SELECT USER_NAME, EMAIL_ADDRESS FROM RIDER_DETAILS WHERE USER_ID = " + rider.getUserID();
            ResultSet resultSet = connect.createStatement().executeQuery(sqlQuery);

            Rider fetchedRider = new Rider();
            if (resultSet.next()) {
                fetchedRider.setUserID(rider.getUserID());
                fetchedRider.setUsername(resultSet.getString(1));
                fetchedRider.setEmailAddress(resultSet.getString(2));
                return fetchedRider;
            }
            else {
                return null;
            }
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler:addRider()");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Driver driverData(Driver driver) {
        try {
            String sqlQuery = "SELECT USER_NAME, EMAIL_ADDRESS FROM DRIVER_DETAILS WHERE USER_ID = " + driver.getUserID();
            ResultSet resultSet = connect.createStatement().executeQuery(sqlQuery);

            Driver fetchedDriver = new Driver();
            if (resultSet.next()) {
                fetchedDriver.setUserID(driver.getUserID());
                fetchedDriver.setUsername(resultSet.getString(1));
                fetchedDriver.setEmailAddress(resultSet.getString(2));
                return fetchedDriver;
            }
            else {
                return null;
            }
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler:addRider()");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean updateRider() {
        return null;
    }

    @Override
    public Boolean deleteRider() {
        return null;
    }

    @Override
    public Boolean deleteDriver() {
        return null;
    }
    /* End of section */


    /* Rider-side matchmaking DB calls */
    @Override
    public JSONObject checkFindStatus(Rider rider) {
        String sqlQuery = "SELECT R.FIND_STATUS, D.USER_ID, D.USER_NAME, CAST(D.LATITUDE AS CHAR(12)) AS SOURCE_LAT, " +
                "CAST(D.LONGITUDE AS CHAR(12)) AS SOURCE_LONG " +
                "FROM RIDER_DETAILS R " +
                "LEFT JOIN DRIVER_DETAILS D " +
                "ON R.DRIVER_ID = D.USER_ID " +
                "WHERE R.FIND_STATUS IN (1,2) AND R.USER_ID = " + rider.getUserID();

        JSONObject result = new JSONObject();

        try {
            ResultSet resultSet = connect.createStatement().executeQuery(sqlQuery);

            if (resultSet.next()) {
                int status = resultSet.getInt(1);

                switch (status) {
                    case 0:
                        result.put("STATUS", "NO_CHANGE");
                        break;
                    case 1:
                        result.put("STATUS", "REJECTED");
                        break;
                    case 2:
                        result.put("STATUS", "FOUND");
                        result.put("DRIVER_ID", resultSet.getInt(2));
                        result.put("DRIVER_NAME", resultSet.getString(3));
                        result.put("DRIVER_LAT", resultSet.getDouble(4));
                        result.put("DRIVER_LONG", resultSet.getDouble(5));
                        break;
                    default:
                        result.put("STATUS", "ERROR");
                        break;
                }
            }
            else {
                result.put("STATUS", "ERROR");
            }

            return result;
        }
        catch (Exception e) {
            System.out.println(LOG_TAG + "Exception in DBHandler:checkFindStatus()");
            e.printStackTrace();

            result = new JSONObject();
            result.put("STATUS", "ERROR");
            return result;
        }

    }

    @Override
    public JSONArray searchDriverForRide() {
        try {
            String sqlQuery = "SELECT USER_ID, USER_NAME, CAST(LATITUDE AS CHAR(12)) AS LATITUDE, " +
                    "CAST(LONGITUDE AS CHAR(12)) AS LONGITUDE FROM DRIVER_DETAILS";

            ResultSet resultSet = connect.createStatement().executeQuery(sqlQuery);
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler:addRider()");
            e.printStackTrace();
            return null;
        }


        return null;
    }

    @Override
    public boolean sendRideRequest(Ride rideRequest) {
        try {
            PreparedStatement sqlQuery = connect.prepareStatement(
                    "UPDATE DRIVER_DETAILS SET RIDER_ID = ?, RIDE_STATUS = 1, DEST_LAT = ?, DEST_LONG = ? " +
                            "WHERE USER_ID = ? AND IS_ACTIVE = 1 AND RIDE_STATUS = 0");

            sqlQuery.setInt(1, rideRequest.getRider().getUserID());
            sqlQuery.setDouble(2, rideRequest.getDropoffLocation().getLatitude());
            sqlQuery.setDouble(3, rideRequest.getDropoffLocation().getLongitude());
            sqlQuery.setInt(4, rideRequest.getDriver().getUserID());

            int numRowsUpdated = sqlQuery.executeUpdate();
            if (numRowsUpdated == 1) {
                System.out.println(LOG_TAG +  ":sendRideRequest: 1 row updated -> Request sent!");
                return true;
            }
            else {
                System.out.println(LOG_TAG + ":sendRideRequest: " + numRowsUpdated + " row updated -> FAILURE!");
                return false;
            }
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler:sendRideRequest()");
            e.printStackTrace();
            return false;
        }
    }
    /* End of section */


    /* Driver-side matchmaking DB calls*/
    @Override
    public boolean markDriverActive(Driver driver)
    {
        try {
            PreparedStatement sqlQuery = connect.prepareStatement("UPDATE DRIVER_DETAILS SET IS_ACTIVE = ? WHERE USER_ID = ?");
            sqlQuery.setBoolean(1, driver.getIsActive());
            sqlQuery.setInt(2, driver.getUserID());

            int numRowsUpdated = sqlQuery.executeUpdate();
            if (numRowsUpdated == 1) {
                System.out.println(LOG_TAG +  ":markDriverActive: 1 row updated -> Request sent!");
                return true;
            }
            else {
                System.out.println(LOG_TAG + ":markDriverActive: " + numRowsUpdated + " row updated -> FAILURE!");
                return false;
            }
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler: markDriverActive()");
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public Ride checkRideStatus(Driver driver)
    {
        try {
            PreparedStatement sqlQuery = connect.prepareStatement(
                    "SELECT D.RIDE_STATUS, D.RIDER_ID, R.USER_NAME, R.LATITUDE AS SOURCE_LAT, R.LONGITUDE AS SOURCE_LONG, D.DEST_LAT, D.DEST_LONG"
                    + " FROM DRIVER_DETAILS D LEFT JOIN RIDER_DETAILS R ON D.RIDER_ID = R.USER_ID"
                    + " WHERE D.RIDE_STATUS = 1 AND D.USER_ID = ?");

            sqlQuery.setInt(1, driver.getUserID());

            ResultSet resultSet = sqlQuery.executeQuery();
            // TODO: make this generic for multiple ride requests
            if (resultSet.getInt(0) == 0) {
                return null;
            }
            if (resultSet.getInt(0) == 1) {
                Ride ride = new Ride();

                Rider rider = new Rider();
                rider.setUserID(resultSet.getInt(1));
                rider.setUsername(resultSet.getString(2));

                Location pickLocation = new Location();
                Location destLocation = new Location();

                pickLocation.setLatitude(resultSet.getDouble(3));
                pickLocation.setLongitude(resultSet.getDouble(4));

                destLocation.setLatitude(resultSet.getDouble(5));
                destLocation.setLongitude(resultSet.getDouble(6));

                ride.setRider(rider);
                ride.setPickupLocation(pickLocation);
                ride.setDropoffLocation(destLocation);
                return ride;
            }
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler: checkRideStatus()");
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    public boolean confirmRideRequest(Ride ride) {
        try {
            PreparedStatement sqlQuery = connect.prepareStatement("UPDATE DRIVER_DETAILS SET RIDE_STATUS = ? WHERE USER_ID = ?");
            sqlQuery.setInt(1, ride.getRideStatus());
            sqlQuery.setInt(2, ride.getDriver().getUserID());

            int numRowsUpdated = sqlQuery.executeUpdate();
            if (numRowsUpdated <= 0)
                return false;

            PreparedStatement sqlQuery1 = connect.prepareStatement("UPDATE RIDER_DETAILS SET FIND_STATUS = ?, DRIVER_ID = ? WHERE USER_ID = ?");
            sqlQuery1.setInt(1, ride.getFindStatus());
            sqlQuery1.setInt(2, ride.getDriver().getUserID());
            sqlQuery1.setInt(3, ride.getRider().getUserID());

            numRowsUpdated = sqlQuery1.executeUpdate();
            return numRowsUpdated > 0;
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler: confirmRideRequest()");
            e.printStackTrace();
            return false;
        }
    }

    /* End of section*/

    /* Location update methods*/
    @Override
    public boolean saveRiderLocation(Rider rider) {
        try {
            PreparedStatement sqlQuery = connect.prepareStatement("UPDATE RIDER_DETAILS SET LATITUDE = ?, LONGITUDE = ?, TIMESTAMP = CURRENT_TIME() " +
                    "WHERE USER_ID = ?");

            sqlQuery.setDouble(1, rider.getLastLocation().getLatitude());
            sqlQuery.setDouble(2, rider.getLastLocation().getLongitude());
            sqlQuery.setInt(3, rider.getUserID());

            sqlQuery.executeUpdate();
            return true;
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler:saveRiderLocation()");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean saveDriverLocation(Driver driver) {
        try {
            PreparedStatement sqlQuery = connect.prepareStatement(
                    "UPDATE DRIVER_DETAILS SET LATITUDE = ?, LONGITUDE = ?, TIMESTAMP = CURRENT_TIME() WHERE USER_ID = ?");

            sqlQuery.setDouble(1, driver.getLastLocation().getLatitude());
            sqlQuery.setDouble(2, driver.getLastLocation().getLongitude());
            sqlQuery.setInt(3, driver.getUserID());

            sqlQuery.executeUpdate();
            return true;
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler:saveRiderLocation()");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Rider getRiderLocation(Rider rider) {
        try {
            String sqlQuery = "SELECT CAST(LATITUDE AS CHAR(12)) AS LATITUDE, CAST(LONGITUDE AS CHAR(12)) AS LONGITUDE" +
                    " FROM RIDER_DETAILS WHERE USER_ID = " + rider.getUserID();

            ResultSet resultSet = connect.createStatement().executeQuery(sqlQuery);

            Rider fetchedRider;

            if (resultSet.next()) {
                fetchedRider = new Rider();
                fetchedRider.setUserID(rider.getUserID());
                fetchedRider.setLastLocation(new Location(resultSet.getDouble(1),
                        resultSet.getDouble(2), null));
            }
            else {
                fetchedRider = null;
            }

            return fetchedRider;
        }
        catch (Exception e) {
            System.out.println(LOG_TAG + "Exception:saveRiderLocation()");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Driver getDriverLocation(Driver driver) {
        try {
            String sqlQuery = "SELECT CAST(LATITUDE AS CHAR(12)) AS LATITUDE, CAST(LONGITUDE AS CHAR(12)) AS LONGITUDE" +
                    " FROM DRIVER_DETAILS WHERE USER_ID = " + driver.getUserID();

            ResultSet resultSet = connect.createStatement().executeQuery(sqlQuery);

            Driver fetchedDriver;

            if (resultSet.next()) {
                fetchedDriver = new Driver();
                fetchedDriver.setUserID(driver.getUserID());
                fetchedDriver.setLastLocation(new Location(resultSet.getDouble(1),
                        resultSet.getDouble(2), null));
            }
            else {
                fetchedDriver = null;
            }

            return fetchedDriver;
        }
        catch (Exception e) {
            System.out.println(LOG_TAG + "Exception:saveRiderLocation()");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JSONArray getRiderLocations() {
        try {
            String sqlQuery = "SELECT CAST(LATITUDE AS CHAR(12)) AS LATITUDE, CAST(LONGITUDE AS CHAR(12)) AS LONGITUDE" +
                    ", TIMESTAMP FROM RIDER_DETAILS";

            // Find list of Rider Locations //TODO: Add criteria later
            ResultSet resultSet = connect.createStatement().executeQuery(sqlQuery);

            //Package resultSet into JSONArray
            JSONArray result = new JSONArray();
            JSONObject row = new JSONObject();

            while (resultSet.next()) {
                row.put("LATITUDE", resultSet.getDouble(1));
                row.put("LONGITUDE", resultSet.getDouble(2));
                row.put("TIMESTAMP", resultSet.getDouble(2));
                result.put(row);
                row = new JSONObject();
            }

            return result;
        }
        catch (Exception e) {
            System.out.println(LOG_TAG + "Exception:saveRiderLocation()");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JSONArray getDriverLocations() {
        try {
            String sqlQuery = "SELECT CAST(LATITUDE AS CHAR(12)) AS LATITUDE, CAST(LONGITUDE AS CHAR(12)) AS LONGITUDE" +
                    ", TIMESTAMP FROM DRIVER_DETAILS";

            // Find list of Rider Locations //TODO: Add criteria later
            ResultSet resultSet = connect.createStatement().executeQuery(sqlQuery);

            //Package resultSet into JSONArray
            JSONArray result = new JSONArray();
            JSONObject row = new JSONObject();

            while (resultSet.next()) {
                row.put("LATITUDE", resultSet.getDouble(1));
                row.put("LONGITUDE", resultSet.getDouble(2));
                row.put("TIMESTAMP", resultSet.getDouble(2));
                result.put(row);
                row = new JSONObject();
            }

            return result;
        }
        catch (Exception e) {
            System.out.println(LOG_TAG + "Exception:saveDriverLocation()");
            e.printStackTrace();
            return null;
        }
    }
    /* End of section */
}
