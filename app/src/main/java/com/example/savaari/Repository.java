package com.example.savaari;

import android.accounts.NetworkErrorException;
import android.util.Log;

import com.example.savaari.services.network.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executor;

public class Repository {
    private static final String url = "https://56cc03fd597d.ngrok.io/";
    private Executor executor;

    Repository(Executor executor) {
        this.executor = executor;
    }


    /*
     *   SET OF RIDER-SIDE MATCHMAKING FUNCTIONS ----------------------------------------------------
     */
    public void findDriver(OnDataLoadedListener callback, int currentUserID, double latitude, double longitude) {
        executor.execute(() -> callback.onDataLoaded(NetworkUtil.findDriver(url, currentUserID, latitude, longitude)));
    }

    /*
     * Checks FIND_STATUS for rider
     * 0 -> Invalid request
     * 1 -> Driver hasn't responded
     * 2 -> Driver accepted request
     * */
    public void checkFindStatus(OnDataLoadedListener callback, int currentUserID) {
        executor.execute(() -> callback.onDataLoaded(NetworkUtil.checkFindStatus(url, currentUserID)));
    }
    /*
     *  END OF RIDER-SIDE MATCHMAKING FUNCTIONS -----------------------------------------------------
     */


    // Sign-Up
    public void signup(OnDataLoadedListener callback, String username, String emailAddress, String password) throws JSONException {
        executor.execute(() -> callback.onDataLoaded(NetworkUtil.signup(url, username, emailAddress, password)));
    }
    // Login
    public void login(OnDataLoadedListener callback, String username, String password) {
        executor.execute(() -> callback.onDataLoaded(NetworkUtil.login(url, username, password)));
    }
    // Loading User Data
    public void loadUserData(OnDataLoadedListener callback, int currentUserID) {
        executor.execute(() -> callback.onDataLoaded(NetworkUtil.loadUserData(url, currentUserID)));
    }

    // Get User Locations
    public void getUserLocations(OnDataLoadedListener callback) {
        executor.execute(() -> callback.onDataLoaded(NetworkUtil.getUserLocations(url)));
    }

    // Send Last Location
    public void sendLastLocation(OnDataLoadedListener callback, int currentUserID, double latitude, double longitude) {
        executor.execute(() ->
                callback.onDataLoaded(NetworkUtil.sendLastLocation(url, currentUserID, latitude, longitude)));
    }
}
