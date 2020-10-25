package com.example.savaari.services.network;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NetworkServiceUtil {

    private static final NetworkServiceUtil instance = new NetworkServiceUtil();
    private static final String LOG_TAG = "NetworkServiceUtil";

    private NetworkServiceUtil() {}

    public static void signup(Context context, String username, String emailAddress, String password) {
        Log.d(LOG_TAG, "signup() : called" + username + ", " + emailAddress + ", " + password);
        Intent intent = new Intent();
        intent.setClass(context, NetworkService.class);
        intent.putExtra("TASK", "signup");
        intent.putExtra("USER_NAME", username);
        intent.putExtra("EMAIL_ADDRESS", emailAddress);
        intent.putExtra("PASSWORD", password);
        context.startService(intent);
    }

    public static void login(Context context, String emailAddress, String password) {
        Log.d(LOG_TAG, "login() : called" + emailAddress + ", " + password);

        Intent intent = new Intent();
        intent.setClass(context, NetworkService.class);
        intent.putExtra("TASK", "login");
        intent.putExtra("EMAIL_ADDRESS", emailAddress);
        intent.putExtra("PASSWORD", password);
        context.startService(intent);
    }

    public static void loadUserData(Context context, int USER_ID) {
        Log.d(LOG_TAG, "loadUserData() : called, USER_ID: " + USER_ID);

        Intent intent = new Intent();
        intent.setClass(context, NetworkService.class);
        intent.putExtra("TASK", "loadData");
        intent.putExtra("USER_ID", USER_ID);
        context.startService(intent);
    }
}
