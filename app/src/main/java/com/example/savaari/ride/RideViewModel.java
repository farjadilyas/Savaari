package com.example.savaari.ride;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.savaari.LoadDataTask;
import com.example.savaari.OnDataLoadedListener;
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

    /* loads array of UserLocation*/
    public void loadUserLocations() {
        if (!userLocationsLoaded.getValue()) {
            new LoadDataTask(null, object -> {
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
                            userLocation.setUserID(obj.getInt("USER_ID"));
                            userLocation.setLatitude(obj.getDouble("LATITUDE"));
                            userLocation.setLongitude(obj.getDouble("LONGITUDE"));
                            userLocation.setTimestamp(obj.getString("TIMESTAMP"));

                            // Adding Final Object
                            mUserLocations.add(userLocation);
                            Log.d(TAG, "loadUserLocations: userID: " + userLocation.getUserID());
                            Log.d(TAG, "loadUserLocations: latitude: " + userLocation.getLatitude());
                            Log.d(TAG, "loadUserLocations: longitude: " + userLocation.getLongitude());
                            Log.d(TAG, "loadUserLocations: timestamp: " + userLocation.getTimestamp());
                        }
                    }
                    userLocationsLoaded.setValue(true);
                }
                catch (Exception e)
                {
                    Log.d(TAG, "Exception in loadUserLocations");
                    e.printStackTrace();
                    userLocationsLoaded.setValue(false);
                }
            }).execute("getUserLocations");
        }
        else {
            userLocationsLoaded.setValue(true);
        }
    }
}
