package com.example.savaari;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.savaari.auth.login.LoginActivity;
import com.example.savaari.ride.RideActivity;


public class MainActivity extends Util {

    public static void wait(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        switch (ThemeVar.getData())
        {
            case(0):
                setTheme(R.style.BlackTheme);
                break;
            case(1):
                setTheme(R.style.RedTheme);
                break;
            case(2):
                setTheme(R.style.BlueTheme);
                break;
            default:
                setTheme(R.style.BlueTheme);
                break;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sh
                = getSharedPreferences("AuthSharedPref",
                MODE_PRIVATE);

        if (sh.getInt("USER_ID", -1) == -1) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run()
                {

                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
            }, 1200);
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run()
                {

                    Intent i = new Intent(MainActivity.this, RideActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
            }, 1200);
        }

        //mAuth.signOut();

        //FirebaseUser user = mAuth.getCurrentUser();

        /*
        if (user != null)
        {
            final DatabaseReference settingsReference = databaseReference.child("users").child(mAuth.getUid()).child("settings");

            settingsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Boolean syncTheme = (Boolean) snapshot.child("syncTheme").getValue();

                    if (syncTheme != null && syncTheme) {
                        long themeVar = (long) snapshot.child("themeVar").getValue();

                        ThemeVar.setData((int) themeVar);
                    }
                    else {
                        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        ThemeVar.setData(preferences.getInt(getString(R.string.preference_theme_var), 1));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            // User is signed in

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run()
                {
                    Intent i = new Intent(MainActivity.this, TaskActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
            }, 600);



        }
        else*/

    }
}

//