package com.example.savaari.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.example.savaari.R;
import com.example.savaari.utility.Util;
import com.example.savaari.auth.login.LoginActivity;
import com.example.savaari.services.location.LocationUpdateUtil;

public class SettingsActivity extends Util implements SettingsClickListener {

    private SettingsViewModel settingsViewModel = null;
    private Toolbar myToolbar;
    private boolean inSubSetting = false;
    private UserSettings userSettings;

    // Getters and Setters
    public boolean isInSubSetting() {
        return inSubSetting;
    }
    public void setInSubSetting(boolean inSubSetting) {
        this.inSubSetting = inSubSetting;
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    // Main onCreate Function
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        themeSelect(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        myToolbar = findViewById(R.id.toolbar);
        myToolbar.setTitle("Settings");
        setSupportActionBar(myToolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);

        userSettings = new UserSettings();

        // TODO: ValueEventListener for change in theme settings

        /*
        * If themeChange is true (theme has been changed), then replace with new ThemeFragment
        * else, replace with new SettingsFragment
        */

        if (getIntent().getBooleanExtra("themeChange", false)) {
            setInSubSetting(true);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, ThemeFragment.newInstance(userSettings.isSyncTheme(), userSettings.isAutoDarkTheme()))
                    .commit();
        }
        else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, SettingsFragment.newInstance(this))
                    .commit();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return(super.onOptionsItemSelected(item));
    }

    @Override
    public void onAccountClicked() {
        setInSubSetting(true);
        myToolbar.setTitle("Account");
    }

    @Override
    public void onGeneralClicked() {
        setInSubSetting(true);
        myToolbar.setTitle("General");
    }

    @Override
    public void onThemeClicked() {
        setInSubSetting(true);

        myToolbar.setTitle("Themes");

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, ThemeFragment.newInstance(userSettings.isSyncTheme(), userSettings.isAutoDarkTheme()))
                .commit();
    }

    @Override
    public void onProductivityClicked() {
        setInSubSetting(true);
        myToolbar.setTitle("Productivity");
    }

    @Override
    public void onRemindersClicked() {
        setInSubSetting(true);
        myToolbar.setTitle("Reminders");
    }

    @Override
    public void onNotificationsClicked() {
        setInSubSetting(true);
        myToolbar.setTitle("Notifications");
    }

    @Override
    public void onSupportClicked() {
        setInSubSetting(true);
        myToolbar.setTitle("Support");
    }

    @Override
    public void onLogoutClicked() {
        SharedPreferences sharedPreferences
                = getSharedPreferences("AuthSharedPref", MODE_PRIVATE);

        SharedPreferences.Editor myEdit
                = sharedPreferences.edit();

        myEdit.putInt("USER_ID", -1);
        myEdit.apply();

        // Stopping the Location Service
        LocationUpdateUtil.stopLocationService(SettingsActivity.this);

        Intent i = new Intent(SettingsActivity.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        finishAffinity();
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        if (isInSubSetting()) {
            themeSelect(this);
            setInSubSetting(false);
            
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, SettingsFragment.newInstance(this))
                    .commit();
        }
        else {
            NavUtils.navigateUpFromSameTask(SettingsActivity.this);
        }
    }
}