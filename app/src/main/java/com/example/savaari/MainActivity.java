package com.example.savaari;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.savaari.auth.login.LoginActivity;
import com.example.savaari.ride.RideActivity;
import com.example.savaari.utility.ThemeVar;
import com.example.savaari.utility.Util;


public class MainActivity extends Util {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ThemeVar.setData(preferences.getInt(getString(R.string.preference_theme_var), ThemeVar.getData()));

        switch (ThemeVar.getData())
        {
            case(0):
                setTheme(R.style.BlackTheme);
                break;
            case(1):
                setTheme(R.style.RedTheme);
                break;
            default:
                setTheme(R.style.BlueTheme);
                break;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Expand logo animation
        ImageView logo = findViewById(R.id.logo);
        Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.zoom);
        logo.startAnimation(animation);

        SharedPreferences sh
                = getSharedPreferences("AuthSharedPref",
                MODE_PRIVATE);

        final int USER_ID = sh.getInt("USER_ID", -1);
        if (USER_ID == -1) {
            launchLoginActivity();
        }
        else {
            ((SavaariApplication) getApplication()).getRepository().persistLogin(object -> {
                if (object == null || !((Boolean) object)) {
                    launchRideActivity(USER_ID, false);
                }

                launchRideActivity(USER_ID, true);
            }, USER_ID);
        }

    }

    public void launchLoginActivity() {
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    public void launchRideActivity(int userID, boolean apiConnection) {
        Intent i = new Intent(MainActivity.this, RideActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("USER_ID", userID);
        i.putExtra("API_CONNECTION", apiConnection);
        startActivity(i);
        finish();
    }
}

//