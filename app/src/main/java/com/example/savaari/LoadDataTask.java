package com.example.savaari;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class LoadDataTask extends AsyncTask<String, Void, Integer> {

    // String Tag
    private final String LOG_TAG = this.getClass().getSimpleName();

    // Declare any reference to UI objects from UI controller
    private OnAuthenticationListener onAuthenticationListener;

    public LoadDataTask(OnAuthenticationListener mListener) { // Can pass references to UI objects
        this.onAuthenticationListener = mListener;
    }

    boolean signup(String urlAddress, String username, String emailAddress, String password) throws JSONException {

        JSONObject jsonParam = new JSONObject();
        jsonParam.put("username", username);
        jsonParam.put("email_address", emailAddress);
        jsonParam.put("password", password);

        return sendPost(urlAddress, jsonParam);
    }

    boolean sendPost(String urlAddress, JSONObject jsonParam) {
        try {
            URL url = new URL(urlAddress);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            Log.i("JSON", jsonParam.toString());
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonParam.toString());

            os.flush();
            os.close();

            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG" , conn.getResponseMessage());

            conn.disconnect();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    Integer login(String urlAddress, String username, String password) throws JSONException {

        Scanner scanner = null;

        JSONObject results;

        try {
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("username", username);
            jsonParam.put("password", password);

            URL url = new URL(urlAddress);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            Log.i("JSON", jsonParam.toString());

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonParam.toString());

            os.flush();
            os.close();

            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG" , conn.getResponseMessage());

            try {
                scanner = new Scanner(conn.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String response = scanner.useDelimiter("\\Z").next();

            results = new JSONObject(response);

            Log.d("HEREEE: ", response);
            scanner.close();
            conn.disconnect();
        }
        catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        return (int) results.get("USER_ID");
    }

    // Function for Posting Current Location Data
    int sendLastLocation(String urladdress, int currentUserID, double latitude, double longitude)
    {
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
            Log.d(LOG_TAG, "sendLastLocation: User_ID: " + currentUserID);
            Log.d(LOG_TAG, "sendLastLocation: Latitude: " + latitude);
            Log.d(LOG_TAG, "sendLastLocation: Longitude: " + longitude);
            Log.d(LOG_TAG, "sendLastLocation: TimeStamp: " + currentTimeStamp);

            // Sending JSON
            return sendPost(urladdress, jsonParam) ? 1 : 0;

        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    protected Integer doInBackground(String... strings)
    {
        try
        {
            String url = "https://50ed16d8cec7.ngrok.io/";
            if (strings[0].equals("signup"))
            {
                if (signup(url + "add_user", strings[1], strings[2], strings[3]))
                {
                    return 1;
                }
            }
            else if (strings[0].equals("sendLocation"))
            {
                return sendLastLocation(url + "saveUserLocation", Integer.parseInt(strings[1]),
                        Double.parseDouble(strings[2]), Double.parseDouble(strings[3]));
            }
            else
            {
                return login(url + "login", strings[1], strings[2]);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    protected void onPostExecute(Integer integer)
    {
        Log.d("IMP RES: ", String.valueOf(integer));
        onAuthenticationListener.authenticationStatus(integer);
        super.onPostExecute(integer);
    }
}