package com.example.savaari;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class LoadDataTask extends AsyncTask<String, Void, Object> {

    // String Tag
    private final String LOG_TAG = this.getClass().getSimpleName();
    private final static int SIGN_UP = 0;
    private final static int LOG_IN = 1;
    private final static int LAST_LOCATION = 2;
    private final static int USER_DATA = 3;
    private final static int USER_LOCATIONS = 4;
    private int OPERATION_CODE = -1;

    // Declare any reference to UI objects from UI controller
    private OnAuthenticationListener onAuthenticationListener;
    private OnDataLoadedListener onDataLoadedListener;

    public LoadDataTask(OnAuthenticationListener mListener, OnDataLoadedListener onDataLoadedListener)
    { // Can pass references to UI objects
        this.onAuthenticationListener = mListener;
        this.onDataLoadedListener = onDataLoadedListener;
    }

    // Main Method that runs in background when this Async task is called
    @Override
    protected Object doInBackground(String... strings)
    {
        try
        {
            // Main URL
            String url = "https://b9b0b4d43db2.ngrok.io/";
            switch (strings[0])
            {
                case "signup":
                    OPERATION_CODE = SIGN_UP;
                    if (NetworkUtil.signup(url + "add_user", strings[1], strings[2], strings[3]))
                    {
                        return 1;
                    }
                    break;

                case "sendLocation":
                    OPERATION_CODE = LAST_LOCATION;
                    return NetworkUtil.sendLastLocation(url + "saveUserLocation", Integer.parseInt(strings[1]),
                            Double.parseDouble(strings[2]), Double.parseDouble(strings[3]));

                case "loadData":
                    OPERATION_CODE = USER_DATA;
                    return NetworkUtil.loadUserData(url + "user_data", Integer.parseInt(strings[1]));

                case "getUserLocations":
                    OPERATION_CODE = USER_LOCATIONS;
                    return NetworkUtil.getUserLocations(url + "getUserLocations");

                default:
                    OPERATION_CODE = LOG_IN;
                    return NetworkUtil.login(url + "login", strings[1], strings[2]);
            }
            // End of Switch
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return -1;
    }// End of Function doInBackground();

    @Override
    protected void onPostExecute(Object object)
    {
        if ((OPERATION_CODE == USER_DATA || OPERATION_CODE == USER_LOCATIONS) && onDataLoadedListener != null)
        {
            onDataLoadedListener.onDataLoaded(object);
        }
        else if (OPERATION_CODE != LAST_LOCATION && onAuthenticationListener != null)
        {
            Log.d("IMP RES: ", String.valueOf((int) object));
            onAuthenticationListener.authenticationStatus((int) object);
        }
        super.onPostExecute(object);
    }
}