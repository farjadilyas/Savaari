package com.savaari_demo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class OracleDBHandler implements DBHandler {

    private Connection connect = null;
    private static OracleDBHandler instance = new OracleDBHandler();    //single instance

    // Return single instance
    public OracleDBHandler getInstance() {
        return instance;
    }

    OracleDBHandler() {
        try {
            // Get MySql driver
            Class.forName("com.mysql.jdbc.Driver");

            // Connect to database
            connect = DriverManager.getConnection("jdbc:mysql://"
                    + "localhost/ride_hailing" + "/feedback?user=" + "root"
                    + "&password=" + "thisIsOurSDAProjectPassword");
        }
        catch (Exception e) {
            System.out.println("Exception in drverManager.getConnection()");
            e.printStackTrace();
        }
    }

    //Add a new Rider
    @Override
    public Boolean addRider(Rider rider) {
        try {
            PreparedStatement sqlQuery = connect.prepareStatement("INSERT INTO `RIDER_DETAILS` (`USER_ID`, `" +
                    "USER_NAME`, `PASSWORD`, `EMAIL_ADDRESS`) VALUES (?, ?, ?, ?)");

            sqlQuery.setInt(1, rider.getUserID());
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

            sqlQuery.setInt(1, driver.getUserID());
            sqlQuery.setString(2, driver.getUsername());
            sqlQuery.setString(3, driver.getPassword());
            sqlQuery.setString(4, driver.getEmailAddress());

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
    public Integer loginRider(Rider rider) {
        try {
            String sqlQuery = "SELECT USER_ID FROM RIDER_DETAILS WHERE EMAIL_ADDRESS = " + rider.getEmailAddress()
                    + " AND PASSWORD = " + rider.getPassword();

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

    @Override
    public Integer loginDriver(Driver driver) {
        try {
            String sqlQuery = "SELECT USER_ID FROM DRIVER_DETAILS WHERE EMAIL_ADDRESS = " + driver.getEmailAddress()
                    + " AND PASSWORD = " + driver.getPassword();

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

    @Override
    public JSONArray riderDetails() {
        try {
            String sqlQuery = "SELECT USER_ID, USER_NAME, PASSWORD, EMAIL_ADDRESS FROM RIDER_DETAILS";

            ResultSet resultSet = connect.createStatement().executeQuery(sqlQuery);

            int numColumns = resultSet.getMetaData().getColumnCount();
            JSONArray result = new JSONArray();
            JSONObject row = new JSONObject();

            while (resultSet.next()) {
                row.put("USER_ID", resultSet.getInt(1));
                row.put("USER_NAME", resultSet.getInt(2));
                row.put("PASSWORD", resultSet.getInt(3));
                row.put("EMAIL_ADDRESS", resultSet.getInt(4));
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
    public JSONArray driverDetails() {
        try {
            String sqlQuery = "SELECT USER_ID, USER_NAME, PASSWORD, EMAIL_ADDRESS FROM DRIVER_DETAILS";

            ResultSet resultSet = connect.createStatement().executeQuery(sqlQuery);

            int numColumns = resultSet.getMetaData().getColumnCount();
            JSONArray result = new JSONArray();
            JSONObject row = new JSONObject();

            while (resultSet.next()) {
                row.put("USER_ID", resultSet.getInt(1));
                row.put("USER_NAME", resultSet.getInt(2));
                row.put("PASSWORD", resultSet.getInt(3));
                row.put("EMAIL_ADDRESS", resultSet.getInt(4));
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

            if (resultSet.next()) {
                Rider result = new Rider();
                result.setUserID(rider.getUserID());
                result.setUsername(resultSet.getString(1));
                result.setEmailAddress(resultSet.getString(2));
                return result;
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
            String sqlQuery = "SELECT USER_NAME, EMAIL_ADDRESS FROM DRIVER_DETAILS WHERE USER_ID = %s";
            ResultSet resultSet = connect.createStatement().executeQuery(sqlQuery);

            if (resultSet.next()) {
                Driver result = new Driver();
                result.setUserID(driver.getUserID());
                result.setUsername(resultSet.getString(1));
                result.setEmailAddress(resultSet.getString(2));
                return result;
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

    @Override
    public JSONObject checkFindStatus(Rider rider) {
        return null;
    }

    @Override
    public JSONArray searchDriverForRide() {
        return null;
    }

    @Override
    public boolean sendRideRequest() {
        return false;
    }
}
