package com.example.savaari.ride;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.savaari.Repository;
import com.example.savaari.user.Driver;
import com.example.savaari.user.UserLocation;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class RideViewModel extends ViewModel {

    private static String LOG_TAG = RideViewModel.class.getSimpleName();
    private final Repository repository;

    /* CCredentials for netowrk operations */
    private int USER_ID = -1;

    /* User account data*/
    private LatLng userCoordinates;
    private Ride ride;

    /* User locations data for pinging */
    private ArrayList<UserLocation> mUserLocations = new ArrayList<>();

    /* Data Loaded status flags */
    MutableLiveData<Boolean> userDataLoaded = new MutableLiveData<>(false);
    MutableLiveData<Boolean> userLocationsLoaded = new MutableLiveData<>(false);
    MutableLiveData<Boolean> driverLocationFetched = new MutableLiveData<>(false);
    MutableLiveData<Driver> pairedDriver = new MutableLiveData<>();


    public RideViewModel(int USER_ID, Repository repository) {
        this.USER_ID = USER_ID;
        this.repository = repository;
        ride = new Ride();
    }

    /* Get user data */
    public LatLng getUserCoordinates() {
        return userCoordinates;
    }
    public ArrayList<UserLocation> getUserLocations() {
        return mUserLocations;
    }
    public Ride getRide() { return ride; }

    /* Set USER_ID */
    //public void setUserID(int USER_ID) { this.USER_ID = USER_ID; }

    /* Return LiveData to observe Data Loaded Flags */
    public LiveData<Boolean> isLiveUserDataLoaded() {
        return userDataLoaded;
    }
    public LiveData<Boolean> isLiveUserLocationsLoaded() { return userLocationsLoaded; }
    public LiveData<Boolean> isDriverLocationFetched() { return driverLocationFetched; }
    public LiveData<Driver> isDriverPaired() { return pairedDriver; }


    /* Need a setter since coordinates are received from activity */
    public void setUserCoordinates(double latitude, double longitude) {
        userCoordinates = new LatLng(latitude, longitude);
    }

    public void loadUserData() {
        repository.loadUserData(object -> {
            JSONObject result = null;
            try {
                if (object == null) {
                    Log.d(LOG_TAG, "onDataLoaded(): resultString is null");
                    userDataLoaded.postValue(false);
                }
                else {
                    result = (JSONObject) object;

                    ride.getRider().setUsername(result.getString("USER_NAME"));
                    ride.getRider().setEmailAddress(result.getString("EMAIL_ADDRESS"));
                    Log.d("loadUserData(): ", result.getString("USER_NAME"));
                    userDataLoaded.postValue(true);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                userDataLoaded.postValue(false);
                Log.d(LOG_TAG, "onDataLoaded(): exception thrown" + result.toString());
            }
        }, USER_ID);
    }

    public void fetchDriverLocation() {
        Log.d(LOG_TAG, "fetchDriverLocation called!");
        int driverID = ride.getDriver().getUserID();

        if (driverID > 0) {
            repository.getDriverLocation(object -> {
                if (object != null) {
                    try {
                        JSONObject result = (JSONObject) object;
                        if (result.getInt("STATUS_CODE") == 200) {

                            Log.d(LOG_TAG, " fetchDriverLocation: Got driver location!");
                            ride.getDriver().setCurrentLocation(new LatLng(result.getDouble("LATITUDE"),
                                    result.getDouble("LONGITUDE")));
                            driverLocationFetched.postValue(true);
                        }
                        else {
                            Log.d(LOG_TAG, " fetchDriverLocation: Failed to fetch driver location");
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        Log.d(LOG_TAG, "fetchDriverLocation(): Exception");
                    }
                }
            }, driverID);
        }
    }


    /* loads array of UserLocation*/
    public void loadUserLocations() {
        if (!userLocationsLoaded.getValue()) {

            repository.getUserLocations(object -> {
                try {
                    JSONArray resultArray = (JSONArray) object;
                    Log.d(TAG, "loadUserLocations: " + resultArray.toString());
                    if (resultArray != null)
                    {
                        Log.d(TAG, "loadUserLocations: found JSON Array");
                        for (int i = 0; i < resultArray.length(); i++) {
                            JSONObject obj = resultArray.getJSONObject(i);
                            UserLocation userLocation = new UserLocation();

                            // Assigning User Objects
                            //userLocation.setUserID(obj.getInt("USER_ID"));
                            userLocation.setLatitude(obj.getDouble("LATITUDE"));
                            userLocation.setLongitude(obj.getDouble("LONGITUDE"));
                            //userLocation.setTimestamp(obj.getString("TIMESTAMP"));

                            // Adding Final Object
                            mUserLocations.add(userLocation);
                            Log.d(TAG, "loadUserLocations: userID: " + userLocation.getUserID());
                            Log.d(TAG, "loadUserLocations: latitude: " + userLocation.getLatitude());
                            Log.d(TAG, "loadUserLocations: longitude: " + userLocation.getLongitude());
                            Log.d(TAG, "loadUserLocations: timestamp: " + userLocation.getTimestamp());
                        }
                    }
                    userLocationsLoaded.postValue(true);
                }
                catch (Exception e)
                {
                    Log.d(TAG, "Exception in loadUserLocations");
                    e.printStackTrace();
                    userLocationsLoaded.postValue(false);
                }
            });
        }
        else {
            userLocationsLoaded.postValue(true);
        }
    }

    /*
    * sets driver status to:
    * PAIRED -> If driver has been matched
    * NOT_PAIRED -> If drivers available but all declined
    * NOT_FOUND -> If drivers not available
    * */
    public void findDriver(int USER_ID, LatLng pickupLocation, LatLng dropoffLocation) {

        repository.findDriver(object -> {
            JSONObject result;
            Driver driver = new Driver();

            try {
                if (object == null) {
                    driver.setStatus("STATUS_ERROR");
                }
                else {
                    result = (JSONObject) object;
                    String status = result.getString("STATUS");

                    switch (status) {
                        case "FOUND":
                            Log.d(LOG_TAG, "findDriver(): FOUND");
                            driver.Initialize(result.getInt("DRIVER_ID"),
                                    result.getString("DRIVER_NAME"),
                                    new LatLng(Double.parseDouble(result.getString("DRIVER_LAT")),
                                            Double.parseDouble(result.getString("DRIVER_LONG"))), "PAIRED");
                            break;
                        case "NOT_PAIRED":
                            Log.d(LOG_TAG, "findDriver(): NOT_PAIRED");
                            driver.setStatus("NOT_PAIRED");
                            break;
                        case "NOT_FOUND":
                            Log.d(LOG_TAG, "findDriver(): NOT_FOUND");
                            driver.setStatus("NOT_FOUND");
                            break;
                        default:
                            Log.d(LOG_TAG, "findDriver(): STATUS ERROR");
                            driver.setStatus("STATUS_ERROR");
                            break;
                    }
                }
                pairedDriver.postValue(driver);
            }
            catch (JSONException e) {
                e.printStackTrace();
                Log.d(LOG_TAG, "findDriver(): JSONException");
            }

        }, USER_ID, pickupLocation.latitude, pickupLocation.longitude, dropoffLocation.latitude, dropoffLocation.longitude);
    }
}
