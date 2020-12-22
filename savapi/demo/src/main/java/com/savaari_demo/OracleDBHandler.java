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
    private static OracleDBHandler instance = null;    //single instance

    // Return single instance
    public static DBHandler getInstance() {
        if (instance == null) {
            instance = new OracleDBHandler();
        }
        return instance;
    }

    private OracleDBHandler() {
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
                    "USER_NAME`, `PASSWORD`, `EMAIL_ADDRESS`, `FIND_STATUS` ,`DRIVER_ID`) VALUES (?, ?, ?, ?, ?, ?)");

            sqlQuery.setInt(1, 0);
            sqlQuery.setString(2, rider.getUsername());
            sqlQuery.setString(3, rider.getPassword());
            sqlQuery.setString(4, rider.getEmailAddress());
            sqlQuery.setInt(5, RideRequest.NOT_SENT);
            sqlQuery.setInt(6, Driver.DEFAULT_ID);

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
    public boolean sendRegistrationRequest(Driver driver)
    {
        try {
            PreparedStatement sqlQuery = connect.prepareStatement("UPDATE DRIVER DETAILS SET FIRST_NAME = ?" +
                    ", LAST_NAME = ?, PHONE_NO = ?, CNIC = ?, LICENSE_NUMBER = ?, STATUS = ?");

            sqlQuery.setString(1, driver.getFirstName());
            sqlQuery.setString(2, driver.getLastName());
            sqlQuery.setString(3, driver.getPhoneNo());
            sqlQuery.setString(4, driver.getCNIC());
            sqlQuery.setString(5, driver.getLicenseNumber());
            sqlQuery.setInt(6, Driver.DV_REQ_SENT);

            int numRowsUpdated = sqlQuery.executeUpdate();

            return numRowsUpdated > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Integer loginRider(Rider rider) {
        try {
            String sqlQuery = "SELECT USER_ID FROM RIDER_DETAILS WHERE EMAIL_ADDRESS = '" + rider.getEmailAddress()
                    + "' AND PASSWORD = '" + rider.getPassword() + "'";

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
        //TODO: refactor with constants

        try {
            String sqlQuery = "SELECT USER_NAME, FIRST_NAME, LAST_NAME, PHONE_NO, CNIC, LICENSE_NO, EMAIL_ADDRESS, STATUS, IS_ACTIVE, ACTIVE_VEHICLE_ID FROM DRIVER_DETAILS WHERE USER_ID = " + driver.getUserID();
            ResultSet resultSet = connect.createStatement().executeQuery(sqlQuery);

            if (resultSet.next()) {

                ArrayList<Vehicle> vehicles = new ArrayList<>();
                Vehicle currentVehicle = null;

                driver.setUserID(driver.getUserID());
                driver.setUsername(resultSet.getString(1));
                driver.setFirstName(resultSet.getString(2));
                driver.setLastName(resultSet.getString(3));
                driver.setPhoneNo(resultSet.getString(4));
                driver.setCNIC(resultSet.getString(5));
                driver.setLicenseNumber(resultSet.getString(6));
                driver.setEmailAddress(resultSet.getString(7));
                driver.setActive(Integer.parseInt(resultSet.getString(8)) == 1);
                driver.setActiveVehicleID(Integer.parseInt(resultSet.getString(9)));

                // Retrieve Driver's Vehicles
                String vehiclesQuery = "SELECT DV.VEHICLE_ID, VT.MAKE, VT.MODEL, VT.YEAR, VT.RIDE_TYPE_ID, DV.NUMBER_PLATE" +
                        ", DV.STATUS, DV.COLOR" +
                        " FROM DRIVERS_VEHICLES DV" +
                        " INNER JOIN VEHICLE_TYPES VT ON DV.VEHICLE_TYPE_ID = VT.VEHICLE_TYPE_ID" +
                        " WHERE DV.DRIVER_ID = " + driver.getUserID();

                resultSet = connect.createStatement().executeQuery(vehiclesQuery);

                while (resultSet.next()) {
                    currentVehicle = new Vehicle(resultSet.getInt(1), resultSet.getString(2),
                            resultSet.getString(3), resultSet.getString(4), resultSet.getInt(5),
                            resultSet.getString(6), resultSet.getInt(7),
                            resultSet.getString(8));
                    vehicles.add(currentVehicle);

                    if (driver.getActiveVehicleID() == Vehicle.VH_DEFAULT && currentVehicle.getStatus() == Vehicle.VH_ACCEPTANCE_ACK)  {
                        driver.setActiveVehicleID(currentVehicle.getStatus());
                    }
                }

                // Retrieve Driver's Vehicle requests
                String vehicleRequestQuery = "SELECT REGISTRATION_REQ_ID, MAKE, MODEL, YEAR, NUMBER_PLATE, STATUS, COLOR" +
                        " FROM VEHICLE_REGISTRATION_REQ" +
                        " WHERE DRIVER_ID = " + driver.getUserID();

                resultSet = connect.createStatement().executeQuery(vehicleRequestQuery);

                while (resultSet.next()) {
                    currentVehicle = new Vehicle(resultSet.getInt(1), resultSet.getString(2),
                            resultSet.getString(3), resultSet.getString(4), 0,
                            resultSet.getString(5), resultSet.getInt(6),
                            resultSet.getString(7));
                    vehicles.add(currentVehicle);
                }

                driver.setVehicles(vehicles);

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
    public boolean resetRider(Rider rider, boolean checkForResponse) {
        try {
            PreparedStatement sqlQuery = connect.prepareStatement(
                    "UPDATE RIDER_DETAILS SET FIND_STATUS = " + Ride.NOT_SENT +", DRIVER_ID = " + Driver.DEFAULT_ID +
                            " WHERE USER_ID = ?" + ((checkForResponse)? " AND FIND_STATUS <> " + RideRequest.FOUND : ""));

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

        String sqlQuery = "SELECT R.FIND_STATUS " +
                "FROM RIDER_DETAILS R " +
                "WHERE R.FIND_STATUS IN (" + RideRequest.REJECTED + "," + RideRequest.FOUND + ") AND R.USER_ID = " + rider.getUserID();

        Integer findStatus;
        ResultSet resultSet = null;
        long currentTime = System.currentTimeMillis();
        long endTime = currentTime + 36000;

        try {
            while (currentTime < endTime) {
                resultSet = connect.createStatement().executeQuery(sqlQuery);

                if (resultSet.next()) {
                    findStatus = resultSet.getInt(1);

                    switch (findStatus) {
                        // Request not sent or response received
                        case -1:
                            return RideRequest.NOT_SENT;
                        case 1:
                            return RideRequest.NOT_PAIRED;
                        case 2:
                            return RideRequest.PAIRED;

                        // Default: do nothing
                    }
                }

                try {
                    Thread.sleep(2000);
                }
                catch (Exception e) {
                    System.out.println("DBHandler: checkFindStatus: Thread.sleep() exception");
                }

                currentTime = System.currentTimeMillis();
            }

            return RideRequest.NO_CHANGE;
        }
        catch (Exception e) {
            System.out.println(LOG_TAG + "Exception in DBHandler:checkFindStatus()");
            e.printStackTrace();

            return RideRequest.STATUS_ERROR;
        }


    }

    @Override
    public ArrayList<Driver> searchDriverForRide() {
        //TODO: Add checks for RideType

        try {
            String sqlQuery = "SELECT USER_ID, USER_NAME, CAST(LATITUDE AS CHAR(12)) AS LATITUDE, " +
                    "CAST(LONGITUDE AS CHAR(12)) AS LONGITUDE FROM DRIVER_DETAILS";

            ResultSet resultSet = connect.createStatement().executeQuery(sqlQuery);

            ArrayList<Driver> results = new ArrayList<>();
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
            return new ArrayList<>();
        }
    }

    //TODO: add check for driver's vehicle's ride type
    @Override
    public boolean sendRideRequest(RideRequest rideRequest) {
        try {
            PreparedStatement sqlQuery = connect.prepareStatement(
                    "UPDATE DRIVER_DETAILS SET RIDER_ID = ?, RIDE_STATUS = 1, SOURCE_LAT = ?, SOURCE_LONG = ?," +
                            "DEST_LAT = ?, DEST_LONG = ?, PAYMENT_MODE = ?, RIDE_TYPE = ? WHERE USER_ID = ? AND IS_ACTIVE = 1 AND RIDE_STATUS = 0");

            sqlQuery.setInt(1, rideRequest.getRider().getUserID());
            sqlQuery.setDouble(2, rideRequest.getPickupLocation().getLatitude());
            sqlQuery.setDouble(3, rideRequest.getPickupLocation().getLongitude());
            sqlQuery.setDouble(4, rideRequest.getDropoffLocation().getLatitude());
            sqlQuery.setDouble(5, rideRequest.getDropoffLocation().getLongitude());
            sqlQuery.setInt(6, rideRequest.getPaymentMethod());
            sqlQuery.setInt(7, rideRequest.getRideType());
            sqlQuery.setInt(8, rideRequest.getDriver().getUserID());

            int numRowsUpdated = sqlQuery.executeUpdate();
            if (numRowsUpdated == 1) {
                System.out.println(LOG_TAG +  ":sendRideRequest: 1 row updated -> Request sent!");

                sqlQuery = connect.prepareStatement("UPDATE RIDER_DETAILS SET FIND_STATUS = " + RideRequest.NO_CHANGE +
                        ", DRIVER_ID = " + rideRequest.getDriver().getUserID() + " WHERE USER_ID = ?");
                sqlQuery.setInt(1, rideRequest.getRider().getUserID());

                numRowsUpdated = sqlQuery.executeUpdate();

                if (numRowsUpdated == 1) {
                    System.out.println(LOG_TAG +  ":sendRideRequest: 1 row updated -> Rider marked as NO_CHANGE!");
                    return true;
                }
                else {
                    System.out.println(LOG_TAG +  ":sendRideRequest: 1 row updated -> failure: Rider NOT marked as NO_CHANGE!");
                    return false;
                }
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
    public RideRequest checkRideRequestStatus(Driver driver, int timeout)
    {
        RideRequest rideRequest = new RideRequest();
        rideRequest.setFindStatus(33);
        try {
            PreparedStatement sqlQuery = connect.prepareStatement(
                    "SELECT D.RIDE_STATUS, D.RIDER_ID, R.USER_NAME, D.SOURCE_LAT, D.SOURCE_LONG, D.DEST_LAT, D.DEST_LONG, D.PAYMENT_MODE, D.RIDE_TYPE"
                    + " FROM DRIVER_DETAILS D LEFT JOIN RIDER_DETAILS R ON D.RIDER_ID = R.USER_ID"
                    + " WHERE D.USER_ID = ? AND D.IS_ACTIVE = 1");

            sqlQuery.setInt(1, driver.getUserID());

            // Preparing the Loop
            ResultSet resultSet = null;
            long currentTime = System.currentTimeMillis();
            long endTime = currentTime + timeout;
            while (currentTime <= endTime)
            {
                resultSet = sqlQuery.executeQuery();

                // If Rows were found
                if (resultSet.next())
                {
                    if (resultSet.getInt(1) == 0) {
                        System.out.println("db:checkRideReqStat: Ride not found!");
                        try {
                            Thread.sleep(2000);
                        }
                        catch (Exception e) {
                            System.out.println("Rider: findDriver: Thread.sleep() exception");
                        }
                        currentTime = System.currentTimeMillis();
                    }
                    if (resultSet.getInt(1) > 0) {
                        System.out.println("db:checkRideReqStat: op2");
                        rideRequest = new RideRequest();

                        rideRequest.setPaymentMethod(resultSet.getInt(8));
                        rideRequest.setFindStatus(resultSet.getInt(1));

                        Rider rider = new Rider();
                        rider.setUserID(resultSet.getInt(2));
                        rider.setUsername(resultSet.getString(3));

                        Location pickLocation = new Location();
                        Location destLocation = new Location();

                        pickLocation.setLatitude(resultSet.getDouble(4));
                        pickLocation.setLongitude(resultSet.getDouble(5));

                        destLocation.setLatitude(resultSet.getDouble(6));
                        destLocation.setLongitude(resultSet.getDouble(7));

                        rideRequest.setRideType(resultSet.getInt(8));

                        rideRequest.setRider(rider);
                        rideRequest.setPickupLocation(pickLocation);
                        rideRequest.setDropoffLocation(destLocation);
                        return rideRequest;
                    }
                } // End if: Rows found
                else {
                    System.out.println("db:checkRideReqStat: op3");
                    return rideRequest;
                }
            } // End while
        } // End of Try block
        catch (Exception e) {
            System.out.println("Exception in DBHandler: checkRideRequestStatus()");
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    public boolean rejectRideRequest(RideRequest rideRequest) {
        int numRowsUpdated = 0;

        try {
            String sqlQuery =
                    "UPDATE RIDER_DETAILS SET FIND_STATUS = " + RideRequest.REJECTED + ", DRIVER_ID = " + Driver.DEFAULT_ID
                            + " WHERE USER_ID = " + rideRequest.getRider().getUserID() + " AND FIND_STATUS = " + RideRequest.NO_CHANGE
                    + " AND DRIVER_ID = " + rideRequest.getDriver().getUserID();

            PreparedStatement riderSqlQuery = connect.prepareStatement(sqlQuery);
            numRowsUpdated = riderSqlQuery.executeUpdate();

            System.out.println("rejectRideRequest: numRowsUpdated: " + numRowsUpdated);

            return (resetDriver(rideRequest.getDriver()));
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler: rejectRideRequest()");
            e.printStackTrace();
            return false;
        }
    }

    /*
     * Confirms ride request (signal to corresponding rider)
     * Records ride
     * */
    @Override
    public boolean confirmRideRequest(Ride ride) {
        try {
            PreparedStatement sqlQuery1 = connect.prepareStatement(
                    "UPDATE RIDER_DETAILS SET FIND_STATUS = ?, DRIVER_ID = ? WHERE USER_ID = ? AND FIND_STATUS = "
                            + RideRequest.NO_CHANGE + " AND DRIVER_ID = " + ride.getDriver().getUserID());

            sqlQuery1.setInt(1, RideRequest.FOUND);
            sqlQuery1.setInt(2, ride.getDriver().getUserID());
            sqlQuery1.setInt(3, ride.getRider().getUserID());

            int numRowsUpdated = sqlQuery1.executeUpdate();
            if (numRowsUpdated <= 0) {
                resetDriver(ride.getDriver());
                return false;
            }

            PreparedStatement sqlQuery = connect.prepareStatement("UPDATE DRIVER_DETAILS SET RIDE_STATUS = ? WHERE USER_ID = ?");
            sqlQuery.setInt(1, RideRequest.MS_REQ_ACCEPTED);
            sqlQuery.setInt(2, ride.getDriver().getUserID());

            numRowsUpdated = sqlQuery.executeUpdate();

            // Returning the confirmation of this query and recording the ride in the DB
            return (numRowsUpdated > 0 && recordRide(ride));
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler: confirmRideRequest()");
            e.printStackTrace();
            resetDriver(ride.getDriver());
            return false;
        }
    }

    @Override
    public boolean markDriverArrival(Ride ride) {
        try {
            PreparedStatement sqlQuery = connect.prepareStatement("UPDATE RIDES SET STATUS = " + Ride.DRIVER_ARRIVED + " WHERE RIDE_ID = ?");
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
            PreparedStatement sqlQuery = connect.prepareStatement("UPDATE RIDES SET STATUS = " + Ride.STARTED + " WHERE RIDE_ID = ?");

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
            String query = "UPDATE RIDES SET STATUS = " + Ride.ARRIVED_AT_DEST + ", DIST_TRAVELLED = " + ride.getDistanceTravelled()
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
    public void recordPayment(Payment payment) {

        String insertPaymentQuery = "INSERT INTO PAYMENTS VALUES(NULL, ?, ?, TIMESTAMP = CURRENT_TIME(), ?)";
        String generatedColumns[] = { "PAYMENT_ID" };

        PreparedStatement insertPaymentStatement = null;
        int numRowsUpdated = 0;


        try {
            insertPaymentStatement = connect.prepareStatement(insertPaymentQuery, generatedColumns);
            insertPaymentStatement.setDouble(1, payment.getAmountPaid());
            insertPaymentStatement.setDouble(2, payment.getChange());
            insertPaymentStatement.setInt(3, payment.getPaymentMode());

            numRowsUpdated = insertPaymentStatement.executeUpdate();
        }
        catch (Exception e) {
            System.out.println(LOG_TAG + ":recordPayment: SQLException #1");
            e.printStackTrace();
        }

        if (numRowsUpdated == 0) {
            System.out.println(LOG_TAG + ":recordPayment: creating payment failed, no rows updated");
        }
        else if (insertPaymentStatement != null) {
            try {
                ResultSet generatedKeys = insertPaymentStatement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    payment.setPaymentID(generatedKeys.getInt(1));
                }
                else {
                    System.out.println(LOG_TAG + ":recordPayment: creating payment failed, no payment id obtained");
                }
            }
            catch (Exception e) {
                System.out.println(LOG_TAG + ":recordPayment: SQLException #2");
                e.printStackTrace();
            }
        }
        else {
            System.out.println(LOG_TAG + ":recordPayment: insertPaymentStatement is null");
        }
    }


    // Store new Ride
    @Override
    public boolean recordRide(Ride ride) {    
        try {
            String query = "INSERT INTO RIDES " +
                    "SELECT 0, R.USER_ID AS RIDER_ID, D.USER_ID AS DRIVER_ID, NULL AS PAYMENT_ID, " +
                    "D.SOURCE_LAT, D.SOURCE_LONG, D.DEST_LAT, D.DEST_LONG, CURRENT_TIME(), NULL AS FINISH_TIME, 0 AS DIST_TRAVELLED, " +
                    "D.RIDE_TYPE AS RIDE_TYPE, 0 AS ESTIMATED_FARE, 0 AS FARE, " + Ride.PICKUP + " AS STATUS " +
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

    @Override
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
            // No active request or request rejected
            return rideRequest;
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler: getRideRequestStatus(rider)");
            e.printStackTrace();
            return null;
        }
    }
    public Ride getRide(RideRequest rideRequest) {
        String sqlQuery = "SELECT RD.RIDE_ID, R.USER_NAME, D.USER_NAME, RD.PAYMENT_ID, RD.SOURCE_LAT, RD.SOURCE_LONG, " +
                "RD.DEST_LAT, RD.DEST_LONG, RD.START_TIME, RD.RIDE_TYPE, RD.ESTIMATED_FARE, RD.STATUS, D.LATITUDE, D.LONGITUDE, " +
                "RD.FARE\n" +
                "FROM RIDES RD, RIDER_DETAILS R, DRIVER_DETAILS D\n" +
                "WHERE RD.RIDER_ID = " + rideRequest.getRider().getUserID() +
                " AND RD.DRIVER_ID = " + rideRequest.getDriver().getUserID() +
                " AND RD.RIDER_ID = R.USER_ID AND RD.DRIVER_ID = D.USER_ID AND RD.STATUS <> " + Ride.END_ACKED;

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

                // Set rider attributes
                ride.getRider().setUserID(rideRequest.getRider().getUserID());
                ride.getRider().setUsername(resultSet.getString(2));

                // Set driver attributes
                ride.getDriver().setUserID(rideRequest.getDriver().getUserID());
                ride.getDriver().setUsername(resultSet.getString(3));
                ride.getDriver().setCurrentLocation(new Location(resultSet.getDouble(13),
                        resultSet.getDouble(14)));

                // Set ride attributes
                ride.getPayment().setPaymentID(resultSet.getInt(4));
                ride.setPickupLocation(new Location(resultSet.getDouble(5), resultSet.getDouble(6)));
                ride.setDropoffLocation(new Location(resultSet.getDouble(7), resultSet.getDouble(8)));
                ride.setStartTime(resultSet.getTimestamp(9).getTime());
                ride.setRideType(resultSet.getInt(10));
                ride.setEstimatedFare(resultSet.getInt(11));
                ride.setRideStatus(resultSet.getInt(12));
                ride.setFare(resultSet.getDouble(15));
                ride.setFindStatus(Ride.PAIRED);
            }
            //result.put("STATUS_CODE", 300);

            return ride;
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler: getRide()");
            e.printStackTrace();
            //result.put("STATUS_CODE", 304);
            return null;
        }


    }

    @Override
    public void getRideStatus(Ride ride) {
        ride.setRideStatus(Ride.DEFAULT);

        String sqlQuery = "SELECT STATUS, FARE FROM RIDES WHERE RIDE_ID = " + ride.getRideID();

        try {
            ResultSet resultSet = connect.createStatement().executeQuery(sqlQuery);

            if (resultSet.next()) {
                ride.setRideStatus(resultSet.getInt(1));
                ride.setFare(resultSet.getDouble(2));
            }
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler: getRide()");
            e.printStackTrace();
        }
    }

    /*
    * Param: Ride with Payment - paymentID must be initialized
    * Adds reference from ride to payment, persists payment information
    */
    @Override
    public boolean endRideWithPayment(Ride ride)
    {
        try {
            if (ride.getPayment().getPaymentID() < 0) {
                return false;
            }

            PreparedStatement sqlQuery = connect.prepareStatement(
                    "UPDATE RIDES SET PAYMENT_ID  = " + ride.getPayment().getPaymentID() +
                            ", STATUS = " + Ride.PAYMENT_MADE +
                            " WHERE RIDE_ID = ?");

            sqlQuery.setInt(1, ride.getRideID());

            int numRowsUpdated = sqlQuery.executeUpdate();

            if (numRowsUpdated > 0) {
                sqlQuery = connect.prepareStatement("UPDATE PAYMENTS P INNER JOIN RIDES RD ON RD.PAYMENT_ID = P.PAYMENT_ID SET P.AMOUNT_PAID = ?, P.CHANGE = ? - RD.FARE WHERE RD.RIDE_ID = ?");

                sqlQuery.setDouble(1, ride.getPayment().getAmountPaid());
                sqlQuery.setDouble(2, ride.getPayment().getAmountPaid());
                sqlQuery.setInt(3, ride.getRideID());

                numRowsUpdated = sqlQuery.executeUpdate();

                return (numRowsUpdated > 0 && resetDriver(ride.getDriver()));

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
            PreparedStatement sqlQuery = connect.prepareStatement("UPDATE RIDES SET STATUS = " + Ride.END_ACKED + " WHERE RIDE_ID = ?");

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

            PreparedStatement sqlQuery = connect.prepareStatement("UPDATE DRIVER_DETAILS SET RIDE_STATUS = "  + RideRequest.MS_DEFAULT
                     + ", RIDER_ID = " + User.DEFAULT_ID + ", SOURCE_LAT = 0, SOURCE_LONG = 0, DEST_LAT = 0, DEST_LONG = 0, PAYMENT_MODE = -1, RIDE_TYPE = 0 WHERE USER_ID = ?");

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
                        null);
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

    /* Driver-Vehicle calls */

    /*
    * For all vehicles, if Vehicle.getStatus()
    * = VH_DEFAULT -> Send new request
    * = VH_REQ_REJECTED -> Update existing request
    * */
    @Override
    public boolean sendVehicleRequest(Driver driver) {
        try {
            ArrayList<Vehicle> driversVehicles = driver.getVehicles();

            String sendNewRequestQuery, updateExistingRequestQuery;
            PreparedStatement statement;

            for (Vehicle currentVehicleRequest : driversVehicles) {

                // If request hasn't been sent
                if (currentVehicleRequest.getStatus() == Vehicle.VH_DEFAULT) {
                    sendNewRequestQuery = "INSERT INTO VEHICLE_REGISTRATION_REQ" +
                            " VALUES(" + driver.getUserID() +", 0, " + currentVehicleRequest.getMake() +
                            ", " + currentVehicleRequest.getModel() + ", " + currentVehicleRequest.getYear() +
                        ", " + currentVehicleRequest.getNumberPlate() + ", " + currentVehicleRequest.getColor() +
                        ", " + Vehicle.VH_REQ_SENT+ ")";
                    System.out.println("sendVehicleRequest: " + sendNewRequestQuery);

                    statement = connect.prepareStatement(sendNewRequestQuery);
                    statement.executeUpdate();

                }
                // Previously rejected request
                else if (currentVehicleRequest.getStatus() == Vehicle.VH_REQ_REJECTED) {
                    updateExistingRequestQuery = "UPDATE VEHICLE_REGISTRATION_REQ" +
                            " SET MAKE = " + currentVehicleRequest.getMake() +
                            ", MODEL = " + currentVehicleRequest.getModel() +
                            ", YEAR = " + currentVehicleRequest.getYear() +
                            ", NUMBER_PLATE = " + currentVehicleRequest.getNumberPlate() +
                            ", COLOR = " + currentVehicleRequest.getColor() +
                            ", STATUS = " + Vehicle.VH_REQ_SENT+ ")";
                    System.out.println("sendVehicleRequest: " + updateExistingRequestQuery);

                    statement = connect.prepareStatement(updateExistingRequestQuery);
                    statement.executeUpdate();
                }
            }

            return true;
        }
        catch (Exception e) {
            System.out.println(LOG_TAG + "Exception:sendVehicleRequest()");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean respondToVehicleRequest(Driver driver) {
        try {
            ArrayList<Vehicle> driversVehicles = driver.getVehicles();

            int numRowsUpdated = 0;

            String approveVehicleRequestQuery =
                    "INSERT INTO DRIVERS_VEHICLES DV\n" +
                            " VALUES(" + driver.getUserID() + ", 0 AS VEHICLE_ID, ?" +
                            ", VRR.NUMBER_PLATE, " + Vehicle.VH_ACCEPTANCE_ACK + ", VRR.COLOR)\n" +
                            " INNER JOIN VEHICLE_REGISTRATION_REQ VRR ON DV.DRIVER_ID = VRR.DRIVER_ID\n" +
                            " WHERE VRR.REGISTRATION_REQ_ID = ?";

            String deleteVehicleRequestQuery =
                    "DELETE FROM VEHICLE_REGISTRATION_REQ" +
                            " WHERE DRIVER_ID = ? AND REGISTRATION_REQ_ID = ?";

            String rejectVehicleRequestQuery = "UPDATE VEHICLE_REGISTRATION_REQ" +
                    " SET STATUS = " + Vehicle.VH_REQ_REJECTED +
                    " WHERE DRIVER_ID = ?" +
                    " AND REGISTRATION_REQ_ID = ?";

            PreparedStatement approveVehicleRequestStatement = connect.prepareStatement(approveVehicleRequestQuery);
            PreparedStatement deleteVehicleRequestStatement = connect.prepareStatement(deleteVehicleRequestQuery);
            PreparedStatement rejectVehicleRequestStatement = connect.prepareStatement(rejectVehicleRequestQuery);

            // Loop through list of driver's vehicles
            for (Vehicle currentVehicle : driversVehicles) {

                // If any records 'dirty' - response received but not persisted, then persist
                if (currentVehicle.getStatus() == Vehicle.VH_REQ_ACCEPTED) {
                    approveVehicleRequestStatement.setInt(1, currentVehicle.getVehicleTypeID());
                    approveVehicleRequestStatement.setInt(2, currentVehicle.getVehicleID());

                    numRowsUpdated = approveVehicleRequestStatement.executeUpdate();

                    if (numRowsUpdated > 0) {
                        deleteVehicleRequestStatement.setInt(1, driver.getUserID());
                        deleteVehicleRequestStatement.setInt(2, currentVehicle.getVehicleID());
                    }
                }
                else if (currentVehicle.getStatus() == Vehicle.VH_REQ_REJECTED) {
                    rejectVehicleRequestStatement.setInt(1, driver.getUserID());
                    rejectVehicleRequestStatement.setInt(2, currentVehicle.getVehicleID());

                    numRowsUpdated = rejectVehicleRequestStatement.executeUpdate();
                }
            }

        }
        catch (Exception e) {
            System.out.println(LOG_TAG + "Exception:approveVehicleRequest()");
            e.printStackTrace();
            return false;
        }
    }

    // Responding to Driver's Request
    @Override
    public boolean respondToDriverRequest(Driver driver) {
        try {
          PreparedStatement sqlQuery = connect.prepareStatement("UPDATE DRIVER_DETAILS SET STATUS = ? WHERE USER_ID = ?");
          sqlQuery.setInt(1, driver.getStatus());
          sqlQuery.setInt(2, driver.getUserID());

          int numRowsUpdated = sqlQuery.executeUpdate();

          return numRowsUpdated > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
