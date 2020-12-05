package com.example.savaari.ride;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.savaari.R;
import com.example.savaari.SavaariApplication;
import com.example.savaari.ThemeVar;
import com.example.savaari.Util;
import com.example.savaari.ride.adapter.OnItemClickListener;
import com.example.savaari.ride.adapter.RideTypeAdapter;
import com.example.savaari.ride.adapter.RideTypeItem;
import com.example.savaari.services.location.LocationUpdateUtil;
import com.example.savaari.settings.SettingsActivity;
import com.example.savaari.user.UserLocation;
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
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
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
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class RideActivity extends Util implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener,
        GoogleMap.OnPolylineClickListener, GoogleMap.OnInfoWindowClickListener, OnItemClickListener {

    private static final String TAG = "RideActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private Boolean locationPermissionGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final int ERROR_DIALOG_REQUEST = 9001;

    private static final float DEFAULT_ZOOM = 15;

    /* Map API Objects*/
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GeoApiContext geoApiContext = null;
    private ImageView centerGPSButton;

    /* Drawing the route on Maps*/
    private Polyline destinationPolyline = null;
    private Marker destinationMarker = null;

    private Marker pickupMarker = null;
    private Polyline pickupPolyline = null;

    /* Nav Views */
    private DrawerLayout drawer;
    LinearLayout searchBar;

    private ImageButton menuButton;
    private NavigationView navigationView;
    private View headerView;
    private TextView navUsername, navEmail;

    LinearLayout rideSelectPanel;
    LinearLayout rideConfigBar;
    ProgressBar progressBar;
    private Button searchRideButton;

    LinearLayout rideTypeSelector;
    LinearLayout paymentMethodSelector;
    LinearLayout rideTypePanel;
    ImageView rideTypeImage;
    TextView rideTypeName;
    TextView rideTypePrice;
    TextView rideTypeHeader;
    ArrayList<RideTypeItem> rideTypeItems;

    LinearLayout rideDetailsPanel;
    ImageView riderImage;
    TextView rideStatusMessage;
    TextView driverName;
    RatingBar ratingBar;

    private RideViewModel rideViewModel = null;
    /* State variables - references to ViewModel variables */
    private int USER_ID = -1;
    private ArrayList<UserLocation> mUserLocations;

    /* User Objects, driver & rider references to instances belonging to Ride Object*/
    private Ride ride = null;
    private Marker driverMarker;
    private LatLng userLocation;    //Updated location of device


    private int FIND_DRIVER_ATTEMPTS = 0;
    private int FIND_DRIVER_ATTEMPTS_LIMIT = 2;


    // Main onCreate Function to override
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        themeSelect(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        /* Register Broadcast Receiver for NetworkService */
        //registerRideReceiver();

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
            rideViewModel = new ViewModelProvider(this, new RideViewModelFactory(USER_ID,
                    ((SavaariApplication) this.getApplication()).getRepository())).get(RideViewModel.class);
            getLocationPermission();

            rideTypeItems = new ArrayList<>();


            // set up the RecyclerView
            rideTypeItems.add(new RideTypeItem(R.drawable.ic_rtype_bike, "Bike", "PKR 81"));
            rideTypeItems.add(new RideTypeItem(R.drawable.ic_car, "Smol Car", "PKR 173"));
            rideTypeItems.add(new RideTypeItem(R.drawable.ic_car, "Med Car", "PKR 224"));
            rideTypeItems.add(new RideTypeItem(R.drawable.ic_car, "Big Car", "PKR 443"));
            RideTypeAdapter rideTypeAdapter = new RideTypeAdapter(rideTypeItems, this);
            RecyclerView recyclerView = findViewById(R.id.select_ride_type);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(rideTypeAdapter);
        }
    }


    /*  ---------- User data ViewModel calls and Observers ------------ */

    /* Loads user data from database */
    private void loadUserData() {
        rideViewModel.loadUserData();
        rideViewModel.isLiveUserDataLoaded().observe(this, this::onUserDataLoaded);
    }
    private void onUserDataLoaded(Boolean dataLoaded) {
        if (dataLoaded) {
            navUsername.setText(rideViewModel.getRide().getRider().getUsername());
            navEmail.setText(rideViewModel.getRide().getRider().getEmailAddress());
            Toast.makeText(RideActivity.this, "User data loaded!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(RideActivity.this, "Data could not be loaded", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadUserLocations() {
        rideViewModel.loadUserLocations();
        rideViewModel.isLiveUserLocationsLoaded().observe(this, this::onUserLocationsLoaded);
    }
    private void onUserLocationsLoaded(Boolean locationsLoaded) {
        if (locationsLoaded) {
            mUserLocations = rideViewModel.getUserLocations();
            Log.d(TAG, "loadUserLocations: Started!");

            // Testing Code
            Log.d(TAG, "loadUserLocations: mUserLocations.size(): " + mUserLocations.size());
            for (int i = 0; i < mUserLocations.size(); ++i) {
                Log.d(TAG, "loadUserLocations: setting Markers");
                MarkerOptions option = new MarkerOptions()
                        .position(new LatLng(mUserLocations.get(i).getLatitude(), mUserLocations.get(i).getLongitude()));
                googleMap.addMarker(option);
            }
            Toast.makeText(RideActivity.this, "User locations loaded!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(RideActivity.this, "User locations could not be loaded", Toast.LENGTH_SHORT).show();
        }
    }

    /* ----------- END OF ViewModel CALLS AND OBSERVERS ---------------- */



    /* -------Matchmaking & In-Ride ViewModel CALLKS AND OBSERVERS -----*/

    private void findDriver() {
        if (FIND_DRIVER_ATTEMPTS > FIND_DRIVER_ATTEMPTS_LIMIT) {
            Toast.makeText(RideActivity.this, "Sorry, we could not find a driver", Toast.LENGTH_SHORT).show();
        }
        else { // Attempt to find a driver
            rideViewModel.findDriver(USER_ID, ride.getPickupLocation(), ride.getDropoffLocation());
        }
    }

    // Called if a ride is found, forwards new rides to newRideFoundAction()
    private void rideFoundAction(Ride ride) {

        driverName.setText(rideViewModel.getRide().getDriver().getUsername());
        ratingBar.setRating(rideViewModel.getRide().getDriver().getRating());

        if (ride.getMatchStatus() == Ride.ALREADY_PAIRED) {
            switch (ride.getRideStatus()) {
                case Ride.PICKUP:
                    setRoute(rideViewModel.getRide().getPickupLocation(), rideViewModel.getRide().getDropoffLocation(), "");
                    MarkerOptions options = new MarkerOptions().position(ride.getPickupLocation())
                            .title("Pickup point");
                    pickupMarker = googleMap.addMarker(options);
                    calculateDirections(ride.getDriver().getCurrentLocation(), pickupMarker, false);

                    rideStatusMessage.setText(rideViewModel.getRide().getDriver().getUsername() + " is arriving in 5 minutes");
                    toggleRideDetailsBar(true, true);

                    startPickupAction();
                    break;

                case Ride.DRIVER_ARRIVED:
                    setRoute(rideViewModel.getRide().getPickupLocation(), rideViewModel.getRide().getDropoffLocation(), "");
                    rideStatusMessage.setText(ride.getDriver().getUsername() + " has arrived");
                    toggleRideDetailsBar(true, true);
                    break;

                case Ride.CANCELLED:
                    Toast.makeText(this, "Your ride was cancelled. Trying again...", Toast.LENGTH_SHORT).show();
                    findDriver();
                    break;

                case Ride.STARTED:
                    setRoute(rideViewModel.getRide().getPickupLocation(), rideViewModel.getRide().getDropoffLocation(), "");
                    rideStatusMessage.setText("Estimated Time to Destination: 6 min");
                    toggleRideDetailsBar(true, true);
                    break;

                case Ride.COMPLETED:
                    Toast.makeText(this, "Thank you for riding with Savaari", Toast.LENGTH_SHORT).show();
                    break;
            }
            watchRideStatus();
        }
        else {
            newRideFoundAction(ride);
        }
    }

    // Handles new ride found logic
    private void newRideFoundAction(Ride ride) {
        String findStatusMessage;

        switch (ride.getMatchStatus()) {
            case Ride.PAIRED:
                MarkerOptions options = new MarkerOptions().position(ride.getPickupLocation())
                        .title("Pickup point");
                pickupMarker = googleMap.addMarker(options);
                calculateDirections(ride.getDriver().getCurrentLocation(), pickupMarker, false);

                findStatusMessage = "Driver found: id: " + ride.getDriver().getUserID() + ", name: " + ride.getDriver().getUsername();

                rideStatusMessage.setText(rideViewModel.getRide().getDriver().getUsername() + " is arriving in 5 minutes");
                toggleRideDetailsBar(true, true);

                startPickupAction();
                watchRideStatus();
                break;

            case Ride.NOT_PAIRED:
                findStatusMessage = "Sorry, we couldn't find you a ride. Try again";
                backToSearchRide();
                break;

            case Ride.NOT_FOUND:
                findStatusMessage = "Few rides available. Try again soon!";
                backToSearchRide();
                break;

            case Ride.DEFAULT:
                backToSearchRide();
                return;

            default:
                findStatusMessage = "There was a problem in finding a ride";
                backToSearchRide();
                break;
        }

        Toast.makeText(RideActivity.this, findStatusMessage, Toast.LENGTH_SHORT).show();
    }

    private void watchRideStatus() {
        Log.d(TAG, "watchRideStatus() called!");
        ((SavaariApplication) getApplication()).scheduledExecutor.scheduleWithFixedDelay((Runnable) () -> rideViewModel.getRideStatus(),
                0L, 8L, TimeUnit.SECONDS);
        rideViewModel.isRideStatusChanged().observe(RideActivity.this, this::onRideStatusChanged);
    }

    private void onRideStatusChanged(Boolean statusChanged) {
        if (statusChanged) {
            switch (ride.getRideStatus()) {
                case Ride.DRIVER_ARRIVED:
                    pickupPolyline.remove();
                    rideStatusMessage.setText(rideViewModel.getRide().getDriver().getUsername() + " has arrived");
                    break;
                case Ride.CANCELLED:
                    Toast.makeText(this, "Your ride was cancelled. Trying again...", Toast.LENGTH_SHORT).show();
                    toggleRideDetailsBar(false, true);
                    findDriver();
                    break;
                case Ride.STARTED:
                    rideStatusMessage.setText("Estimated Time to Destination: 6 min");
                    //Toast.makeText(this, "Your ride was cancelled. Trying again...", Toast.LENGTH_SHORT).show();
                    break;
                case Ride.COMPLETED:
                    Toast.makeText(this, "Thank you for riding with Savaari", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    }

    /*
     * Launches thread that fetches driver location updates
     * Sets & updates driver location Marker
     * provides UI elements for editing ride options
     *
     * Executes From: Driver & Rider matched
     * To: Driver arrives at pickup point
     * */
    private void startPickupAction() {
        progressBar.setVisibility(View.INVISIBLE);

        ((SavaariApplication) getApplication()).scheduledExecutor.scheduleWithFixedDelay((Runnable) () -> rideViewModel.fetchDriverLocation(),
                0L, 8L, TimeUnit.SECONDS);

        MarkerOptions options = new MarkerOptions()
                .position(ride.getDriver().getCurrentLocation())
                .title("Driver: " + ride.getDriver().getUsername());
        driverMarker = googleMap.addMarker(options);

        rideViewModel.isDriverLocationFetched().observe(RideActivity.this, isFetched -> {
            if (isFetched) {
                driverMarker.setPosition(rideViewModel.getRide().getDriver().getCurrentLocation());
            }
        });
    }

    private void backToSearchRide() {
        progressBar.setVisibility(View.INVISIBLE);
        toggleRideSearchBar(true, true);

    }

    private void onSearchRideAction() {
        if (ride.getPickupLocation() == null) {
            ride.setPickupLocation(userLocation, "Current location");
        }
        if (ride.getDropoffLocation() == null) {
            Toast.makeText(RideActivity.this, "Add dropoff location!", Toast.LENGTH_SHORT).show();
        }
        else {
            setRoute(null, null, "");

            toggleRideSearchBar(false, true);
            progressBar.setVisibility(View.VISIBLE);

            LocationUpdateUtil.stopLocationService(RideActivity.this);

            FIND_DRIVER_ATTEMPTS = 0;
            findDriver();
        }
    }

    /* ---- END OF Matchmaking & In-Ride ViewModel CALLS AND OBSERVERS --*/



    /* ------------ START OF GOOGLE MAPS API FUNCTIONALITY -------------*/
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

    /*
     * Callback from initMap()'s getMapAsync()
     * Initialize GoogleMap Object
     * */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(RideActivity.this, "Map is ready", Toast.LENGTH_SHORT).show();
        this.googleMap = googleMap;
        if (ThemeVar.getData()%2 == 0) {
            googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style));
        }

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
    private void setRoute(LatLng src, LatLng dest, String title) {
        //Update ride with the updated pickup/dropoff location
        if (src != null) {
            ride.setPickupLocation(src, title);
        }
        if (dest != null) {
            ride.setDropoffLocation(dest, title);

            if (destinationMarker != null) {
                destinationMarker.remove();
            }

            MarkerOptions options = new MarkerOptions()
                    .position(ride.getDropoffLocation())
                    .title(title);
            destinationMarker = googleMap.addMarker(options);
        }

        Log.d("RideActivity: ", "setRoute: pickup: " + (ride.getPickupLocation() == null) + ", dropoff: " + (ride.getDropoffLocation() == null));

        //If ride has valid pickup & dropoff locations, draw route
        if (ride.getPickupLocation() != null && ride.getDropoffLocation() != null) {
            moveCamera(ride.getDropoffLocation(), DEFAULT_ZOOM, title);
            calculateDirections(ride.getPickupLocation(), destinationMarker, true);
        }
    }

    /*
     * Initialize Autocomplete Support Fragment
     * onPlaceSelected() implementation
     */
    private void initializeAutocomplete() {
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_api_key), Locale.US);
        }

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragmentDest = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment_dest),
        autocompleteFragmentSrc = (AutocompleteSupportFragment) getSupportFragmentManager().
                findFragmentById(R.id.autocomplete_fragment_src);

        // Specify the types of place data to return.
        assert autocompleteFragmentDest != null;
        autocompleteFragmentDest.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG, Place.Field.ID, Place.Field.NAME));
        assert autocompleteFragmentSrc != null;
        autocompleteFragmentSrc.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG, Place.Field.ID, Place.Field.NAME));

        //Set the hint text in both fragments
        View srcFragmentView = autocompleteFragmentSrc.getView(),
        destFragmentView = autocompleteFragmentDest.getView();
        EditText srcTextInput = srcFragmentView.findViewById(R.id.places_autocomplete_search_input),
        destTextInput = destFragmentView.findViewById(R.id.places_autocomplete_search_input);
        srcTextInput.setHint(R.string.add_source);
        srcTextInput.setTextSize(18);
        destTextInput.setHint(R.string.add_dest);
        destTextInput.setTextSize(18);

        // Source selected listener
        autocompleteFragmentSrc.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                //TODO: set source
                Log.d("HERE!!!", "SOURCE SELECTED");
                setRoute(place.getLatLng(), null, "");
            }

            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(RideActivity.this, "Could not navigate from selected place", Toast.LENGTH_SHORT).show();
                Log.d("init(): ", "Source: onPlaceSelectedListener(): An error occurred: " + status);
            }
        });

        // Destination selected listener
        autocompleteFragmentDest.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {

                Log.d("HERE!!!", "DEST SELECTED");
                String title = ((place.getName() == null)?
                        ((place.getAddress() == null)?  "" : place.getAddress()) : place.getName());

                setRoute(null, Objects.requireNonNull(place.getLatLng()), title);

                Log.d("onPlaceSelected: ", "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Toast.makeText(RideActivity.this, "Could not navigate to selected place", Toast.LENGTH_SHORT).show();
                Log.d("init(): ", "Dest: onPlaceSelectedListener(): An error occurred: " + status);
            }
        });
    }

    private void initializeViews() {
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

        searchBar = findViewById(R.id.search_bar);
        rideSelectPanel = findViewById(R.id.ride_select_panel);
        progressBar = findViewById(R.id.progressBar);

        rideTypeSelector = findViewById(R.id.ride_type_config);
        paymentMethodSelector = findViewById(R.id.payment_config);
        rideConfigBar = findViewById(R.id.ride_config);

        searchRideButton = findViewById(R.id.go_btn);
        searchRideButton.setEnabled(false);

        ratingBar = findViewById(R.id.rider_rating);
        riderImage = findViewById(R.id.rider_img);
        driverName = findViewById(R.id.rider_name);
        rideStatusMessage = findViewById(R.id.ride_status_txt);
        rideDetailsPanel = findViewById(R.id.ride_details_panel);

        rideTypeHeader = findViewById(R.id.ride_type_header);
        rideTypeImage = findViewById(R.id.ride_type_img);
        rideTypeName = findViewById(R.id.ride_type_name);
        rideTypePrice = findViewById(R.id.ride_type_price);
        rideTypePanel = findViewById(R.id.ride_type_panel);

        rideConfigBar.setOnClickListener(v -> {
            rideTypeHeader.setText(R.string.select_ride_type);
            toggleRideTypePanel(true, true);
        });
    }

    /*
     * Initializes View Objects including:
     * centerGPSButton
     * autocompleteFragment
     */
    private void init() {
        Log.d(TAG, "init: initializing");


        initializeViews();
        toggleRideSearchBar(false, false);
        toggleRideDetailsBar(false, false);

        // Set observer for ViewModel:driverPaired
        rideViewModel.isRideFound().observe(this, this::rideFoundAction);

        // Load complete user data
        loadUserData();
        loadUserLocations();
        rideViewModel.loadRide();

        //Initialize user objects
        ride = rideViewModel.getRide();

        initializeAutocomplete();

        searchRideButton.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            onSearchRideAction();
        });
        centerGPSButton.setOnClickListener(v -> getDeviceLocation()); //moveCamera to user location
    }

    // Set visibility of Ride Search bar
    private void toggleRideSearchBar(boolean visibility, boolean withAnimation) {
        if (visibility) {
            if (withAnimation) {
                rideSelectPanel.setAnimation(inFromBottomAnimation(400));
                searchBar.setAnimation(inFromRightAnimation(400));
            }
            rideSelectPanel.setVisibility(View.VISIBLE);
            searchBar.setVisibility(View.VISIBLE);
            searchRideButton.setEnabled(true);
        }
        else {
            if (withAnimation) {
                rideSelectPanel.setAnimation(outToBottomAnimation(400));
                searchBar.setAnimation(outToRightAnimation(400));
            }
            rideSelectPanel.setVisibility(View.GONE);
            searchBar.setVisibility(View.GONE);
            searchRideButton.setEnabled(false);
        }
    }

    // Set visibility of the Ride Details bar
    private void toggleRideDetailsBar(boolean visibility, boolean withAnimation) {
        if (visibility) {
            if (withAnimation) {
                rideDetailsPanel.setAnimation(inFromBottomAnimation(400));
            }
            rideDetailsPanel.setVisibility(View.VISIBLE);
        }
        else {
            if (withAnimation) {
                rideDetailsPanel.setAnimation(outToBottomAnimation(400));
            }
            rideDetailsPanel.setVisibility(View.GONE);
        }
    }

    private void toggleRideTypePanel(boolean visibility, boolean withAnimation) {
        if (visibility) {
            if (withAnimation) {
                rideTypePanel.setAnimation(inFromBottomAnimation(400));
            }
            rideTypePanel.setVisibility(View.VISIBLE);
        }
        else {
            if (withAnimation) {
                rideTypePanel.setAnimation(outToBottomAnimation(400));
            }
            rideTypePanel.setVisibility(View.GONE);
        }
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
                location.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: found location!");
                        Location currentLocation = (Location) task.getResult();

                        moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, "");

                        // Calling User Location Save Function
                        try
                        {
                            userLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            rideViewModel.setUserCoordinates(userLocation.latitude, userLocation.longitude);
                            LocationUpdateUtil.saveUserLocation(userLocation, RideActivity.this);

                            // Starting Background Location Service
                            ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                            LocationUpdateUtil.startLocationService(manager, RideActivity.this);

                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    } else {
                        Log.d(TAG, "onComplete: current location is null");
                        Toast.makeText(RideActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }


    /*
     * Calculates directions from sourceLocation to destinationMarker
     */
    private void calculateDirections(LatLng sourceLocation, Marker destinationMarker, boolean sourceToDest) {
        Log.d(TAG, "calculateDirections: calculating directions.");

        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                destinationMarker.getPosition().latitude,
                destinationMarker.getPosition().longitude
        );
        DirectionsApiRequest directions = new DirectionsApiRequest(geoApiContext);

        directions.alternatives(false);
        directions.origin(
                new com.google.maps.model.LatLng(
                        sourceLocation.latitude,
                        sourceLocation.longitude
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

                addPolylinesToMap(result, sourceToDest);
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

    private void addPolylinesToMap(final DirectionsResult result, boolean sourceToDest){

        /*
         * Posting to main thread
         * since this method is called from a different context
         * changes to google map must be made on the same thread as the one it is on
         */
        new Handler(Looper.getMainLooper()).post(() -> {
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
            if (sourceToDest) {
                if (destinationPolyline != null) {
                    destinationPolyline.remove();

                    if (pickupPolyline != null) {
                        pickupPolyline.remove();
                    }
                }

                destinationPolyline = googleMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                destinationPolyline.setColor(ContextCompat.getColor(RideActivity.this, R.color.maps_blue));
                destinationMarker.setSnippet("Duration: " + route.legs[0].duration);
                destinationMarker.showInfoWindow();
            }
            else {
                if (pickupPolyline != null) {
                    destinationPolyline.remove();
                }
                pickupPolyline = googleMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                pickupPolyline.setColor(ContextCompat.getColor(RideActivity.this, R.color.success_green));
                pickupMarker.setSnippet("Duration: " + route.legs[0].duration);
                pickupMarker.showInfoWindow();
            }

            zoomRoute(newDecodedPath);

            searchRideButton.setEnabled(true);

            //}
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
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
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
            case (R.id.nav_help):
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Cancel scheduled but not started task, and avoid new ones
        ((SavaariApplication) getApplication()).scheduledExecutor.shutdown();

        //Wait for the running tasks
        try {
            ((SavaariApplication) getApplication()).scheduledExecutor.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Interrupt the threads and shutdown the scheduler
        ((SavaariApplication) getApplication()).scheduledExecutor.shutdownNow();
    }

    @Override
    public void OnClick(int position) {
        rideViewModel.getRide().setRideType(position);
        toggleRideTypePanel(false, true);

        rideTypeImage.setImageResource(rideTypeItems.get(position).getRideTypeImage());
        rideTypeName.setText(rideTypeItems.get(position).getRideTypeName());
        rideTypePrice.setText(rideTypeItems.get(position).getRideTypePrice());
    }
}