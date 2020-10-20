package com.example.savaari.ride;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.savaari.LoadDataTask;
import com.example.savaari.OnAuthenticationListener;
import com.example.savaari.R;
import com.example.savaari.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import org.json.JSONException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

public class RideActivity extends Util implements OnMapReadyCallback {

    private static final String TAG = "RideActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private Boolean locationPermissionGranted = false;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final int ERROR_DIALOG_REQUEST = 9001;

    private static final float DEFAULT_ZOOM = 15;

    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private ImageView centerGPSButton;


    // --------------------------------------------------------------------------------
    // Nabeel Attributes
    private Location mUserLocation;

    // Call this function after getting the USER's Locations
    private void saveUserLocation() throws JSONException
    {
        Log.d(TAG, "saveUserLocation: inside!");
        if (mUserLocation != null)
        {
            // Function for Networking POST
            SharedPreferences sh = getSharedPreferences("AuthSharedPref", MODE_PRIVATE);
            int currentUserID = sh.getInt("USER_ID", -1);
            Log.d(TAG, "saveUserLocation: currentUserID: " + currentUserID);
            if (currentUserID != -1)
            {
                Log.d(TAG, "saveUserLocation: Executing sendLocationFunction");
                // Creating new Task
                new LoadDataTask(new OnAuthenticationListener()
                {
                    @Override
                    public void authenticationStatus(int USER_ID)
                    {
                        // Empty
                    }
                }).execute("sendLocation", String.valueOf(currentUserID), String.valueOf(mUserLocation.getLatitude())
                        , String.valueOf(mUserLocation.getLongitude()));
            }
        }
    }
    // --------------------------------------------------------------------------------

    // Main onCreate Function to override
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        themeSelect(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride);

        //searchText = findViewById(R.id.input_search);
        centerGPSButton = findViewById(R.id.user_location);

        getLocationPermission();
    }

    /*
    * Receives autocompleteFragment's result
    * Gets Place Object using 'getPlaceFromIntent()'
    * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1) {
            Log.d("onActivityResult: ", "This happened");

            if (resultCode == AutocompleteActivity.RESULT_OK) {
                assert data != null;
                Place place = Autocomplete.getPlaceFromIntent(data);
                String title = ((place.getName() == null) ?
                        ((place.getAddress() == null) ? "" : place.getAddress()) : place.getName());

                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId() + ", lat: " + place.getLatLng().latitude
                        + ", lon: " + place.getLatLng().longitude);
                moveCamera(Objects.requireNonNull(place.getLatLng()), DEFAULT_ZOOM, title);
            }
            else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            }
            // The user canceled the operation.
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /*
    * Initializes View Objects including:
    * centerGPSButton
    * autocompleteFragment
    */
    private void init() {
        Log.d(TAG, "init: initializing");

        /* moveCamera to user location*/
        centerGPSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation();
            }
        });


        /* Google Places Autocomplete API Initialization */

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_api_key), Locale.US);
        }

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        assert autocompleteFragment != null;
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG, Place.Field.ID, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {

                String title = ((place.getName() == null)?
                        ((place.getAddress() == null)?  "" : place.getAddress()) : place.getName());

                moveCamera(Objects.requireNonNull(place.getLatLng()), DEFAULT_ZOOM, title);

                Log.d("onPlaceSelected: ", "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Toast.makeText(RideActivity.this, "Could not navigate to selected place", Toast.LENGTH_SHORT).show();
                Log.d("init(): ", "onPlaceSelectedListener(): An error occurred: " + status);
            }
        });
    }

    /*
    * Moves camera to param: (latLng, zoom)
    * Adds marker if title specified
    * */
    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving the camera to lat: " + latLng.latitude + ", lng: " + latLng.longitude);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (!title.equals("")) {
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            googleMap.addMarker(options);
        }
    }


    /* Get's device location, calls moveCamera()*/
    private void getDeviceLocation() {
        Log.d("getDeviceLocation", "getting device location");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (locationPermissionGranted) {
                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            /*
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                                    .build();
                            CameraUpdate cu = CameraUpdateFactory.newCameraPosition(cameraPosition);
                            googleMap.animateCamera(cu);*/

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, "");
                            // Calling User Location Save Function
                            try
                            {
                                mUserLocation = currentLocation;
                                saveUserLocation();
                            } catch (JSONException e)
                            {
                                e.printStackTrace();
                            }

                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(RideActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    /*
    * initMap() if permissions granted
    * else, explicitly ask for permission
    * */
    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
                initMap();
                return;
            }
        }

        ActivityCompat.requestPermissions(this, permissions,
                LOCATION_PERMISSION_REQUEST_CODE); //Doesn't matter
    }


    /*
    * Callback from initMap()'s getMapAsync()
    * Initialize GoogleMap Object
    * */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(RideActivity.this, "Map is ready", Toast.LENGTH_SHORT).show();
        this.googleMap = googleMap;

        if (locationPermissionGranted) {

            // Calling the Get Device Location to retrieve the location
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissi ons
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setAllGesturesEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);

            init();
        }
    }

    /*
    * Prerequisite: Map permissions granted
    * Initializes map fragment
    * */
    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        assert mapFragment != null;
        mapFragment.getMapAsync(RideActivity.this);
    }

    /* Callback for when permissions have been granted/denied */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locationPermissionGranted = false;

        switch(requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int permissionIndex = 0 ; permissionIndex < grantResults.length ; ++permissionIndex) {
                        if (grantResults[permissionIndex] != PackageManager.PERMISSION_GRANTED) {
                            locationPermissionGranted = false;
                            return;
                        }
                    }
                    locationPermissionGranted = true;
                    initMap();
                }
            }
        }
    }

    /*
    * Checks if device's Google Play Services are available
    * TODO: call this before getLocationPermission() in onCreate()
    *  */
    public boolean isServicesOK() {
        Log.d("isServicesOK: ", "checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(RideActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            Log.d("PLAY SERVICES: ", "WORKING");
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.d("PLAY SERVICES", "ERROR, BUT FIXABLE");

            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(RideActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
            return true;
        }
        else {
            Toast.makeText(this, "Error. Map services unavailable", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}