package com.example.savaari.services.network;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NetworkService extends IntentService
{
    // Main Attributes
    private final String url = "https://56cc03fd597d.ngrok.io/";
    private static final String TAG = "NetworkService";

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
                    Log.d("NetworkService: ", "signup called");
                    result = NetworkUtil.signup(url + "add_rider", intent.getExtras().getString("USER_NAME"),
                            intent.getExtras().getString("EMAIL_ADDRESS"), intent.getExtras().getString("PASSWORD"));
                    responseIntent.putExtra("RESULT", (boolean) result);
                    break;

                case "sendLocation":
                    result = NetworkUtil.sendLastLocation(url + "saveRiderLocation", intent.getExtras().getInt("USER_ID"),
                            intent.getExtras().getDouble("LATITUDE"), intent.getExtras().getDouble("LONGITUDE"));
                    responseIntent.putExtra("RESULT", (int) result);
                    break;

                case "loadData":
                    result =  NetworkUtil.loadUserData(url + "rider_data", intent.getExtras().getInt("USER_ID"));
                    assert result != null;
                    responseIntent.putExtra("RESULT", ((JSONObject) result).toString());
                    break;

                case "getUserLocations":
                    result = NetworkUtil.getUserLocations(url + "getRiderLocations");
                    assert result != null;
                    responseIntent.putExtra("RESULT", ((JSONArray) result).toString());
                    break;

                case "findDriver":
                    result = NetworkUtil.findDriver(url + "findDriver", intent.getExtras().getInt("USER_ID"),
                            intent.getExtras().getDouble("LATITUDE"), intent.getExtras().getDouble("LONGITUDE"));
                    responseIntent.putExtra("RESULT", (boolean) result);
                    break;

                case "checkFindStatus":
                    Log.d(TAG, "checkFindStatus, user_id: " + intent.getExtras().getInt("USER_ID"));
                    result = NetworkUtil.checkFindStatus(url + "checkFindStatus",
                            intent.getExtras().getInt("USER_ID"));

                    assert result != null;
                    Log.d(TAG, "checkFindService(): " + result.toString());
                    responseIntent.putExtra("RESULT", result.toString());
                    break;

                default:
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