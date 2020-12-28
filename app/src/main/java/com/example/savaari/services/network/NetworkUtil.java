package com.example.savaari.services.network;

import android.text.TextUtils;
import android.util.Log;

import com.example.savaari.ride.entity.Location;
import com.example.savaari.ride.entity.Ride;
import com.example.savaari.ride.entity.Rider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// This class holds static functions for interacting with the API Layer
public class NetworkUtil
{
    // Main Attributes
    private static NetworkUtil instance = null;
    private static final String TAG = NetworkUtil.class.getSimpleName();

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Cookie Management Variables
    private  final String COOKIES_HEADER = "Set-Cookie";
    private  final java.net.CookieManager msCookieManager = new java.net.CookieManager();
    private  Map<String, List<String>> headerFields;
    private List<String> cookiesHeader;

    // Private Constructor
    private NetworkUtil()
    {
        // Empty
    }

    public synchronized static NetworkUtil getInstance() {
        if (instance == null) {
            instance = new NetworkUtil();
        }
        return instance;
    }

    // -------------------------------------------------------------------------------
    //                                 Main Methods
    // -------------------------------------------------------------------------------

    // Sending POST Requests
    private String sendPost(String urlAddress, JSONObject jsonParam, boolean needResponse) {
        try
        {
            // Creating the HTTP Connection
            URL url = new URL(urlAddress);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");

            /*
            * Add cookies to request header
            * While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'
            * */
            if (msCookieManager.getCookieStore().getCookies().size() > 0) {
                conn.setRequestProperty("Cookie",
                        TextUtils.join(";",  msCookieManager.getCookieStore().getCookies()));
            }
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

            // Save cookie
            headerFields = conn.getHeaderFields();
            cookiesHeader = headerFields.get(COOKIES_HEADER);

            if (cookiesHeader != null) {
                for (String cookie : cookiesHeader) {
                    msCookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                }
            }

            // Sending the Response Back to the User in JSON
            if (needResponse)
            {
                Scanner scanner;
                try
                {
                    scanner = new Scanner(conn.getInputStream());
                    String response = null;

                    if (scanner.hasNext()) {
                        response = scanner.useDelimiter("\\Z").next();
                        Log.d(TAG, "sendPost: " + response);
                    }
                    else {
                        Log.d(TAG, "sendPost: received null Input Stream");
                    }
                    scanner.close();
                    conn.disconnect();
                    return response;
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            return null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /*
    *   SET OF RIDER-SIDE MATCHMAKING FUNCTIONS ----------------------------------------------------
    */
    public Ride findDriver(String urlAddress, int currentUserID, double srcLatitude,
                                        double srcLongitude, double destLatitude, double destLongitude, int paymentMode, int rideType) {
        String url = urlAddress + "findDriver";
        try {
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("USER_ID", currentUserID);
            jsonParam.put("SOURCE_LAT", srcLatitude);
            jsonParam.put("SOURCE_LONG", srcLongitude);
            jsonParam.put("DEST_LAT", destLatitude);
            jsonParam.put("DEST_LONG", destLongitude);
            jsonParam.put("PAYMENT_MODE", paymentMode);
            jsonParam.put("RIDE_TYPE_ID", rideType);

            String resultString = sendPost(url, jsonParam, true);

            return ((resultString == null)? null : objectMapper.readValue(resultString, Ride.class));
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
    public boolean signup(String urlAddress, String username, String emailAddress, String password)
    {
        try {
            String url = urlAddress + "add_rider";
            Log.d("NetworkUtil: ", "signup() called");
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("username", username);
            jsonParam.put("email_address", emailAddress);
            jsonParam.put("password", password);

            String resultString = sendPost(url, jsonParam, true);
            return ((resultString != null) && (new JSONObject(resultString).getInt("STATUS_CODE") == 200));
        }
        catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Login
    public int login(String urlAddress, String username, String password)
    {
        String url = urlAddress + "login_rider";
        try
        {
            // Creating the JSON Object
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("username", username);
            jsonParam.put("password", password);

            // Sending Request
            String resultString = sendPost(url, jsonParam, true);
            return ((resultString == null)? -1 : new JSONObject(resultString).getInt("USER_ID"));

        } catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    // Login with credentials
    public boolean persistLogin(String urlAddress, Integer userID) {

        try {
            String url = urlAddress + "persistRiderLogin";
            String resultString = sendPost(url, new JSONObject(), true);
            return ((resultString != null) && new JSONObject(resultString).getInt("STATUS_CODE") == 200);
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, " :persistLogin - Exception");
            return false;
        }
    }

    public boolean logout(String urlAddress, Integer userID) {
        try{
            String url = urlAddress + "logoutRider";
            String resultString = sendPost(url, new JSONObject(), true);
            return ((resultString != null) && new JSONObject(resultString).getInt("STATUS_CODE") == 200);
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, " :logout - Exception");
            return false;
        }
    }
    // Loading User Data
    public Rider loadUserData(String urlAddress, int currentUserID)
    {
        String url = urlAddress + "rider_data";
        JSONObject jsonParam = new JSONObject();
        try
        {
            jsonParam.put("USER_ID", currentUserID);
            String resultString = sendPost(url, jsonParam, true);
            return ((resultString == null)? null : objectMapper.readValue(resultString, Rider.class));
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    // Send Last Location
    public int sendLastLocation(String urlAddress, int currentUserID, double latitude, double longitude)
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
            String resultString = sendPost(url, jsonParam, true);

            if (resultString == null) {
                return -1;
            }
            else {
                return ((new JSONObject(resultString).getInt("STATUS") == 200)? 1 : 0);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    // Get User Locations
    public ArrayList<Location> getUserLocations(String urlAddress)
    {
        String url = urlAddress + "getRiderLocations";
        JSONObject jsonParam = new JSONObject();
        try
        {
            jsonParam.put("Dummy", 0);

            String resultString = sendPost(url, jsonParam, true);

            if (resultString == null) {
                return null;
            }
            else {
                return objectMapper.readValue(resultString,
                        objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Location.class));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }


    /* In-ride methods */

    public Ride getRide(String urlAddress, int riderID) {
        Log.d(TAG, ":getRide() called!");
        String url = urlAddress + "getRideForRider";

        JSONObject jsonParam = new JSONObject();

        try {
            jsonParam.put("USER_ID", riderID);

            String resultString = sendPost(url, jsonParam, true);
            return ((resultString == null)? null : objectMapper.readValue(resultString, Ride.class));
        }
        catch (JSONException | JsonProcessingException e) {
            e.printStackTrace();
            Log.d(TAG, " :getRide() - JSONException");
            return null;
        }
    }

    public JSONObject getRideStatus(String urlAddress, int rideID) {
        Log.d(TAG, " :getRideStatus called!");
        String url = urlAddress + "getRideStatus";

        JSONObject jsonParam = new JSONObject();

        try {
            jsonParam.put("RIDE_ID", rideID);

            String resultString = sendPost(url, jsonParam, true);
            return ((resultString == null)? null : new JSONObject(resultString));
        }
        catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, " :getRideStatus() - JSONException");
            return null;
        }
    }

    public boolean acknowledgeEndOfRide(String urlAddress, int rideID, int riderID) {
        Log.d(TAG, " :acknowledgeEndOfRide called!");
        String url = urlAddress + "acknowledgeEndOfRide";

        JSONObject jsonParam = new JSONObject();

        try {
            jsonParam.put("RIDE_ID", rideID);
            jsonParam.put("RIDER_ID", riderID);

            String resultString = sendPost(url, jsonParam, true);
            return ((resultString != null) && new JSONObject(resultString).getInt("STATUS_CODE") == 200);
        }
        catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, " :acknowledgeEndOfRide() - JSONException");
            return false;
        }
    }

    public boolean giveFeedbackForDriver(String urlAddress, int rideID, int driverID, float rating) {
        Log.d(TAG, " :giveFeedbackForDriver called!");
        String url = urlAddress + "giveFeedbackForDriver";

        JSONObject jsonParam = new JSONObject();

        try {
            jsonParam.put("RIDE_ID", rideID);
            jsonParam.put("DRIVER_ID", driverID);
            jsonParam.put("RATING", rating);

            String resultString = sendPost(url, jsonParam, true);
            return ((resultString != null) && new JSONObject(resultString).getInt("STATUS") == 200);
        }
        catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, " :giveFeedbackForDriver() - JSONException");
            return false;
        }
    }

    // Get paired driver Location
    public JSONObject getDriverLocation(String urlAddress, int driverID) {
        Log.d(TAG, " :getDriverLocation called!");
        String url = urlAddress + "getDriverLocation";
        JSONObject jsonParam = new JSONObject();

        try {
            jsonParam.put("USER_ID", driverID);

            String resultString = sendPost(url, jsonParam, true);
            return ((resultString == null)? null : new JSONObject(resultString));
        }
        catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, " :getDriverLocation() - JSONException");
            return null;
        }
    }

    /* End of section */
}
