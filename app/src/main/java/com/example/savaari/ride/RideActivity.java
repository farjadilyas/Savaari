package com.example.savaari.ride;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import com.example.savaari.LoadDataTask;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.example.savaari.LoadDataTask;
import com.example.savaari.OnAuthenticationListener;
import com.example.savaari.R;
import com.example.savaari.UserLocation;
import com.example.savaari.Util;
import com.example.savaari.services.LocationUpdateService;


import com.example.savaari.OnDataLoadedListener;
import com.example.savaari.R;
import com.example.savaari.Util;
import com.example.savaari.services.LocationUpdateUtil;
import com.example.savaari.settings.SettingsActivity;

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

import com.google.android.gms.maps.model.LatLngBounds;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import com.google.android.material.navigation.NavigationView;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class RideActivity extends Util implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener,
        GoogleMap.OnPolylineClickListener, GoogleMap.OnInfoWindowClickListener {

    private static final String TAG = "RideActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private Boolean locationPermissionGranted = false;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final int ERROR_DIALOG_REQUEST = 9001;

    private static final float DEFAULT_ZOOM = 15;

    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private GeoApiContext geoApiContext = null;

    private ImageView centerGPSButton;

    /* Drawing the route on Maps*/
    private Polyline destinationPolyline = null;
    private DirectionsLeg destinationLeg = null;
    private Marker destinationMarker = null;


    private DrawerLayout drawer;
    private ImageButton menuButton;
    private NavigationView navigationView;
    private View headerView;
    private TextView navUsername, navEmail;

    private int USER_ID = -1;
    private ArrayList<UserLocation> mUserLocations;

   // --------------------------------------------------------------------------------
    // --------------------[ LOCATION BACKGROUND SERVICE ]-----------------------------
    // --------------------------------------------------------------------------------
    // Nabeel Attributes
    private Location mUserLocation;
  

    // Call this function after getting the USER's Locations
    private void saveUserLocation()
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
                new LoadDataTask(null, null).execute("sendLocation", String.valueOf(currentUserID), String.valueOf(mUserLocation.getLatitude())
                        , String.valueOf(mUserLocation.getLongitude()));
            }
        }
    }


    // Check if the background location service is running
    private boolean isLocationServiceRunning()
    {
        // Iterating over all services to check if the service is running
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
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
    private void startLocationService()
    {
        if (!isLocationServiceRunning())
        {
            Intent serviceIntent = new Intent(this, LocationUpdateService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                RideActivity.this.startForegroundService(serviceIntent);
            }
            else
            {
                startService(serviceIntent);
            }
        }
    }


    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------

    // Main onCreate Function to override
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        themeSelect(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride);

        Intent recvIntent = getIntent();
        USER_ID = recvIntent.getIntExtra("USER_ID", -1);

        mUserLocations = new ArrayList<>();

        if (USER_ID == -1) {
            SharedPreferences sh
                    = getSharedPreferences("AuthSharedPref",
                    MODE_PRIVATE);

            USER_ID = sh.getInt("USER_ID", -1);
        }

        if (USER_ID == -1) {
            Toast.makeText(RideActivity.this, "Sorry. We can not authenticate you", Toast.LENGTH_LONG).show();
        }
        else {
            centerGPSButton = findViewById(R.id.user_location);

            getLocationPermission();
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
     * Prerequisite: Map permissions granted
     * Initializes map fragment
     * */
    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        assert mapFragment != null;
        mapFragment.getMapAsync(RideActivity.this);

        if (geoApiContext == null) {
            geoApiContext = new GeoApiContext.Builder()
                    .apiKey(getString(R.string.directions_api_key))
                    .build();
        }
    }

    // Function for loading User Location Data
    // Nabeel Danish
    private void loadUserLocations()
    {
        Log.d(TAG, "loadUserLocations: Started!");
        new LoadDataTask(null, new OnDataLoadedListener()
        {
            @Override
            public void onDataLoaded(Object object)
            {
                try {
                    JSONArray resultArray = (JSONArray) object;
                    Log.d(TAG, "loadUserLocations: " + resultArray.toString());
                    if (resultArray != null)
                    {
                        Log.d(TAG, "loadUserLocations: found JSON Array");
                        for (int i = 0; i < resultArray.length(); i++) {
                            JSONObject obj = resultArray.getJSONObject(i);
                            UserLocation userLocation = new UserLocation();

                            // Assigning User Objects
                            userLocation.setUserID(obj.getInt("USER_ID"));
                            userLocation.setLatitude(obj.getDouble("LATITUDE"));
                            userLocation.setLongitude(obj.getDouble("LONGITUDE"));
                            userLocation.setTimestamp(obj.getString("TIMESTAMP"));

                            // Adding Final Object
                            mUserLocations.add(userLocation);
                            Log.d(TAG, "loadUserLocations: userID: " + userLocation.getUserID());
                            Log.d(TAG, "loadUserLocations: latitude: " + userLocation.getLatitude());
                            Log.d(TAG, "loadUserLocations: longitude: " + userLocation.getLongitude());
                            Log.d(TAG, "loadUserLocations: timestamp: " + userLocation.getTimestamp());
                        }
                    }
                }
                catch (Exception e)
                {
                    Log.d(TAG, "Exception in loadUserLocations");
                    e.printStackTrace();
                }
                // Testing Code
                Log.d(TAG, "loadUserLocations: mUserLocations.size(): " + mUserLocations.size());
                for (int i = 0; i < mUserLocations.size(); ++i)
                {
                    Log.d(TAG, "loadUserLocations: setting Markers");
                    MarkerOptions option = new MarkerOptions()
                            .position(new LatLng(mUserLocations.get(i).getLatitude(), mUserLocations.get(i).getLongitude()));
                    googleMap.addMarker(option);
                }
            }
        }).execute("getUserLocations");
    }

    /*
     * Callback from initMap()'s getMapAsync()
     * Initialize GoogleMap Object
     * */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(RideActivity.this, "Map is ready", Toast.LENGTH_SHORT).show();
        this.googleMap = googleMap;
        googleMap.setOnPolylineClickListener(this);

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
            googleMap.setOnInfoWindowClickListener(this);

            init();
        }
    }

    /*
     * Moves camera to param: (latLng, zoom)
     * Adds marker if title specified
     * */
    private void moveCamera(LatLng latLng, float zoom, String title) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }


    /* Method for adding a destination */
    private void setDestination(LatLng latLng, String title) {
        moveCamera(latLng, DEFAULT_ZOOM, title);

        if (destinationMarker != null) {
            destinationMarker.remove();
        }

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(title);
        destinationMarker = googleMap.addMarker(options);

        calculateDirections(destinationMarker);
    }

    /*
    * Initialize Autocomplete Support Fragment
    * onPlaceSelected() implementation
    */
    private void initializeAutocomplete() {
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

                setDestination(Objects.requireNonNull(place.getLatLng()), title);

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

    private void initializeNavigationBar() {
        drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerView = navigationView.getHeaderView(0);
        navUsername = headerView.findViewById(R.id.header_nickname);
        navEmail = headerView.findViewById(R.id.header_email);
        menuButton = findViewById(R.id.menu_btn);

        menuButton.setOnClickListener(v -> {
            if (!drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
    }

    /*
     * Initializes View Objects including:
     * centerGPSButton
     * autocompleteFragment
     */
    private void init() {
        Log.d(TAG, "init: initializing");


        // Calling Load Data Functions
        loadUserData();
        loadUserLocations();

        centerGPSButton.setOnClickListener(v -> getDeviceLocation()); //moveCamera to user location

        /* Google Places Autocomplete API Initialization */
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_api_key), Locale.US);
        }

        initializeNavigationBar();
        initializeAutocomplete();
    }

    /*
     * Loads user data from database
     */
    private void loadUserData() {
        new LoadDataTask(null, object -> {
            if (object == null) {
                Toast.makeText(RideActivity.this, "Network Connection failed", Toast.LENGTH_SHORT).show();
            }
            else {
                JSONObject jsonObject = (JSONObject) object;
                try {
                    navUsername.setText(jsonObject.getString("USER_NAME"));
                    navEmail.setText(jsonObject.getString("EMAIL_ADDRESS"));
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("loadUserData(): ", "JSONException");
                }
            }
        }).execute("loadData", String.valueOf(USER_ID));
    }

    /*
    * Receives autocompleteFragment's result (callback)
    * Gets Place Object using 'getPlaceFromIntent()'
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1) {

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

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, "");

                            // Calling User Location Save Function
                            try
                            {
                                mUserLocation = currentLocation;
                                saveUserLocation();

                                // Starting Background Location Service
                                ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                                LocationUpdateUtil.startLocationService(manager, RideActivity.this);

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
    * Calculates directions from userLocation to marker
    */
    private void calculateDirections(Marker marker){
        Log.d(TAG, "calculateDirections: calculating directions.");

        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                marker.getPosition().latitude,
                marker.getPosition().longitude
        );
        DirectionsApiRequest directions = new DirectionsApiRequest(geoApiContext);

        directions.alternatives(false);
        directions.origin(
                new com.google.maps.model.LatLng(
                        mUserLocation.getLatitude(),
                        mUserLocation.getLongitude()
                )
        );
        Log.d(TAG, "calculateDirections: destination: " + destination.toString());
        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                Log.d(TAG, "calculateDirections: routes: " + result.routes[0].toString());
                Log.d(TAG, "calculateDirections: duration: " + result.routes[0].legs[0].duration);
                Log.d(TAG, "calculateDirections: distance: " + result.routes[0].legs[0].distance);
                Log.d(TAG, "calculateDirections: geocodedWayPoints: " + result.geocodedWaypoints[0].toString());

                addPolylinesToMap(result);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, "calculateDirections: Failed to get directions: " + e.getMessage() );

            }
        });
    }

    /* Given a list of 'checkpoints, this zooms in on the route '*/
    public void zoomRoute(List<LatLng> lstLatLngRoute) {

        if (googleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);

        int routePadding = 120;
        LatLngBounds latLngBounds = boundsBuilder.build();

        googleMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding),
                600,
                null
        );
    }

    private void addPolylinesToMap(final DirectionsResult result){

        /*
        * Posting to main thread
        * since this method is called from a different context
        * changes to google map must be made on the same thread as the one it is on
        */
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: result routes: " + result.routes.length);

                /* Loops through possible routes*/
                //for(DirectionsRoute route: result.routes){
                DirectionsRoute route = result.routes[0];
                Log.d(TAG, "run: leg: " + route.legs[0].toString());

                /* get list of LatLng corresponding to each 'checkpoint' along the route */
                List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                List<LatLng> newDecodedPath = new ArrayList<>();

                // This loops through all the LatLng coordinates of ONE polyline.
                for(com.google.maps.model.LatLng latLng: decodedPath){

                    newDecodedPath.add(new LatLng(
                            latLng.lat,
                            latLng.lng
                    ));
                }

                /* Add all the 'checkpoints' to the polyline */
                if (destinationPolyline != null)
                    destinationPolyline.remove();
                destinationPolyline = googleMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                destinationPolyline.setColor(ContextCompat.getColor(RideActivity.this, R.color.maps_blue));
                destinationPolyline.setClickable(true);
                destinationLeg = route.legs[0];
                destinationMarker.setSnippet("Duration: " + route.legs[0].duration);
                destinationMarker.showInfoWindow();

                zoomRoute(newDecodedPath);

                //}
            }
        });
    }

    /* listener for polyline clicks */
    @Override
    public void onPolylineClick(Polyline polyline) {
        //TODO: Highlight more specific details (maybe?)
        //polyline.setColor(ContextCompat.getColor(RideActivity.this, R.color.maps_blue));
        //polyline.setZIndex(1);

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(RideActivity.this);
        builder.setMessage("Open Google Maps?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        String latitude = String.valueOf(marker.getPosition().latitude);
                        String longitude = String.valueOf(marker.getPosition().longitude);
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");

                        try{
                            if (mapIntent.resolveActivity(RideActivity.this.getPackageManager()) != null) {
                                startActivity(mapIntent);
                            }
                        }catch (NullPointerException e){
                            Log.e(TAG, "onClick: NullPointerException: Couldn't open map." + e.getMessage() );
                            Toast.makeText(RideActivity.this, "Couldn't open map", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
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

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        drawer.closeDrawer(GravityCompat.START);

        switch (item.getItemId()) {
            case (R.id.nav_your_trips):
                break;
            case (R.id.nav_help):
                break;
            case (R.id.nav_wallet):
                break;
            case (R.id.nav_settings):
                Intent i = new Intent(RideActivity.this, SettingsActivity.class);
                startActivity(i);
                finish();
                break;
        }
        return true;
    }
}