package com.example.savaari.ride;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.savaari.UserLocation;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class RideViewModel extends ViewModel {

    private static String LOG_TAG = RideViewModel.class.getSimpleName();

    /* CCredentials for netowrk operations */
    private int USER_ID = -1;

    /* User account data*/
    private String username;
    private String emailAddress;
    private LatLng userCoordinates = new LatLng(0,0);

    /* User locations data for pinging */
    private ArrayList<UserLocation> mUserLocations = new ArrayList<>();

    /* Data Loaded status flags */
    MutableLiveData<Boolean> userDataLoaded = new MutableLiveData<>(false);
    MutableLiveData<Boolean> userLocationsLoaded = new MutableLiveData<>(false);

    public RideViewModel(int USER_ID) {
        this.USER_ID = USER_ID;
    }

    /* Get user data */
    public String getUsername() {
        return username;
    }
    public String getEmailAddress() {
        return emailAddress;
    }
    public LatLng getUserCoordinates() {
        return userCoordinates;
    }
    public ArrayList<UserLocation> getUserLocations() {
        return mUserLocations;
    }

    /* Set USER_ID */
    //public void setUserID(int USER_ID) { this.USER_ID = USER_ID; }

    /* Return LiveData to observe Data Loaded Flags */
    public LiveData<Boolean> isLiveUserDataLoaded() {
        return userDataLoaded;
    }
    public LiveData<Boolean> isLiveUserLocationsLoaded() { return userLocationsLoaded; }


    /* Need a setter since coordinates are received from activity */
    public void setUserCoordinates(double latitude, double longitude) {
        userCoordinates = new LatLng(latitude, longitude);
    }


    // Function on User Data Loaded
    public void onUserDataLoaded(String resultString) {
        try {
            if (resultString == null) {
                Log.d(LOG_TAG, "onDataLoaded(): resultString is null");
                userDataLoaded.setValue(false);
            }
            else {
                JSONObject result = new JSONObject(resultString);

                username = result.getString("USER_NAME");
                emailAddress = result.getString("EMAIL_ADDRESS");
                Log.d("loadUserData(): ", username + ", " + emailAddress);
                userDataLoaded.setValue(true);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            userDataLoaded.setValue(false);
            Log.d(LOG_TAG, "onDataLoaded(): exception thrown" + resultString);
        }
    }

    // Function on User Locations Loaded
    public void onUserLocationsLoaded(String resultString)
    {
        try {
            if (resultString == null)
            {
                Log.d(TAG, "onUserLocationsLoaded(): resultString is null");
                userLocationsLoaded.setValue(false);
            }
            else {
                JSONArray resultArray = new JSONArray(resultString);
                Log.d(TAG, "loadUserLocations: " + resultArray.toString());
                Log.d(TAG, "loadUserLocations: found JSON Array");

                // Appending the User Locations in Array
                for (int i = 0; i < resultArray.length(); i++) {
                    JSONObject obj = resultArray.getJSONObject(i);
                    UserLocation userLocation = new UserLocation();

                    // Assigning User Objects
                    userLocation.setUserID(obj.getInt("USER_ID"));
                    userLocation.setLatitude(obj.getDouble("LATITUDE"));
                    userLocation.setLongitude(obj.getDouble("LONGITUDE"));
                    userLocation.setTimestamp(obj.getString("TIMESTAMP"));

                    // Adding Final Object
                    mUserLocations.add(userLocation);
                }
                userLocationsLoaded.setValue(true);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d(TAG, "onUserLocationsLoaded(): Exception thrown!");
            userLocationsLoaded.setValue(false);
        }
    }
    // End of Function: onUserLocationsLoaded();
}
