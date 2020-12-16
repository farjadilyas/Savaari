package com.savaari_demo;

import com.savaari_demo.entity.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class OracleDBHandler implements DBHandler {

    private static final String LOG_TAG = OracleDBHandler.class.getSimpleName();
    private Connection connect = null;
    private static OracleDBHandler instance = new OracleDBHandler();    //single instance

    // Return single instance
    public static DBHandler getInstance() {
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

            //System.out.println("LOGIN QUERY3: " + sqlQuery);

            ResultSet resultSet = connect.createStatement().executeQuery(sqlQuery);

            if (resultSet.next()) {
                return resultSet.getInt("USER_ID");
            }
            else {
                return -1;
            }
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler:loginRider()");
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
            System.out.println("Exception in DBHandler:loginDriver()");
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
            System.out.println("Exception in DBHandler:riderDetails()");
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
            System.out.println("Exception in DBHandler:driverDetails()");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean fetchRiderData(Rider rider) {

        try {
            String sqlQuery = "SELECT USER_NAME, EMAIL_ADDRESS FROM RIDER_DETAILS WHERE USER_ID = " + rider.getUserID();
            ResultSet resultSet = connect.createStatement().executeQuery(sqlQuery);

            if (resultSet.next()) {
                rider.setUserID(rider.getUserID());
                rider.setUsername(resultSet.getString(1));
                rider.setEmailAddress(resultSet.getString(2));
                return true;
            }
            else {
                return false;
            }
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler:riderData()");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean fetchDriverData(Driver driver) {
        try {
            String sqlQuery = "SELECT USER_NAME, EMAIL_ADDRESS FROM DRIVER_DETAILS WHERE USER_ID = " + driver.getUserID();
            ResultSet resultSet = connect.createStatement().executeQuery(sqlQuery);

            if (resultSet.next()) {
                driver.setUserID(driver.getUserID());
                driver.setUsername(resultSet.getString(1));
                driver.setEmailAddress(resultSet.getString(2));
                return true;
            }
            else {
                return false;
            }
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler:driverData()");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean resetRider(Rider rider) {
        try {
            PreparedStatement sqlQuery = connect.prepareStatement(
                    "UPDATE RIDER_DETAILS SET FIND_STATUS = 0, DRIVER_ID = -1 WHERE RIDER_ID = ?");

            sqlQuery.setInt(1, rider.getUserID());

            int numRowsUpdated = sqlQuery.executeUpdate();

            return (numRowsUpdated > 0);
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler:resetRider()");
            e.printStackTrace();
            return false;
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
    public Integer checkFindStatus(Rider rider) {
        //TODO: simplify query, driver details not needed

        String sqlQuery = "SELECT R.FIND_STATUS, D.USER_ID, D.USER_NAME, CAST(D.LATITUDE AS CHAR(12)) AS SOURCE_LAT, " +
                "CAST(D.LONGITUDE AS CHAR(12)) AS SOURCE_LONG " +
                "FROM RIDER_DETAILS R " +
                "LEFT JOIN DRIVER_DETAILS D " +
                "ON R.DRIVER_ID = D.USER_ID " +
                "WHERE R.FIND_STATUS IN (1,2) AND R.USER_ID = " + rider.getUserID();

        Integer findStatus;

        try {
            ResultSet resultSet = connect.createStatement().executeQuery(sqlQuery);

            if (resultSet.next()) {
                int status = resultSet.getInt(1);

                switch (status) {
                    case 0:
                        findStatus = RideRequest.NO_CHANGE;
                        break;
                    case 1:
                        findStatus = RideRequest.NOT_PAIRED;
                        break;
                    case 2:
                        findStatus = RideRequest.PAIRED;
                        break;
                    default:
                        findStatus = RideRequest.STATUS_ERROR;
                        break;
                }
            }
            else {
                findStatus = RideRequest.STATUS_ERROR;
            }

            return findStatus;
        }
        catch (Exception e) {
            System.out.println(LOG_TAG + "Exception in DBHandler:checkFindStatus()");
            e.printStackTrace();

            return RideRequest.STATUS_ERROR;
        }

    }

    @Override
    public ArrayList<Driver> searchDriverForRide() {
        try {
            String sqlQuery = "SELECT USER_ID, USER_NAME, CAST(LATITUDE AS CHAR(12)) AS LATITUDE, " +
                    "CAST(LONGITUDE AS CHAR(12)) AS LONGITUDE FROM DRIVER_DETAILS";

            ResultSet resultSet = connect.createStatement().executeQuery(sqlQuery);

            ArrayList<Driver> results = new ArrayList<Driver>();
            Driver currentDriver;

            while(resultSet.next()) {
                currentDriver = new Driver();
                currentDriver.setUserID(resultSet.getInt(1));
                currentDriver.setUsername(resultSet.getString(2));
                currentDriver.setCurrentLocation(new Location(resultSet.getDouble(3),
                        resultSet.getDouble(4), null));
                results.add(currentDriver);

                /*
                row.put("USER_ID", );
                row.put("USER_NAME", resultSet.getString(2));
                row.put("LATITUDE", resultSet.getDouble(3));
                row.put("LONGITUDE", resultSet.getDouble(4));
                results.put(row);
                row = new JSONObject();*/
            }

            return results;
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler:searchDriverForRide()");
            e.printStackTrace();
            return new ArrayList<Driver>();
        }
    }

    @Override
    public boolean sendRideRequest(RideRequest rideRequest) {
        try {
            PreparedStatement sqlQuery = connect.prepareStatement(
                    "UPDATE DRIVER_DETAILS SET RIDER_ID = ?, RIDE_STATUS = 1, SOURCE_LAT = ?, SOURCE_LONG = ?," +
                            "DEST_LAT = ?, DEST_LONG = ? WHERE USER_ID = ? AND IS_ACTIVE = 1 AND RIDE_STATUS = 0");

            sqlQuery.setInt(1, rideRequest.getRider().getUserID());
            sqlQuery.setDouble(2, rideRequest.getPickupLocation().getLatitude());
            sqlQuery.setDouble(3, rideRequest.getPickupLocation().getLongitude());
            sqlQuery.setDouble(4, rideRequest.getDropoffLocation().getLatitude());
            sqlQuery.setDouble(5, rideRequest.getDropoffLocation().getLongitude());
            sqlQuery.setInt(6, rideRequest.getDriver().getUserID());

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
            sqlQuery.setBoolean(1, driver.isActive());
            sqlQuery.setInt(2, driver.getUserID());

            System.out.println(sqlQuery);

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
    public RideRequest checkRideRequestStatus(Driver driver)
    {
        try {
            PreparedStatement sqlQuery = connect.prepareStatement(
                    "SELECT D.RIDE_STATUS, D.RIDER_ID, R.USER_NAME, D.SOURCE_LAT, D.SOURCE_LONG, D.DEST_LAT, D.DEST_LONG"
                    + " FROM DRIVER_DETAILS D LEFT JOIN RIDER_DETAILS R ON D.RIDER_ID = R.USER_ID"
                    + " WHERE D.USER_ID = ?");

            sqlQuery.setInt(1, driver.getUserID());

            ResultSet resultSet = sqlQuery.executeQuery();
            // TODO: make this generic for multiple ride requests

            if (resultSet.next()) {
                if (resultSet.getInt(1) == 0) {
                    System.out.println("db:checkRideReqStat: op1");
                    return null;
                }
                if (resultSet.getInt(1) > 0) {
                    System.out.println("db:checkRideReqStat: op2");
                    RideRequest ride = new RideRequest();

                    Rider rider = new Rider();
                    rider.setUserID(resultSet.getInt(2));
                    rider.setUsername(resultSet.getString(3));

                    Location pickLocation = new Location();
                    Location destLocation = new Location();

                    pickLocation.setLatitude(resultSet.getDouble(4));
                    pickLocation.setLongitude(resultSet.getDouble(5));

                    destLocation.setLatitude(resultSet.getDouble(6));
                    destLocation.setLongitude(resultSet.getDouble(7));

                    ride.setRider(rider);
                    ride.setPickupLocation(pickLocation);
                    ride.setDropoffLocation(destLocation);
                    return ride;
                }
            }
            else {
                System.out.println("db:checkRideReqStat: op3");
                return null;
            }
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler: checkRideRequestStatus()");
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /*
     * Confirms ride request (signal to corresponding rider)
     * Records ride
     * */
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

    @Override
    public boolean markDriverArrival(Ride ride) {
        try {
            PreparedStatement sqlQuery = connect.prepareStatement("UPDATE RIDES SET STATUS = 12 WHERE RIDE_ID = ?");
            sqlQuery.setInt(1, ride.getRideID());
            int numRowsUpdates = sqlQuery.executeUpdate();
            return (numRowsUpdates > 0);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Starting the Ride from Driver side
    @Override
    public boolean startRideDriver(Ride ride) {
        try {
            PreparedStatement sqlQuery = connect.prepareStatement("UPDATE RIDES SET STATUS = 14 WHERE RIDE_ID = ?");

            sqlQuery.setInt(1, ride.getRideID());
            int numRowsUpdated = sqlQuery.executeUpdate();
            return (numRowsUpdated > 0);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean markArrivalAtDestination(Ride ride) {
        try {
            String query = "UPDATE RIDES SET STATUS = 15, DIST_TRAVELLED = " + ride.getDistanceTravelled()
                    + ", FARE = " + ride.getFare() + ", FINISH_TIME = CURRENT_TIME() WHERE RIDE_ID = " + ride.getRideID();

            System.out.println("This is the sqlQuery: " + query);
            PreparedStatement sqlQuery = connect.prepareStatement(query);
            int numRowsUpdated = sqlQuery.executeUpdate();
            System.out.println(LOG_TAG + ":markArrivalAtDestination: numRowsUpdated: " + numRowsUpdated);

            if (numRowsUpdated > 0) {
                System.out.println(LOG_TAG + "markArrivalAtDestination: NO ERROR");
                return true;
            }
            else {
                System.out.println(LOG_TAG + "markArrivalAtDestination: error #1");
                return false;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println(LOG_TAG + "markArrivalAtDestination: error #2");
            return false;
        }
    }

    /* End of section*/

    @Override
    public Payment addPayment() {
        
        Payment newPayment = null;
        
        String insertPaymentQuery = "INSERT INTO PAYMENTS VALUES(0, 0, 0, NULL, 0)";
        String generatedColumns[] = { "PAYMENT_ID" };

        PreparedStatement insertPaymentStatement;
        int numRowsUpdated;


        try {
            insertPaymentStatement = connect.prepareStatement(insertPaymentQuery, generatedColumns);
            numRowsUpdated = insertPaymentStatement.executeUpdate();
        }
        catch (Exception e) {
            System.out.println(LOG_TAG + ":addPayment: SQLException #1");
            e.printStackTrace();
            return null;
        }

        if (numRowsUpdated == 0) {
            System.out.println(LOG_TAG + ":recordRide: creating payment failed, no rows updated");
        }
        else {
            try {
                ResultSet generatedKeys = insertPaymentStatement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    newPayment = new Payment();
                    newPayment.setPaymentID(generatedKeys.getInt(1));
                }
                else {
                    System.out.println(LOG_TAG + ":recordRide: creating payment failed, no payment id obtained");
                }
            }
            catch (Exception e) {
                System.out.println(LOG_TAG + ":addPayment: SQLException #2");
                e.printStackTrace();
                return null;
            }
        }

        return newPayment;
    }


    // Store new Ride
    @Override
    public boolean recordRide(Ride ride) {    
        try {
            String query = "INSERT INTO RIDES " +
                    "SELECT 0, R.USER_ID AS RIDER_ID, D.USER_ID AS DRIVER_ID, " + ride.getPayment().getPaymentID() +" AS PAYMENT_ID, " +
                    "D.SOURCE_LAT, D.SOURCE_LONG, D.DEST_LAT, D.DEST_LONG, CURRENT_TIME(), NULL AS FINISH_TIME, 0 AS DIST_TRAVELLED, " +
                    "1 AS RIDE_TYPE, 0 AS ESTIMATED_FARE, 0 AS FARE, 11 AS STATUS " +
                    "FROM DRIVER_DETAILS AS D, RIDER_DETAILS AS R " +
                    "WHERE D.RIDER_ID = R.USER_ID AND D.USER_ID = " + ride.getDriver().getUserID() +
                    " AND D.RIDER_ID = " + ride.getRider().getUserID();

            System.out.println("The query is: " + query);
            PreparedStatement sqlQuery = connect.prepareStatement(query);

            int numRowsUpdated = sqlQuery.executeUpdate();

            return (numRowsUpdated == 1);
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler: recordRide()");
            e.printStackTrace();
            return false;
        }
    }

    public RideRequest checkRideRequestStatus(Rider rider) {
        String sqlQuery = "SELECT FIND_STATUS, DRIVER_ID FROM RIDER_DETAILS WHERE USER_ID = " + rider.getUserID();

        //TODO: make constants for ride request status

        try {
            ResultSet resultSet = connect.createStatement().executeQuery(sqlQuery);
            RideRequest rideRequest = null;

            if (resultSet.next()) {

                int findStatus = resultSet.getInt(1);

                rideRequest = new Ride();
                rideRequest.setRider(rider);
                rideRequest.setDriver(new Driver());

                rideRequest.setFindStatus(findStatus);
                rideRequest.getDriver().setUserID(resultSet.getInt(2));
            }
            else {
                // No active request or request rejected
                rideRequest = null;
            }

            return rideRequest;
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler: getRideRequestStatus(rider)");
            e.printStackTrace();
            return null;
        }
    }
    public Ride getRide(RideRequest rideRequest) {
        String sqlQuery = "SELECT RD.RIDE_ID, R.USER_NAME, D.USER_NAME, RD.PAYMENT_ID, RD.SOURCE_LAT, RD.SOURCE_LONG, RD.DEST_LAT, RD.DEST_LONG, RD.START_TIME, RD.RIDE_TYPE, RD.ESTIMATED_FARE, RD.STATUS, D.LATITUDE, D.LONGITUDE, RD.FARE\n" +
                "FROM RIDES RD, RIDER_DETAILS R, DRIVER_DETAILS D\n" +
                "WHERE RD.RIDER_ID = " + rideRequest.getRider().getUserID() +
                " AND RD.DRIVER_ID = " + rideRequest.getDriver().getUserID() +
                " AND RD.RIDER_ID = R.USER_ID AND RD.DRIVER_ID = D.USER_ID AND RD.STATUS <> 20";

        //JSONObject result = new JSONObject();
        Ride ride = null;

        try {
            ResultSet resultSet = connect.createStatement().executeQuery(sqlQuery);

            // Package results into ride object
            if (resultSet.next()) {

                /*
                result.put("STATUS_CODE", 200);
                result.put("RIDE_ID", resultSet.getInt(1));
                result.put("RIDER_ID", rideRequest.getRider().getUserID());
                result.put("DRIVER_ID", rideRequest.getDriver().getUserID());
                result.put("RIDER_NAME", resultSet.getString(2));
                result.put("DRIVER_NAME", resultSet.getString(3));
                result.put("PAYMENT_ID", resultSet.getInt(4));
                result.put("SOURCE_LAT", resultSet.getDouble(5));
                result.put("SOURCE_LONG", resultSet.getDouble(6));
                result.put("DEST_LAT", resultSet.getDouble(7));
                result.put("DEST_LONG", resultSet.getDouble(8));
                result.put("START_TIME", resultSet.getTimestamp(9).getTime());
                result.put("RIDE_TYPE", resultSet.getInt(10));
                result.put("ESTIMATED_FARE", resultSet.getInt(11));
                result.put("RIDE_STATUS", resultSet.getInt(12));
                result.put("DRIVER_LAT", resultSet.getDouble(13));
                result.put("DRIVER_LONG", resultSet.getDouble(14));
                result.put("FARE", resultSet.getDouble(15));*/

                ride = new Ride();
                ride.setRideID(resultSet.getInt(1));
                ride.getRider().setUsername(resultSet.getString(2));
                ride.getDriver().setUsername(resultSet.getString(3));
                ride.getPayment().setPaymentID(resultSet.getInt(4));
                ride.setPickupLocation(new Location(resultSet.getDouble(5), resultSet.getDouble(6)));
                ride.setDropoffLocation(new Location(resultSet.getDouble(7), resultSet.getDouble(8)));
                ride.setStartTime(resultSet.getTimestamp(9).getTime());
                ride.setRideType(resultSet.getInt(10));
                ride.setEstimatedFare(resultSet.getInt(11));
                ride.setRideStatus(resultSet.getInt(12));
                ride.getDriver().setCurrentLocation(new Location(resultSet.getDouble(13),
                        resultSet.getDouble(14)));
                ride.setFare(resultSet.getDouble(15));

                ride.setFindStatus(Ride.PAIRED);
            }
            else {
                //result.put("STATUS_CODE", 300);
            }

            return ride;
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler: getRide()");
            e.printStackTrace();
            //result.put("STATUS_CODE", 304);
            return null;
        }


    }

    public Integer getRideStatus(Ride ride) {
        String sqlQuery = "SELECT STATUS FROM RIDES WHERE RIDE_ID = " + ride.getRideID();

        Integer rideStatus = Ride.DEFAULT;

        try {
            ResultSet resultSet = connect.createStatement().executeQuery(sqlQuery);

            if (resultSet.next()) {
                rideStatus = resultSet.getInt(1);
            }

            return rideStatus;
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler: getRide()");
            e.printStackTrace();
            return Ride.DEFAULT;
        }
    }

    @Override
    public boolean endRideWithPayment(Ride ride)
    {
        try {
            PreparedStatement sqlQuery = connect.prepareStatement("UPDATE RIDES SET STATUS = 16 WHERE RIDE_ID = ?");
            sqlQuery.setInt(1, ride.getRideID());

            int numRowsUpdated = sqlQuery.executeUpdate();
            if (numRowsUpdated > 0) {
                sqlQuery = connect.prepareStatement("UPDATE PAYMENTS P INNER JOIN RIDES RD ON RD.PAYMENT_ID = P.PAYMENT_ID SET P.AMOUNT_PAID = ?, P.CHANGE = ? - RD.FARE WHERE RD.RIDE_ID = ?");

                sqlQuery.setDouble(1, ride.getPayment().getAmountPaid());
                sqlQuery.setDouble(2, ride.getPayment().getAmountPaid());
                sqlQuery.setInt(3, ride.getRideID());

                numRowsUpdated = sqlQuery.executeUpdate();

                return numRowsUpdated > 0;

            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("Exception in DBHandler: endRideWitPayment");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean acknowledgeEndOfRide(Ride ride) {
        try {
            PreparedStatement sqlQuery = connect.prepareStatement("UPDATE RIDES SET STATUS = 20 WHERE RIDE_ID = ?");

            sqlQuery.setInt(1, ride.getRideID());

            int numRowsUpdated = sqlQuery.executeUpdate();
            return (numRowsUpdated > 0);
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler: acknowledgeEndOfRide");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean resetDriver(Driver driver) {
        try {

            PreparedStatement sqlQuery = connect.prepareStatement("UPDATE DRIVER_DETAILS SET IS_ACTIVE = 0, RIDE_STATUS = 0, RIDER_ID = -1, SOURCE_LAT = 0, SOURCE_LONG = 0, DEST_LAT = 0, DEST_LONG = 0 WHERE DRIVER_ID = ?");
            sqlQuery.setInt(1, driver.getUserID());

            if (sqlQuery.executeUpdate() > 0) {
                return true;
            }

        } catch (Exception e) {
            System.out.println("Exception in DBHandler: resetDriver()");
            e.printStackTrace();
        }
        return false;
    }
    /* End of section */


    /* Location update methods*/
    @Override
    public boolean saveRiderLocation(Rider rider) {
        try {
            PreparedStatement sqlQuery = connect.prepareStatement("UPDATE RIDER_DETAILS SET LATITUDE = ?, LONGITUDE = ?, TIMESTAMP = CURRENT_TIME() " +
                    "WHERE USER_ID = ?");

            sqlQuery.setDouble(1, rider.getCurrentLocation().getLatitude());
            sqlQuery.setDouble(2, rider.getCurrentLocation().getLongitude());
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

            sqlQuery.setDouble(1, driver.getCurrentLocation().getLatitude());
            sqlQuery.setDouble(2, driver.getCurrentLocation().getLongitude());
            sqlQuery.setInt(3, driver.getUserID());

            sqlQuery.executeUpdate();
            return true;
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler:saveDriverLocation()");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Location getRiderLocation(Rider rider) {
        try {
            String sqlQuery = "SELECT CAST(LATITUDE AS CHAR(12)) AS LATITUDE, CAST(LONGITUDE AS CHAR(12)) AS LONGITUDE" +
                    " FROM RIDER_DETAILS WHERE USER_ID = " + rider.getUserID();

            ResultSet resultSet = connect.createStatement().executeQuery(sqlQuery);

            Location fetchedLocation = null;

            if (resultSet.next()) {
                fetchedLocation = new Location(resultSet.getDouble(1),
                        resultSet.getDouble(2), null);
            }

            return fetchedLocation;
        }
        catch (Exception e) {
            System.out.println(LOG_TAG + "Exception:getRiderLocation()");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Location getDriverLocation(Driver driver) {
        try {
            String sqlQuery = "SELECT CAST(LATITUDE AS CHAR(12)) AS LATITUDE, CAST(LONGITUDE AS CHAR(12)) AS LONGITUDE" +
                    " FROM DRIVER_DETAILS WHERE USER_ID = " + driver.getUserID();

            ResultSet resultSet = connect.createStatement().executeQuery(sqlQuery);

            Location location;

            if (resultSet.next()) {
                location = new Location(resultSet.getDouble(1),
                        resultSet.getDouble(2), null);
            }
            else {
                location = null;
            }

            return location;
        }
        catch (Exception e) {
            System.out.println(LOG_TAG + "Exception:getDriverLocation()");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<Location> getRiderLocations() {
        try {
            String sqlQuery = "SELECT CAST(LATITUDE AS CHAR(12)) AS LATITUDE, CAST(LONGITUDE AS CHAR(12)) AS LONGITUDE" +
                    ", TIMESTAMP FROM RIDER_DETAILS";

            // Find list of Rider Locations //TODO: Add criteria later
            ResultSet resultSet = connect.createStatement().executeQuery(sqlQuery);

            ArrayList<Location> locations = new ArrayList<>();
            Location currentLocation;

            while (resultSet.next()) {
                currentLocation = new Location(resultSet.getDouble(1),
                        resultSet.getDouble(2),
                        resultSet.getTimestamp(3).getTime());
                locations.add(currentLocation);
            }

            return locations;
        }
        catch (Exception e) {
            System.out.println(LOG_TAG + "Exception:getRiderLocations()");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<Location> getDriverLocations() {
        try {
            String sqlQuery = "SELECT CAST(LATITUDE AS CHAR(12)) AS LATITUDE, CAST(LONGITUDE AS CHAR(12)) AS LONGITUDE" +
                    ", TIMESTAMP FROM DRIVER_DETAILS";

            // Find list of Driver Locations //TODO: Add criteria later
            ResultSet resultSet = connect.createStatement().executeQuery(sqlQuery);

            ArrayList<Location> locations = new ArrayList<>();
            Location currentLocation;

            while (resultSet.next()) {
                currentLocation = new Location(resultSet.getDouble(1),
                        resultSet.getDouble(2),
                        resultSet.getTimestamp(3).getTime());
                locations.add(currentLocation);
            }

            return locations;
        }
        catch (Exception e) {
            System.out.println(LOG_TAG + "Exception:getDriverLocations()");
            e.printStackTrace();
            return null;
        }
    }
    /* End of section */
}
