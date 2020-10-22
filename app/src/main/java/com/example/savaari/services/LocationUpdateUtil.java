package com.example.savaari.services;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class LocationUpdateUtil
{
    // Main Attributes
    private static final LocationUpdateUtil locationUpdateUtil = new LocationUpdateUtil();
    private static final String TAG = "LocationUpdateUtil";

    // Main Constructor
    private LocationUpdateUtil()
    {
        // Empty
    }

    // Methods
    // Check if the background location service is running
    public static boolean isLocationServiceRunning(ActivityManager manager)
    {
        // Iterating over all services to check if the service is running
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if ("com.example.savaari.services.LocationUpdateService".equals(service.service.getClassName()))
            {
                Log.d(TAG, "isLocationServiceRunning: location service is running");
                return true;
            }
        }
        Log.d(TAG, "isLocationServiceRunning: location service is not running.");
        return false;
    }

    // Method for Starting the Location Service
    public static void startLocationService(ActivityManager manager, Context context)
    {
        if (!LocationUpdateUtil.isLocationServiceRunning(manager))
        {
            Intent serviceIntent = new Intent(context, LocationUpdateService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                context.startForegroundService(serviceIntent);
            }
            else
            {
                context.startService(serviceIntent);
            }
        }
    }
    // Method for stopping Location Service
    public static void stopLocationService(Context context)
    {
        context.stopService(new Intent(context, LocationUpdateService.class));
        Log.d(TAG, "stopLocationService: service stopped");
    }
}
