package com.example.savaari;

import com.example.savaari.services.network.NetworkUtil;
import com.example.savaari.services.network.OnDataLoadedListener;

import java.util.concurrent.Executor;

public class Repository {
    private static final String url = "https://12c3377db210.ngrok.io/";
    private Executor executor;

    Repository(Executor executor) {
        this.executor = executor;
    }


    //Find Driver
    public void findDriver(OnDataLoadedListener callback, int currentUserID, double latitude, double longitude) {
        executor.execute(() -> callback.onDataLoaded(NetworkUtil.findDriver(url, currentUserID, latitude, longitude)));
    }

    // Sign-Up
    public void signup(OnDataLoadedListener callback, String nickname, String username, String password) {
        executor.execute(() -> callback.onDataLoaded(NetworkUtil.signup(url, nickname, username, password)));
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
    public void sendLastLocation(int currentUserID, double latitude, double longitude) {
        executor.execute(() ->
                NetworkUtil.sendLastLocation(url, currentUserID, latitude, longitude));
    }
}
