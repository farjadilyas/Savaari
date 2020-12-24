package com.savaari_demo.database;

import com.savaari_demo.entity.*;
import org.apache.commons.dbutils.DbUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OracleDBHandler implements DBHandler {

    private static final String LOG_TAG = OracleDBHandler.class.getSimpleName();

    public OracleDBHandler() {
    }

    /* Methods to close ResultSet, PreparedStatement, and Connection */
    private void closeAll(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {

            closeResultSet(resultSet);
            closeStatement(preparedStatement);
            closeConnection(connection);
    }

    private void closeConnection(Connection connection) {
        try {
            if (connection != null){
            DbUtils.close(connection);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void closeStatement(PreparedStatement preparedStatement) {
        try {
            if (preparedStatement != null) {
                DbUtils.close(preparedStatement);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void closeResultSet(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                DbUtils.close(resultSet);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    /* End of closing util methods*/

    private static int executeUpdate(String query) {

        int numRowsUpdated;

        try {
            Connection connection = DBCPDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            numRowsUpdated = preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
            return numRowsUpdated;
        }
        catch (Exception e) {
            System.out.println("Exception in OracleDBHandler: executeUpdate");
            return -1;
        }
    }

    //Add a new Rider
    @Override
    public Boolean addRider(Rider rider) {

        String q = String.format("INSERT INTO `RIDER_DETAILS` (`USER_ID`, `" +
                        "USER_NAME`, `PASSWORD`, `EMAIL_ADDRESS`, `FIND_STATUS`, `DRIVER_ID`) " +
                        " VALUES(%d, '%s', '%s', '%s', %d, %d)",
                0, rider.getUsername(), rider.getPassword(), rider.getEmailAddress(), RideRequest.NOT_SENT,
                Driver.DEFAULT_ID);
        System.out.println(q);
        return (executeUpdate(q)) > 0;
    }

    @Override
    public Boolean addDriver(Driver driver) {

        return (executeUpdate(String.format("INSERT INTO `DRIVER_DETAILS` (`USER_ID`, `" +
        "USER_NAME`, `PASSWORD`, `EMAIL_ADDRESS`) VALUES(%d, '%s', '%s', '%s')",
                0,
                driver.getUsername(),
                driver.getPassword(),
                driver.getEmailAddress()))) > 0;
    }

    @Override
    public boolean sendRegistrationRequest(Driver driver)
    {
        return (executeUpdate(String.format("UPDATE DRIVER_DETAILS SET FIRST_NAME = '%s'" +
                ", LAST_NAME = '%s', PHONE_NO = '%s', CNIC = '%s', LICENSE_NO = '%s', STATUS = %d WHERE USER_ID = %d",
                driver.getFirstName(), driver.getLastName(), driver.getPhoneNo(), driver.getCNIC(), driver.getLicenseNumber(),
                Driver.DV_REQ_SENT, driver.getUserID()))) > 0;
    }

    @Override
    public Integer loginRider(Rider rider) {

        Connection connect = null;
        ResultSet resultSet = null;
        try {
            String sqlQuery = "SELECT USER_ID FROM RIDER_DETAILS WHERE EMAIL_ADDRESS = '" + rider.getEmailAddress()
                    + "' AND PASSWORD = '" + rider.getPassword() + "'";

            connect = DBCPDataSource.getConnection();
            resultSet = connect.createStatement().executeQuery(sqlQuery);

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
        finally {
            closeAll(connect, null, resultSet);
        }
    }

    @Override
    public Integer loginDriver(Driver driver) {
        Connection connect = null;
        ResultSet resultSet = null;

        try {
            String sqlQuery = "SELECT USER_ID FROM DRIVER_DETAILS WHERE EMAIL_ADDRESS = '" + driver.getEmailAddress()
                    + "' AND PASSWORD = '" + driver.getPassword() + "'";

            connect = DBCPDataSource.getConnection();
            resultSet = connect.createStatement().executeQuery(sqlQuery);

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
        finally {
            closeAll(connect, null, resultSet);
        }
    }

    /* CRUD Operations on User Object */
    @Override
    public JSONArray riderDetails() {
        Connection connect = null;
        ResultSet resultSet = null;

        try {
            String sqlQuery = "SELECT USER_ID, USER_NAME, PASSWORD, EMAIL_ADDRESS FROM RIDER_DETAILS";

            connect = DBCPDataSource.getConnection();
            resultSet = connect.createStatement().executeQuery(sqlQuery);

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
        finally {
            closeAll(connect, null, resultSet);
        }
    }

    @Override
    public JSONArray driverDetails() {
        Connection connect = null;
        ResultSet resultSet = null;

        try {
            String sqlQuery = "SELECT USER_ID, USER_NAME, PASSWORD, EMAIL_ADDRESS FROM DRIVER_DETAILS";

            connect = DBCPDataSource.getConnection();
            resultSet = connect.createStatement().executeQuery(sqlQuery);

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
        finally {
            closeAll(connect, null, resultSet);
        }
    }

    @Override
    public boolean fetchRiderData(Rider rider) {
        Connection connect = null;
        ResultSet resultSet = null;

        try {
            String sqlQuery = "SELECT USER_NAME, EMAIL_ADDRESS FROM RIDER_DETAILS WHERE USER_ID = " + rider.getUserID();

            connect = DBCPDataSource.getConnection();
            resultSet = connect.createStatement().executeQuery(sqlQuery);

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
        finally {
            closeAll(connect, null, resultSet);
        }
    }

    @Override
    public boolean fetchDriverData(Driver driver) {
        Connection connect = null;
        ResultSet resultSet = null;

        try {
            String sqlQuery = "SELECT USER_NAME, FIRST_NAME, LAST_NAME, PHONE_NO, CNIC, LICENSE_NO, EMAIL_ADDRESS, STATUS, IS_ACTIVE, ACTIVE_VEHICLE_ID FROM DRIVER_DETAILS WHERE USER_ID = " + driver.getUserID();

            connect = DBCPDataSource.getConnection();
            resultSet = connect.createStatement().executeQuery(sqlQuery);

            if (resultSet.next()) {

                ArrayList<Vehicle> vehicles = new ArrayList<>();
                Vehicle currentVehicle;

                driver.setUserID(driver.getUserID());
                driver.setUsername(resultSet.getString(1));
                driver.setFirstName(resultSet.getString(2));
                driver.setLastName(resultSet.getString(3));
                driver.setPhoneNo(resultSet.getString(4));
                driver.setCNIC(resultSet.getString(5));
                driver.setLicenseNumber(resultSet.getString(6));
                driver.setEmailAddress(resultSet.getString(7));
                driver.setStatus(resultSet.getInt(8));
                driver.setActive(resultSet.getInt(9) == 1);
                driver.setActiveVehicleID(Integer.parseInt(resultSet.getString(10)));

                closeResultSet(resultSet);

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

                closeResultSet(resultSet);

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
        finally {
            closeAll(connect, null, resultSet);
        }
    }

    @Override
    public boolean resetRider(Rider rider, boolean checkForResponse) {
        return (executeUpdate(
                "UPDATE RIDER_DETAILS SET FIND_STATUS = " + Ride.NOT_SENT +", DRIVER_ID = " + Driver.DEFAULT_ID +
                        " WHERE USER_ID = " + rider.getUserID() +
                        ((checkForResponse)? " AND FIND_STATUS <> " + RideRequest.FOUND : ""))) > 0;
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

        Connection connect = null;
        ResultSet resultSet = null;

        String sqlQuery = "SELECT R.FIND_STATUS " +
                "FROM RIDER_DETAILS R " +
                "WHERE R.FIND_STATUS IN (" + RideRequest.REJECTED + "," + RideRequest.FOUND + ") AND R.USER_ID = " + rider.getUserID();

        int findStatus;
        long currentTime = System.currentTimeMillis();
        long endTime = currentTime + 36000;

        try {
            connect = DBCPDataSource.getConnection();

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
        finally {
            closeAll(connect, null, resultSet);
        }
    }

    @Override
    public ArrayList<Driver> searchDriverForRide(RideRequest rideRequest) {

        Connection connect = null;
        ResultSet resultSet = null;

        try {
            String sqlQuery = "SELECT USER_ID, USER_NAME, CAST(LATITUDE AS CHAR(12)) AS LATITUDE, " +
                    "CAST(LONGITUDE AS CHAR(12)) AS LONGITUDE FROM DRIVER_DETAILS D" +
                    " INNER JOIN DRIVERS_VEHICLES DV ON DV.DRIVER_ID = D.USER_ID AND D.ACTIVE_VEHICLE_ID = DV.VEHICLE_ID" +
                    " INNER JOIN VEHICLE_TYPES VT ON DV.VEHICLE_TYPE_ID = VT.VEHICLE_TYPE_ID" +
                    " WHERE VT.RIDE_TYPE_ID = " + rideRequest.getRideType();

            connect = DBCPDataSource.getConnection();
            resultSet = connect.createStatement().executeQuery(sqlQuery);

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
        finally {
            closeAll(connect, null, resultSet);
        }
    }

    //TODO: add check for driver's vehicle's ride type
    @Override
    public boolean sendRideRequest(RideRequest rideRequest) {

        Connection connect = null;
        PreparedStatement sqlStatement = null;

        boolean requestSent = false;

        try {
            connect = DBCPDataSource.getConnection();
            sqlStatement = connect.prepareStatement(
                    "UPDATE DRIVER_DETAILS SET RIDER_ID = ?, RIDE_STATUS = 1, SOURCE_LAT = ?, SOURCE_LONG = ?," +
                            "DEST_LAT = ?, DEST_LONG = ?, PAYMENT_MODE = ?, RIDE_TYPE = ? WHERE USER_ID = ? AND IS_ACTIVE = 1 AND RIDE_STATUS = 0");

            sqlStatement.setInt(1, rideRequest.getRider().getUserID());
            sqlStatement.setDouble(2, rideRequest.getPickupLocation().getLatitude());
            sqlStatement.setDouble(3, rideRequest.getPickupLocation().getLongitude());
            sqlStatement.setDouble(4, rideRequest.getDropoffLocation().getLatitude());
            sqlStatement.setDouble(5, rideRequest.getDropoffLocation().getLongitude());
            sqlStatement.setInt(6, rideRequest.getPaymentMethod());
            sqlStatement.setInt(7, rideRequest.getRideType());
            sqlStatement.setInt(8, rideRequest.getDriver().getUserID());

            int numRowsUpdated = sqlStatement.executeUpdate();
            if (numRowsUpdated == 1) {
                System.out.println(LOG_TAG +  ":sendRideRequest: 1 row updated -> Request sent!");

                sqlStatement = connect.prepareStatement("UPDATE RIDER_DETAILS SET FIND_STATUS = " + RideRequest.NO_CHANGE +
                        ", DRIVER_ID = " + rideRequest.getDriver().getUserID() + " WHERE USER_ID = ?");
                sqlStatement.setInt(1, rideRequest.getRider().getUserID());

                numRowsUpdated = sqlStatement.executeUpdate();

                if (numRowsUpdated == 1) {
                    System.out.println(LOG_TAG +  ":sendRideRequest: 1 row updated -> Rider marked as NO_CHANGE!");
                    requestSent = true;
                }
                else {
                    System.out.println(LOG_TAG +  ":sendRideRequest: 1 row updated -> failure: Rider NOT marked as NO_CHANGE!");
                    requestSent = false;
                }
            }
            else {
                System.out.println(LOG_TAG + ":sendRideRequest: " + numRowsUpdated + " row updated -> FAILURE!");
                requestSent = false;
            }
            return requestSent;
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler:sendRideRequest()");
            e.printStackTrace();
            return requestSent;
        }
        finally {
            closeAll(connect, sqlStatement, null);
        }
    }
    /* End of section */


    /* Driver-side matchmaking DB calls*/
    @Override
    public boolean markDriverActive(Driver driver)
    {
        return (executeUpdate(String.format("UPDATE DRIVER_DETAILS SET IS_ACTIVE = %s WHERE USER_ID = %d AND STATUS = %d",
                driver.isActive(), driver.getUserID(), Driver.DV_REQ_APPROVED)) > 0);
    }

    @Override
    public RideRequest checkRideRequestStatus(Driver driver, int timeout)
    {
        Connection connect = null;
        PreparedStatement sqlStatement = null;
        ResultSet resultSet = null;

        try {
            connect = DBCPDataSource.getConnection();
            sqlStatement = connect.prepareStatement(
                    "SELECT D.RIDE_STATUS, D.RIDER_ID, R.USER_NAME, D.SOURCE_LAT, D.SOURCE_LONG, D.DEST_LAT, D.DEST_LONG, D.PAYMENT_MODE, D.RIDE_TYPE"
                    + " FROM DRIVER_DETAILS D LEFT JOIN RIDER_DETAILS R ON D.RIDER_ID = R.USER_ID"
                    + " WHERE D.USER_ID = ? AND D.IS_ACTIVE = 1");

            sqlStatement.setInt(1, driver.getUserID());

            // Preparing the Loop
            long currentTime = System.currentTimeMillis();
            long endTime = currentTime + timeout;
            while (currentTime <= endTime)
            {
                // Close result set before executing if appropriate
                closeResultSet(resultSet);
                resultSet = sqlStatement.executeQuery();

                //TODO: why is it returning this when a request isn't present
                RideRequest rideRequest = new RideRequest();
                rideRequest.setFindStatus(33);

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

            return null;
        } // End of Try block
        catch (Exception e) {
            System.out.println("Exception in DBHandler: checkRideRequestStatus()");
            e.printStackTrace();
            return null;
        }
        finally {
            closeAll(connect, sqlStatement, resultSet);
        }
    }


    @Override
    public boolean rejectRideRequest(RideRequest rideRequest) {

        //TODO: Add multiple queries functionality

        int numRowsUpdated = executeUpdate("UPDATE RIDER_DETAILS SET FIND_STATUS = " + RideRequest.REJECTED + ", DRIVER_ID = " + Driver.DEFAULT_ID
                + " WHERE USER_ID = " + rideRequest.getRider().getUserID() + " AND FIND_STATUS = " + RideRequest.NO_CHANGE
                + " AND DRIVER_ID = " + rideRequest.getDriver().getUserID());

        System.out.println("rejectRideRequest: numRowsUpdated: " + numRowsUpdated);
        return (resetDriver(rideRequest.getDriver()));
    }

    /*
     * Confirms ride request (signal to corresponding rider)
     * Records ride
     * */
    @Override
    public boolean confirmRideRequest(Ride ride) {

        Connection connect = null;
        PreparedStatement sqlStatement = null;

        try {
            connect = DBCPDataSource.getConnection();

            // Notify rider query
            sqlStatement = connect.prepareStatement(
                    "UPDATE RIDER_DETAILS SET FIND_STATUS = ?, DRIVER_ID = ? WHERE USER_ID = ? AND FIND_STATUS = "
                            + RideRequest.NO_CHANGE + " AND DRIVER_ID = " + ride.getDriver().getUserID());

            sqlStatement.setInt(1, RideRequest.FOUND);
            sqlStatement.setInt(2, ride.getDriver().getUserID());
            sqlStatement.setInt(3, ride.getRider().getUserID());

            int numRowsUpdated = sqlStatement.executeUpdate();
            if (numRowsUpdated <= 0) {
                resetDriver(ride.getDriver());
                return false;
            }
            closeStatement(sqlStatement);

            // Notify driver query
            sqlStatement = connect.prepareStatement("UPDATE DRIVER_DETAILS SET RIDE_STATUS = ? WHERE USER_ID = ?");
            sqlStatement.setInt(1, RideRequest.MS_REQ_ACCEPTED);
            sqlStatement.setInt(2, ride.getDriver().getUserID());

            numRowsUpdated = sqlStatement.executeUpdate();

            // Returning the confirmation of this query and recording the ride in the DB
            return (numRowsUpdated > 0 && recordRide(ride));
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler: confirmRideRequest()");
            e.printStackTrace();
            resetDriver(ride.getDriver());
            return false;
        }
        finally {
            closeAll(connect, sqlStatement, null);
        }
    }

    @Override
    public boolean markDriverArrival(Ride ride) {
        return (executeUpdate(String.format("UPDATE RIDES SET STATUS = " + Ride.DRIVER_ARRIVED + " WHERE RIDE_ID = %d",
                ride.getRideID())) > 0);
    }

    // Starting the Ride from Driver side
    @Override
    public boolean startRideDriver(Ride ride) {
        return (executeUpdate(String.format("UPDATE RIDES SET STATUS = %d WHERE RIDE_ID = %d",
                Ride.STARTED,
                ride.getRideID())) > 0);
    }

    @Override
    public boolean markArrivalAtDestination(Ride ride) {
        return (executeUpdate("UPDATE RIDES SET STATUS = " + Ride.ARRIVED_AT_DEST + ", DIST_TRAVELLED = " +
                ride.getDistanceTravelled() + ", FARE = " + ride.getFare()
                + ", FINISH_TIME = CURRENT_TIME() WHERE RIDE_ID = " + ride.getRideID()) > 0);
    }

    /* End of section*/

    @Override
    public void recordPayment(Payment payment) {

        Connection connect = null;
        PreparedStatement sqlStatement = null;
        ResultSet generatedKeys = null;
        String insertPaymentQuery = "INSERT INTO PAYMENTS VALUES(NULL, ?, ?, TIMESTAMP = CURRENT_TIME(), ?)";
        String generatedColumns[] = {"PAYMENT_ID"};

        int numRowsUpdated;

        try {
            connect = DBCPDataSource.getConnection();
            sqlStatement = connect.prepareStatement(insertPaymentQuery, generatedColumns);
            sqlStatement.setDouble(1, payment.getAmountPaid());
            sqlStatement.setDouble(2, payment.getChange());
            sqlStatement.setInt(3, payment.getPaymentMode());

            numRowsUpdated = sqlStatement.executeUpdate();

            if (numRowsUpdated == 0) {
                System.out.println(LOG_TAG + ":recordPayment: creating payment failed, no rows updated");
            }
            else {
                generatedKeys = sqlStatement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    payment.setPaymentID(generatedKeys.getInt(1));
                }
                else {
                    System.out.println(LOG_TAG + ":recordPayment: creating payment failed, no payment id obtained");
                }
            }
        }
        catch (Exception e) {
            System.out.println(LOG_TAG + ":recordPayment: SQLException");
            e.printStackTrace();
        }
        finally {
            closeAll(connect, sqlStatement, generatedKeys);
        }
    }


    // Store new Ride
    @Override
    public boolean recordRide(Ride ride) {

        String query = "INSERT INTO RIDES " +
                "SELECT 0, R.USER_ID AS RIDER_ID, D.USER_ID AS DRIVER_ID, D.ACTIVE_VEHICLE_ID AS VEHICLE_ID, NULL AS PAYMENT_ID, " +
                "D.SOURCE_LAT, D.SOURCE_LONG, D.DEST_LAT, D.DEST_LONG, CURRENT_TIME(), NULL AS FINISH_TIME, 0 AS DIST_TRAVELLED, " +
                "D.RIDE_TYPE AS RIDE_TYPE, 0 AS ESTIMATED_FARE, 0 AS FARE, " + Ride.PICKUP + " AS STATUS " +
                "FROM DRIVER_DETAILS AS D, RIDER_DETAILS AS R " +
                "WHERE D.RIDER_ID = R.USER_ID AND D.USER_ID = " + ride.getDriver().getUserID() +
                " AND D.RIDER_ID = " + ride.getRider().getUserID();

        return (executeUpdate(query) > 0);
    }

    @Override
    public RideRequest checkRideRequestStatus(Rider rider) {

        Connection connect = null;
        ResultSet resultSet  =null;

        String sqlQuery = "SELECT FIND_STATUS, DRIVER_ID FROM RIDER_DETAILS WHERE USER_ID = " + rider.getUserID();
        //TODO: make constants for ride request status

        try {
            connect = DBCPDataSource.getConnection();
            resultSet = connect.createStatement().executeQuery(sqlQuery);
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
        finally {
            closeAll(connect, null, resultSet);
        }
    }
    public Ride getRide(RideRequest rideRequest) {

        Connection connect = null;
        ResultSet resultSet = null;

        String sqlQuery = "SELECT RD.RIDE_ID, R.USER_NAME, D.USER_NAME, RD.PAYMENT_ID, RD.SOURCE_LAT, RD.SOURCE_LONG, " +
                "RD.DEST_LAT, RD.DEST_LONG, RD.START_TIME, RD.RIDE_TYPE, RD.ESTIMATED_FARE, RD.STATUS, D.LATITUDE, D.LONGITUDE, " +
                "RD.FARE, D.ACTIVE_VEHICLE_ID, V.MAKE, V.MODEL, V.YEAR, DV.NUMBER_PLATE, DV.COLOR, R.RATING, D.RATING," +
                " D.FIRST_NAME, D.LAST_NAME, D.PHONE_NO\n" +
                " FROM RIDES RD\n" +
                " INNER JOIN RIDER_DETAILS R ON RD.RIDER_ID = R.USER_ID" +
                " INNER JOIN DRIVER_DETAILS D ON RD.DRIVER_ID = D.USER_ID" +
                " INNER JOIN DRIVERS_VEHICLES DV ON D.USER_ID = DV.DRIVER_ID AND D.ACTIVE_VEHICLE_ID = DV.VEHICLE_ID" +
                " INNER JOIN VEHICLE_TYPES V ON DV.VEHICLE_TYPE_ID = V.VEHICLE_TYPE_ID" +
                " WHERE RD.RIDER_ID = " + rideRequest.getRider().getUserID() +
                " AND RD.DRIVER_ID = " + rideRequest.getDriver().getUserID() +
                " AND RD.STATUS <> " + Ride.END_ACKED;

        System.out.println("getRide(): " + sqlQuery);

        //JSONObject result = new JSONObject();
        Ride ride = null;

        try {
            connect = DBCPDataSource.getConnection();
            resultSet = connect.createStatement().executeQuery(sqlQuery);

            // Package results into ride object
            if (resultSet.next()) {

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

                Vehicle vehicle = new Vehicle();
                vehicle.setVehicleID(resultSet.getInt(16));
                vehicle.setMake(resultSet.getString(17));
                vehicle.setModel(resultSet.getString(18));
                vehicle.setYear(resultSet.getString(19));
                vehicle.setNumberPlate(resultSet.getString(20));
                vehicle.setColor(resultSet.getString(21));

                ride.getRider().setRating(resultSet.getFloat(22));
                ride.getDriver().setRating(resultSet.getFloat(23));
                ride.getDriver().setFirstName(resultSet.getString(24));
                ride.getDriver().setLastName(resultSet.getString(25));
                ride.getDriver().setPhoneNo(resultSet.getString(26));

                ride.setVehicle(vehicle);
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
        finally {
            closeAll(connect, null, resultSet);
        }
    }

    @Override
    public void getRideStatus(Ride ride) {

        Connection connect = null;
        ResultSet resultSet = null;

        ride.setRideStatus(Ride.DEFAULT);
        String sqlQuery = "SELECT STATUS, FARE FROM RIDES WHERE RIDE_ID = " + ride.getRideID();

        try {
            connect = DBCPDataSource.getConnection();
            resultSet = connect.createStatement().executeQuery(sqlQuery);

            if (resultSet.next()) {
                ride.setRideStatus(resultSet.getInt(1));
                ride.setFare(resultSet.getDouble(2));
            }
        }
        catch (Exception e) {
            System.out.println("Exception in DBHandler: getRide()");
            e.printStackTrace();
        }
        finally {
            closeAll(connect, null, resultSet);
        }
    }

    /*
    * Param: Ride with Payment - paymentID must be initialized
    * Adds reference from ride to payment, persists payment information
    */
    @Override
    public boolean endRideWithPayment(Ride ride)
    {
        Connection connect = null;
        PreparedStatement sqlStatement = null;
        try {
            if (ride.getPayment().getPaymentID() < 0) {
                return false;
            }

            connect = DBCPDataSource.getConnection();
            sqlStatement = connect.prepareStatement(
                    "UPDATE RIDES SET PAYMENT_ID  = " + ride.getPayment().getPaymentID() +
                            ", STATUS = " + Ride.PAYMENT_MADE +
                            " WHERE RIDE_ID = ?");
            sqlStatement.setInt(1, ride.getRideID());
            int numRowsUpdated = sqlStatement.executeUpdate();

            closeStatement(sqlStatement);

            if (numRowsUpdated > 0) {
                sqlStatement = connect.prepareStatement("UPDATE PAYMENTS P INNER JOIN RIDES RD ON RD.PAYMENT_ID = P.PAYMENT_ID SET P.AMOUNT_PAID = ?, P.CHANGE = ? - RD.FARE WHERE RD.RIDE_ID = ?");

                sqlStatement.setDouble(1, ride.getPayment().getAmountPaid());
                sqlStatement.setDouble(2, ride.getPayment().getAmountPaid());
                sqlStatement.setInt(3, ride.getRideID());

                numRowsUpdated = sqlStatement.executeUpdate();

                return (numRowsUpdated > 0 && resetDriver(ride.getDriver()));

            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("Exception in DBHandler: endRideWitPayment");
            e.printStackTrace();
            return false;
        }
        finally {
            closeAll(connect, sqlStatement, null);
        }
    }

    @Override
    public boolean acknowledgeEndOfRide(Ride ride) {

        return (executeUpdate(String.format("UPDATE RIDES SET STATUS = %d WHERE RIDE_ID = %d",
                Ride.END_ACKED,
                ride.getRideID())) > 0);
    }

    /* TODO: Keep track of ratings in ride later? */
    @Override
    public boolean giveFeedbackForDriver(Ride ride, float rating) {

        return (executeUpdate("UPDATE DRIVER_DETAILS D" +
                " SET D.RATING = D.RATING*(cast(D.NUM_RATINGS as DECIMAL)/CAST(D.NUM_RATINGS+1 AS DECIMAL)) + ("
                + rating +"/CAST(D.NUM_RATINGS+1 AS DECIMAL)), " +
                " D.NUM_RATINGS = D.NUM_RATINGS + 1" +
                " WHERE D.USER_ID = " + ride.getDriver().getUserID()) > 0);
    }

    @Override
    public boolean giveFeedbackForRider(Ride ride, float rating) {

        return (executeUpdate("UPDATE RIDER_DETAILS R" +
                " SET R.RATING = R.RATING*(cast(R.NUM_RATINGS as DECIMAL)/CAST(R.NUM_RATINGS+1 AS DECIMAL)) + ("
                + rating +"/CAST(R.NUM_RATINGS+1 AS DECIMAL)), " +
                " R.NUM_RATINGS = R.NUM_RATINGS + 1" +
                " WHERE R.USER_ID = " + ride.getRider().getUserID()) > 0);
    }

    @Override
    public boolean resetDriver(Driver driver) {

        return (executeUpdate(
                "UPDATE DRIVER_DETAILS SET RIDE_STATUS = "  + RideRequest.MS_DEFAULT + ", RIDER_ID = " +
                        User.DEFAULT_ID + ", SOURCE_LAT = 0, SOURCE_LONG = 0, DEST_LAT = 0, DEST_LONG = 0, " +
                        " PAYMENT_MODE = -1, RIDE_TYPE = 0 WHERE USER_ID = " + driver.getUserID()) > 0);
    }
    /* End of section */


    /* Location update methods*/
    @Override
    public boolean saveRiderLocation(Rider rider) {

        return (executeUpdate("UPDATE RIDER_DETAILS SET LATITUDE = " + rider.getCurrentLocation().getLatitude()
                + ", LONGITUDE = " + rider.getCurrentLocation().getLongitude() +
                ", TIMESTAMP = CURRENT_TIME() WHERE USER_ID = " + rider.getUserID()) > 0);
    }

    @Override
    public boolean saveDriverLocation(Driver driver) {
        return (executeUpdate("UPDATE DRIVER_DETAILS SET LATITUDE = " + driver.getCurrentLocation().getLatitude()
                + ", LONGITUDE = " + driver.getCurrentLocation().getLongitude() +
                ", TIMESTAMP = CURRENT_TIME() WHERE USER_ID = " + driver.getUserID()) > 0);
    }

    @Override
    public Location getRiderLocation(Rider rider) {

        Connection connect = null;
        ResultSet resultSet = null;
        try {
            connect = DBCPDataSource.getConnection();
            String sqlQuery = "SELECT CAST(LATITUDE AS CHAR(12)) AS LATITUDE, CAST(LONGITUDE AS CHAR(12)) AS LONGITUDE" +
                    " FROM RIDER_DETAILS WHERE USER_ID = " + rider.getUserID();

            resultSet = connect.createStatement().executeQuery(sqlQuery);

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
        finally {
            closeAll(connect, null, resultSet);
        }
    }

    @Override
    public Location getDriverLocation(Driver driver) {

        Connection connect = null;
        ResultSet resultSet = null;

        try {
            connect = DBCPDataSource.getConnection();
            String sqlQuery = "SELECT CAST(LATITUDE AS CHAR(12)) AS LATITUDE, CAST(LONGITUDE AS CHAR(12)) AS LONGITUDE" +
                    " FROM DRIVER_DETAILS WHERE USER_ID = " + driver.getUserID();

            resultSet = connect.createStatement().executeQuery(sqlQuery);

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
        finally {
            closeAll(connect, null, resultSet);
        }
    }

    @Override
    public ArrayList<Location> getRiderLocations() {

        Connection connect = null;
        ResultSet resultSet = null;

        try {
            String sqlQuery = "SELECT CAST(LATITUDE AS CHAR(12)) AS LATITUDE, CAST(LONGITUDE AS CHAR(12)) AS LONGITUDE" +
                    ", TIMESTAMP FROM RIDER_DETAILS";

            // Find list of Rider Locations //TODO: Add criteria later

            connect = DBCPDataSource.getConnection();
            resultSet = connect.createStatement().executeQuery(sqlQuery);

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
        finally {
            closeAll(connect, null, resultSet);
        }
    }

    @Override
    public ArrayList<Location> getDriverLocations() {

        Connection connect = null;
        ResultSet resultSet = null;

        try {
            String sqlQuery = "SELECT CAST(LATITUDE AS CHAR(12)) AS LATITUDE, CAST(LONGITUDE AS CHAR(12)) AS LONGITUDE" +
                    ", TIMESTAMP FROM DRIVER_DETAILS";

            // Find list of Driver Locations //TODO: Add criteria later
            connect = DBCPDataSource.getConnection();
            resultSet = connect.createStatement().executeQuery(sqlQuery);

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
        finally {
            closeAll(connect, null, resultSet);
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

        //TODO: don't make new statement every time

        Connection connect = null;
        PreparedStatement statement = null;

        try {
            ArrayList<Vehicle> driversVehicles = driver.getVehicles();

            String sendNewRequestQuery, updateExistingRequestQuery;

            connect = DBCPDataSource.getConnection();

            for (Vehicle currentVehicleRequest : driversVehicles) {

                // If request hasn't been sent
                if (currentVehicleRequest.getStatus() == Vehicle.VH_DEFAULT) {
                    sendNewRequestQuery = "INSERT INTO VEHICLE_REGISTRATION_REQ" +
                            " VALUES(" + driver.getUserID() +", 0, '" + currentVehicleRequest.getMake() +
                            "', '" + currentVehicleRequest.getModel() + "', '" + currentVehicleRequest.getYear() +
                        "', '" + currentVehicleRequest.getNumberPlate() + "', '" + currentVehicleRequest.getColor() +
                        "', " + Vehicle.VH_REQ_SENT+ ")";
                    System.out.println("sendVehicleRequest: " + sendNewRequestQuery);

                    statement = connect.prepareStatement(sendNewRequestQuery);
                    statement.executeUpdate();
                    closeStatement(statement);

                }
                // Previously rejected request
                else if (currentVehicleRequest.getStatus() == Vehicle.VH_REQ_REJECTED) {
                    updateExistingRequestQuery = "UPDATE VEHICLE_REGISTRATION_REQ" +
                            " SET MAKE = '" + currentVehicleRequest.getMake() +
                            "', MODEL = '" + currentVehicleRequest.getModel() +
                            "', YEAR = '" + currentVehicleRequest.getYear() +
                            "', NUMBER_PLATE = '" + currentVehicleRequest.getNumberPlate() +
                            "', COLOR = '" + currentVehicleRequest.getColor() +
                            "', STATUS = " + Vehicle.VH_REQ_SENT+
                            " WHERE DRIVER_ID = " + driver.getUserID() +
                            " AND REGISTRATION_REQ_ID = " + currentVehicleRequest.getVehicleID();

                    System.out.println("sendVehicleRequest: " + updateExistingRequestQuery);

                    statement = connect.prepareStatement(updateExistingRequestQuery);
                    statement.executeUpdate();
                    closeStatement(statement);
                }
            }

            return true;
        }
        catch (Exception e) {
            System.out.println(LOG_TAG + "Exception:sendVehicleRequest()");
            e.printStackTrace();
            return false;
        }
        finally {
            closeAll(connect, statement, null);
        }
    }

    @Override
    public boolean respondToVehicleRequest(Driver driver) {

        Connection connect = null;
        PreparedStatement approveVehicleRequestStatement = null,
                deleteVehicleRequestStatement = null,
                rejectVehicleRequestStatement = null;
        try {
            ArrayList<Vehicle> driversVehicles = driver.getVehicles();

            int numRowsUpdated;

            String approveVehicleRequestQuery =
                    "INSERT INTO DRIVERS_VEHICLES\n" +
                    " SELECT " + driver.getUserID() + ", 0, ?,VRR.NUMBER_PLATE, " + Vehicle.VH_ACCEPTANCE_ACK + ", VRR.COLOR" +
                    " FROM VEHICLE_REGISTRATION_REQ VRR" +
                    " WHERE VRR.DRIVER_ID = " + driver.getUserID() + " AND VRR.REGISTRATION_REQ_ID = ?";

            String deleteVehicleRequestQuery =
                    "DELETE FROM VEHICLE_REGISTRATION_REQ" +
                            " WHERE DRIVER_ID = ? AND REGISTRATION_REQ_ID = ?";

            String rejectVehicleRequestQuery = "UPDATE VEHICLE_REGISTRATION_REQ" +
                    " SET STATUS = " + Vehicle.VH_REQ_REJECTED +
                    " WHERE DRIVER_ID = ?" +
                    " AND REGISTRATION_REQ_ID = ?";

            connect = DBCPDataSource.getConnection();
            approveVehicleRequestStatement = connect.prepareStatement(approveVehicleRequestQuery);
            deleteVehicleRequestStatement = connect.prepareStatement(deleteVehicleRequestQuery);
            rejectVehicleRequestStatement = connect.prepareStatement(rejectVehicleRequestQuery);

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

                        deleteVehicleRequestStatement.executeUpdate();
                    }
                }
                else if (currentVehicle.getStatus() == Vehicle.VH_REQ_REJECTED) {
                    rejectVehicleRequestStatement.setInt(1, driver.getUserID());
                    rejectVehicleRequestStatement.setInt(2, currentVehicle.getVehicleID());

                    rejectVehicleRequestStatement.executeUpdate();
                }
            }

            return true;

        }
        catch (Exception e) {
            System.out.println(LOG_TAG + "Exception:respondToVehicleRequest()");
            e.printStackTrace();
            return false;
        }
        finally {
            closeStatement(approveVehicleRequestStatement);
            closeStatement(deleteVehicleRequestStatement);
            closeStatement(rejectVehicleRequestStatement);
            closeConnection(connect);
        }
    }

    @Override
    public boolean setActiveVehicle(Driver driver) {

        Connection connect = null;
        PreparedStatement setActiveVehicleStatement = null;
        ResultSet resultSet = null;

        try {
            // Check if vehicle still approved
            String checkVehicleApprovedQuery =
                    "SELECT STATUS FROM DRIVERS_VEHICLES WHERE DRIVER_ID = " + driver.getUserID() + " AND VEHICLE_ID = " + driver.getActiveVehicleID();

            connect = DBCPDataSource.getConnection();
            resultSet = connect.createStatement().executeQuery(checkVehicleApprovedQuery);

            // If approved, set as driver's active vehicle
            if (resultSet.next() && resultSet.getInt(1) == Vehicle.VH_ACCEPTANCE_ACK) {
                setActiveVehicleStatement = connect.prepareStatement("UPDATE DRIVER_DETAILS SET ACTIVE_VEHICLE_ID = " + driver.getActiveVehicleID() +
                        " WHERE USER_ID  = " + driver.getUserID());

                // Return true if a row is updated
                return (setActiveVehicleStatement.executeUpdate() > 0);
            }
            else {
                return false;
            }
        }
        catch (Exception e) {
            System.out.println(LOG_TAG + "Exception:setActiveVehicle()");
            e.printStackTrace();
            return false;
        }
        finally {
            closeAll(connect, setActiveVehicleStatement, resultSet);
        }
    }

    // Responding to Driver's Request
    @Override
    public boolean respondToDriverRequest(Driver driver) {

        return (executeUpdate(String.format("UPDATE DRIVER_DETAILS SET STATUS = %d WHERE USER_ID = %d",
                driver.getStatus(),
                driver.getUserID())) > 0);
    }
}