package com.farjad.savaari;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.farjad.savaari.auth.login.LoginActivity;
import com.farjad.savaari.ride.RideActivity;
import com.farjad.savaari.utility.Util;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // Initialize Util class by passing App Context
        Util.getInstance(getApplicationContext());
        Util.getInstance().setThemeID(preferences.getInt(getString(R.string.preference_theme_var), Util.getInstance().getThemeID()));

        Util.getInstance().themeSelect(this);

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
            Util.getInstance().getRepository().persistLogin(object -> {
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