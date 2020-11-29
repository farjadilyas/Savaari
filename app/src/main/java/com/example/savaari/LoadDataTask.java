package com.example.savaari;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.example.savaari.services.network.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;

public class LoadDataTask extends AsyncTask<String, Void, Object> {

    // String Tag
    private final String LOG_TAG = this.getClass().getSimpleName();
    private final static int SIGN_UP = 0;
    private final static int LOG_IN = 1;
    private final static int LAST_LOCATION = 2;
    private final static int USER_DATA = 3;
    private final static int USER_LOCATIONS = 4;
    private final static int FIND_DRIVER = 5;
    private final static int CHECK_FIND_STATUS = 5;
    private int OPERATION_CODE = -1;

    MutableLiveData<Boolean> stopService = new MutableLiveData<>(false);
    JSONObject findStatus;

    // Declare any reference to UI objects from UI controller
    private final OnAuthenticationListener onAuthenticationListener;
    private final OnDataLoadedListener onDataLoadedListener;

    private Timer timer;

    public LoadDataTask(OnAuthenticationListener mListener, OnDataLoadedListener onDataLoadedListener)
    { // Can pass references to UI objects
        this.onAuthenticationListener = mListener;
        this.onDataLoadedListener = onDataLoadedListener;
    }

    // Stop Ride Status
    public void stopCheckFindStatus()
    {
        timer.cancel();
        timer = null;
    }

    // Main Method that runs in background when this Async task is called

    @Override
    protected Object doInBackground(String... strings)
    {
        // Main URL
        String url = "https://56cc03fd597d.ngrok.io/";
        switch (strings[0])
        {
            case "signup":
                OPERATION_CODE = SIGN_UP;
                if (NetworkUtil.signup(url + "add_rider", strings[1], strings[2], strings[3]))
                {
                    return 1;
                }
                break;

            case "sendLocation":
                OPERATION_CODE = LAST_LOCATION;
                return NetworkUtil.sendLastLocation(url + "saveRiderLocation", Integer.parseInt(strings[1]),
                        Double.parseDouble(strings[2]), Double.parseDouble(strings[3]));

            case "loadData":
                OPERATION_CODE = USER_DATA;
                return NetworkUtil.loadUserData(url + "rider_data", Integer.parseInt(strings[1]));

            case "getUserLocations":
                OPERATION_CODE = USER_LOCATIONS;
                return NetworkUtil.getUserLocations(url + "getRiderLocations");

            case "findDriver":
                OPERATION_CODE = FIND_DRIVER;
                return NetworkUtil.findDriver(url + "findDriver", Integer.parseInt(strings[1]),
                        Double.parseDouble(strings[2]), Double.parseDouble(strings[3]));

            case "checkFindStatus":
                OPERATION_CODE = CHECK_FIND_STATUS;
                /*stopService.observe((LifecycleOwner) this, aBoolean -> {
                    if (aBoolean) {
                        stopCheckFindStatus();
                    }
                });*/

            default:
                OPERATION_CODE = LOG_IN;
                return NetworkUtil.login(url + "login_rider", strings[1], strings[2]);
        }
        // End of Switch
        return -1;
    }// End of Function doInBackground();

    @Override
    protected void onPostExecute(Object object)
    {
        try {
            if ((OPERATION_CODE == USER_DATA || OPERATION_CODE == USER_LOCATIONS
                    || OPERATION_CODE == FIND_DRIVER || OPERATION_CODE == CHECK_FIND_STATUS) && onDataLoadedListener != null) {
                onDataLoadedListener.onDataLoaded(object);
            }
            else if (OPERATION_CODE != LAST_LOCATION && onAuthenticationListener != null) {
                Log.d("IMP RES: ", String.valueOf((int) object));
                onAuthenticationListener.authenticationStatus((int) object);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.d("LoadDataTask: ", "onPostExecute exception");
        }
        super.onPostExecute(object);
    }
}