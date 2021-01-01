package com.example.savaari;

import android.util.Log;

import com.example.savaari.services.network.NetworkUtil;
import com.example.savaari.services.network.OnDataLoadedListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Executor;

public class Repository {
    private static String url;
    private Executor executor;

    Repository(Executor executor) {
        this.executor = executor;
        url = loadDataSourceUrl();
        Log.d("Repository url: ", url);
    }

    public String loadDataSourceUrl() {
        Properties prop;
        String propFileName = "config.properties";
        InputStream inputStream;

        try {
            prop = new Properties();
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            // Get property value
            return prop.getProperty("dataSourceUrl");
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    //Find Driver
    public void findDriver(OnDataLoadedListener callback, int currentUserID, double srcLatitude,
                           double srcLongitude, double destLatitude, double destLongitude, int paymentMode, int rideType) {
        
        executor.execute(() -> callback.onDataLoaded(NetworkUtil.getInstance().findDriver(url, currentUserID,
                srcLatitude, srcLongitude, destLatitude, destLongitude, paymentMode, rideType)));
    }

    // Sign-Up
    public void signup(OnDataLoadedListener callback, String nickname, String username, String password) {
        executor.execute(() -> callback.onDataLoaded(NetworkUtil.getInstance().signup(url, nickname, username, password)));
    }
    // Login
    public void login(OnDataLoadedListener callback, String username, String password) {
        executor.execute(() -> callback.onDataLoaded(NetworkUtil.getInstance().login(url, username, password)));
    }

    public void persistLogin(OnDataLoadedListener callback, Integer userID) {
        executor.execute(() -> callback.onDataLoaded(NetworkUtil.getInstance().persistLogin(url, userID)));
    }

    public void logout(OnDataLoadedListener callback, Integer userID) {
        executor.execute(() -> callback.onDataLoaded(NetworkUtil.getInstance().logout(url, userID)));
    }
    // Loading User Data
    public void loadUserData(OnDataLoadedListener callback, int currentUserID) {
        executor.execute(() -> callback.onDataLoaded(NetworkUtil.getInstance().loadUserData(url, currentUserID)));
    }

    // Get User Locations
    public void getUserLocations(OnDataLoadedListener callback) {
        executor.execute(() -> callback.onDataLoaded(NetworkUtil.getInstance().getUserLocations(url)));
    }

    // Send Last Location
    public void sendLastLocation(int currentUserID, double latitude, double longitude) {
        executor.execute(() ->
                NetworkUtil.getInstance().sendLastLocation(url, currentUserID, latitude, longitude));
    }

    public void getDriverLocation(OnDataLoadedListener callback, int driverID) {
        callback.onDataLoaded(NetworkUtil.getInstance().getDriverLocation(url, driverID));
    }

    public void getRide(OnDataLoadedListener callback, int riderID) {
        executor.execute(() -> callback.onDataLoaded(NetworkUtil.getInstance().getRide(url, riderID)));
    }

    public void getRideStatus(OnDataLoadedListener callback, int rideID) {
        callback.onDataLoaded(NetworkUtil.getInstance().getRideStatus(url, rideID));
    }

    public void acknowledgeEndOfRide(OnDataLoadedListener callback, int rideID, int riderID) {
        executor.execute(() -> callback.onDataLoaded(NetworkUtil.getInstance().acknowledgeEndOfRide(url, rideID, riderID)));
    }

    public void giveFeedbackForDriver(OnDataLoadedListener callback, int rideID, int driverID, float rating) {
        executor.execute(() -> callback.onDataLoaded(NetworkUtil.getInstance().giveFeedbackForDriver(url, rideID, driverID, rating)));
    }
}
