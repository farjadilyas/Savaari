package com.example.savaari.services.network;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.savaari.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NetworkService extends IntentService
{
    // Main Attributes
    private final String url = "https://56cc03fd597d.ngrok.io/";
    private static final String TAG = "NetworkService";

    /* Network task operation codes */
    private final static int SIGN_UP = 0;
    private final static int LOG_IN = 1;
    private final static int LAST_LOCATION = 2;
    private final static int USER_DATA = 3;
    private final static int USER_LOCATIONS = 4;
    private final static int MARK_ACTIVE = 5;
    private final static int CHECK_RIDE_STATUS = 6;
    private int OPERATION_CODE = -1;

    /* Intent for sending result via broadcast receiver */
    Intent responseIntent;

    // Main Constructor
    public NetworkService()
    {
        super("NetworkService");
        Log.d(TAG, "NetworkService() Constructor");
    }

    public NetworkService(String name)
    {
        super(name);
    }

    // Main onHandleIntent
    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        // Getting Variables from Intent
        String task = intent.getExtras().getString("TASK");

        Log.d(TAG, "onHandleIntent: TASK = " + task);

        responseIntent = new Intent();
        responseIntent.setAction("RESULT");

        Object result;

        /* Pick network task to execute */
        try
        {
            // Main URL;
            switch (task)
            {
                case "signup":
                    OPERATION_CODE = SIGN_UP;
                    Log.d("NetworkService: ", "signup called");
                    result = NetworkUtil.signup(url + "add_rider", intent.getExtras().getString("USER_NAME"),
                            intent.getExtras().getString("EMAIL_ADDRESS"), intent.getExtras().getString("PASSWORD"));
                    responseIntent.putExtra("RESULT", (boolean) result);
                    break;

                case "sendLocation":
                    OPERATION_CODE = LAST_LOCATION;
                    result = NetworkUtil.sendLastLocation(url + "saveRiderLocation", intent.getExtras().getInt("USER_ID"),
                            intent.getExtras().getDouble("LATITUDE"), intent.getExtras().getDouble("LONGITUDE"));
                    responseIntent.putExtra("RESULT", (int) result);
                    break;

                case "loadData":
                    OPERATION_CODE = USER_DATA;
                    result =  NetworkUtil.loadUserData(url + "rider_data", intent.getExtras().getInt("USER_ID"));
                    assert result != null;
                    responseIntent.putExtra("RESULT", ((JSONObject) result).toString());
                    break;

                case "getUserLocations":
                    OPERATION_CODE = USER_LOCATIONS;
                    result = NetworkUtil.getUserLocations(url + "getRiderLocations");
                    assert result != null;
                    responseIntent.putExtra("RESULT", ((JSONArray) result).toString());
                    break;

                default:
                    OPERATION_CODE = LOG_IN;
                    result = NetworkUtil.login(url + "login_rider", intent.getExtras().getString("EMAIL_ADDRESS"),
                            intent.getExtras().getString("PASSWORD"));
                    Log.d(TAG, "login: result: " + (int) result);
                    responseIntent.putExtra("RESULT", (int) result);
                    break;
            }
            // End of Switch
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        /* Send intent to broadcast receiver */
        responseIntent.putExtra("TASK", task);
        sendBroadcast(responseIntent);
    }
}