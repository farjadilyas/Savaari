package com.example.savaari;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class LoadDataTask extends AsyncTask<String, Void, Boolean> {

    // Declare any reference to UI objects from UI controller
    private MyInterface mListener;

    public LoadDataTask(MyInterface mListener) { // Can pass references to UI objects
        this.mListener = mListener;
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

    boolean login(String urlAddress, String username, String password) {

        Scanner scanner = null;

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
            //JSONObject json = java.text.MessageFormat.parseJson(response);

            Log.d("HEREEE: ", response);
            scanner.close();
            conn.disconnect();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        if (strings[0].equals("signup")) {
            try {
                if (signup("http://186d53a20a06.ngrok.io/add_user", strings[1], strings[2], strings[3])) {
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
         else {
            return login("http://186d53a20a06.ngrok.io/login", strings[1], strings[2]);
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        Log.d("IMP RES: ", String.valueOf(aBoolean));
        mListener.myMethod(aBoolean);
        super.onPostExecute(aBoolean);
    }
}