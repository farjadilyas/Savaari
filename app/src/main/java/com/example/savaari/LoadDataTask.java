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
    private int OPERATION_CODE = -1;

    // Declare any reference to UI objects from UI controller
    private OnAuthenticationListener onAuthenticationListener;
    private OnDataLoadedListener onDataLoadedListener;

    public LoadDataTask(OnAuthenticationListener mListener, OnDataLoadedListener onDataLoadedListener) { // Can pass references to UI objects
        this.onAuthenticationListener = mListener;
        this.onDataLoadedListener = onDataLoadedListener;
    }

    boolean signup(String urlAddress, String username, String emailAddress, String password) throws JSONException {

        OPERATION_CODE = SIGN_UP;

        JSONObject jsonParam = new JSONObject();
        jsonParam.put("username", username);
        jsonParam.put("email_address", emailAddress);
        jsonParam.put("password", password);

        return NetworkUtil.sendPost(urlAddress, jsonParam, false).getBoolean("result");
    }

    Integer login(String urlAddress, String username, String password) throws JSONException {

        OPERATION_CODE = LOG_IN;
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

    JSONObject loadUserData(String urlAddress, int currentUserID) {
        OPERATION_CODE = USER_DATA;

        Scanner scanner = null;
        JSONObject results;

        try {
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("USER_ID", currentUserID);

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

            return results;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Main Method that runs in background when this Async task is called
    @Override
    protected Object doInBackground(String... strings)
    {
        try
        {
            // Main URL
            String url = "https://d440b35ba592.ngrok.io/";

            switch (strings[0]) {
                case "signup":
                    if (signup(url + "add_user", strings[1], strings[2], strings[3])) {
                        return 1;
                    }
                    break;

                case "sendLocation":
                    OPERATION_CODE = LAST_LOCATION;
                    return NetworkUtil.sendLastLocation(url + "saveUserLocation", Integer.parseInt(strings[1]),
                            Double.parseDouble(strings[2]), Double.parseDouble(strings[3]));

                case "loadData":
                    return loadUserData(url + "user_data", Integer.parseInt(strings[1]));

                default:
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
    protected void onPostExecute(Object object)
    {
        if (OPERATION_CODE == USER_DATA && onDataLoadedListener != null) {
            onDataLoadedListener.onDataLoaded(object);
        }
        else if (OPERATION_CODE != LAST_LOCATION && onAuthenticationListener != null){
            Log.d("IMP RES: ", String.valueOf((int) object));
            onAuthenticationListener.authenticationStatus((int) object);
        }
        super.onPostExecute(object);
    }
}