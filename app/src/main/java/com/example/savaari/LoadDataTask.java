package com.example.savaari;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
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
            //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
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

    boolean signin(String urlAddress, String username, String password) {
        String requestURL = urlAddress;
        URL wikiRequest = null;
        try {
            wikiRequest = new URL(requestURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection = null;

        try {
            assert wikiRequest != null;
            connection = (HttpURLConnection) wikiRequest.openConnection();
        } catch (IOException e) {
            System.out.println("Connection failed\n");
        }

        assert connection != null;
        connection.setDoOutput(true);


        Scanner scanner = null;
        try {
            scanner = new Scanner(wikiRequest.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert scanner != null;
        String response = scanner.useDelimiter("\\Z").next();
        //JSONObject json = java.text.MessageFormat.parseJson(response);

        Log.d("IMP MSG: ", response);
        scanner.close();

        return true;
    }

    boolean login(String urlAddress, String username, String password) {
        return true;
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        switch (strings[0]) {
            case "signup":
                try {
                    if (signup("http://86e3f26e888a.ngrok.io/add_user", strings[1], strings[2], strings[3])) {
                        return true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            case "signin":
                if (signin("http://86e3f26e888a.ngrok.io/add_user", strings[1], strings[2])) {
                    return true;
                }
            case "login":
                if (login("http://86e3f26e888a.ngrok.io/add_user", strings[1], strings[2])) {
                    return true;
                }
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