package com.example.savaari.services.network;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

// This class holds static functions for interacting with the API Layer
public class NetworkUtil
{
    // Main Attributes
    private static final NetworkUtil networkUtil = new NetworkUtil();
    private static final String TAG = NetworkUtil.class.getSimpleName();

    // Private Constructor
    private NetworkUtil()
    {
        // Empty
    }

    // -------------------------------------------------------------------------------
    //                                 Main Methods
    // -------------------------------------------------------------------------------

    public static JSONArray sendPostArray(String urlAddress, JSONObject jsonParam, boolean needResponse) throws JSONException {

        JSONArray result = new JSONArray();
        try
        {
            // Creating the HTTP Connection
            URL url = new URL(urlAddress);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // Sending the Data and Receiving Output
            Log.i(TAG, "sendPostArray: " + jsonParam.toString());
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonParam.toString());

            // Flushing output streams
            os.flush();
            os.close();

            Log.i(TAG, "sendPostArray: Status: " + conn.getResponseCode());
            Log.i(TAG, "sendPostArray: Response Message: " + conn.getResponseMessage());

            // Sending the Response Back to the User in JSON
            if (needResponse)
            {
                Scanner scanner;
                try
                {
                    scanner = new Scanner(conn.getInputStream());
                    String response = scanner.useDelimiter("\\Z").next();
                    JSONArray results = new JSONArray(response);
                    Log.d(TAG, "sendPostArray: " + response);
                    scanner.close();
                    conn.disconnect();
                    return results;
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            result.put(0, true);
            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result.put(0, false);
            return result;
        }
    }
    // Sending POST Requests
    public static JSONObject sendPost(String urlAddress, JSONObject jsonParam, boolean needResponse) throws JSONException {

        JSONObject result = new JSONObject();
        try
        {
            // Creating the HTTP Connection
            URL url = new URL(urlAddress);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // Sending the Data and Receiving Output
            Log.i(TAG, "sendPost: " + jsonParam.toString());
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonParam.toString());

            // Flushing output streams
            os.flush();
            os.close();

            Log.i(TAG, "sendPost: Status: " + conn.getResponseCode());
            Log.i(TAG, "sendPost: Response Message: " + conn.getResponseMessage());

            // Sending the Response Back to the User in JSON
            if (needResponse)
            {
                Scanner scanner;
                try
                {
                    scanner = new Scanner(conn.getInputStream());
                    String response = scanner.useDelimiter("\\Z").next();
                    JSONObject results = new JSONObject(response);
                    Log.d(TAG, "sendPost: " + response);
                    scanner.close();
                    conn.disconnect();
                    return results;
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            result.put("result", true);
            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result.put("result", false);
            return result;
        }
    }

    // Send Last Location
    public static int sendLastLocation(String urlAddress, int currentUserID, double latitude, double longitude)
    {
        String url = urlAddress + "saveRiderLocation";
        try
        {
            // TimeStamp
            long tsLong = System.currentTimeMillis() / 1000;
            String currentTimeStamp = Long.toString(tsLong);

            // JSON
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("USER_ID", currentUserID);
            jsonParam.put("LATITUDE", latitude);
            jsonParam.put("LONGITUDE", longitude);
            jsonParam.put("TIMESTAMP", currentTimeStamp);

            // Logging
            Log.d(TAG, "sendLastLocation: User_ID: " + currentUserID);
            Log.d(TAG, "sendLastLocation: Latitude: " + latitude);
            Log.d(TAG, "sendLastLocation: Longitude: " + longitude);
            Log.d(TAG, "sendLastLocation: TimeStamp: " + currentTimeStamp);

            // Sending JSON
            return NetworkUtil.sendPost(url, jsonParam, false).getBoolean("result") ? 1 : 0;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    /*
    *   SET OF RIDER-SIDE MATCHMAKING FUNCTIONS ----------------------------------------------------
    */
    public static JSONObject findDriver(String urlAddress, int currentUserID, double srcLatitude,
                                        double srcLongitude, double destLatitude, double destLongitude) {
        String url = urlAddress + "findDriver";
        try {
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("USER_ID", currentUserID);
            jsonParam.put("SOURCE_LAT", srcLatitude);
            jsonParam.put("SOURCE_LONG", srcLongitude);
            jsonParam.put("DEST_LAT", destLatitude);
            jsonParam.put("DEST_LONG", destLongitude);

            return NetworkUtil.sendPost(url, jsonParam, true);
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.d("NetworkUtil: ", "findDriver() Exception");
            return null;
        }
    }

    /*
    *  END OF RIDER-SIDE MATCHMAKING FUNCTIONS -----------------------------------------------------
    */

    // Sign-Up
    public static boolean signup(String urlAddress, String username, String emailAddress, String password)
    {
        try {
            String url = urlAddress + "add_rider";
            Log.d("NetworkUtil: ", "signup() called");
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("username", username);
            jsonParam.put("email_address", emailAddress);
            jsonParam.put("password", password);

            return (sendPost(url, jsonParam, true).getInt("STATUS_CODE") == 200);
        }
        catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Login
    public static int login(String urlAddress, String username, String password)
    {
        String url = urlAddress + "login_rider";
        try
        {
            // Creating the JSON Object
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("username", username);
            jsonParam.put("password", password);

            // Sending Request
            JSONObject results = sendPost(url, jsonParam, true);

            return results.getInt("USER_ID");
        } catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }
    // Loading User Data
    public static JSONObject loadUserData(String urlAddress, int currentUserID)
    {
        String url = urlAddress + "rider_data";
        JSONObject jsonParam = new JSONObject();
        try
        {
            jsonParam.put("USER_ID", currentUserID);
            return sendPost(url, jsonParam, true);
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    // Get User Locations
    public static JSONArray getUserLocations(String urlAddress)
    {
        String url = urlAddress + "getRiderLocations";
        JSONObject jsonParam = new JSONObject();
        try
        {
            jsonParam.put("Dummy", 0);
            return sendPostArray(url, jsonParam, true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    // Get paired driver Location
    public static JSONObject getDriverLocation(String urlAddress, int driverID) {
        Log.d(TAG, " :getDriverLocation called!");
        String url = urlAddress + "getDriverLocation";
        JSONObject jsonParam = new JSONObject();

        try {
            jsonParam.put("USER_ID", driverID);
            return sendPost(url, jsonParam, true);
        }
        catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, " :getDriverLocation() - JSONException");
            return null;
        }
    }
}
