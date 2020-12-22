package com.savaari_demo.entity;

import com.savaari_demo.OracleDBHandler;

import java.util.ArrayList;

public class Driver extends User
{
	// Status flags values
	public static final int
		DV_DEFAULT = 0,
		DV_REQ_SENT = 1,
		DV_REQ_REJECTED= 2,
		DV_REQ_APPROVED = 3;

	// Main Attributes
	private static final String LOG_TAG = Driver.class.getSimpleName();
	private Boolean active;
	private Boolean takingRide;
	private int rideRequestStatus;
	private String CNIC;
	private String licenseNumber;
	private int status;
	private int activeVehicleID;
	private ArrayList<Vehicle> vehicles;

	// Main Constructors
	public Driver() {

	}

	public Driver(Location currentLocation, Boolean active, Boolean takingRide) {
		super();
		setCurrentLocation(currentLocation);
		setActive(active);
		setTakingRide(takingRide);
	}
	
	// Getters and Setters
	public Boolean isActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public Boolean isTakingRide() {
		return takingRide;
	}
	public void setTakingRide(Boolean takingRide) {
		this.takingRide = takingRide;
	}
	public int getRideRequestStatus() {
		return rideRequestStatus;
	}
	public void setRideRequestStatus(int rideRequestStatus) {
		this.rideRequestStatus = rideRequestStatus;
	}

	public String getCNIC() {
		return CNIC;
	}

	public void setCNIC(String CNIC) {
		this.CNIC = CNIC;
	}

	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getActiveVehicleID() {
		return activeVehicleID;
	}

	public void setActiveVehicleID(int activeVehicleID) {
		this.activeVehicleID = activeVehicleID;
	}

	// Main Methods for System Interactions

	// Sign-UP
	public boolean signup() {
		return OracleDBHandler.getInstance().addDriver(this);
	}

	// Login
	public Integer login()
	{
		return OracleDBHandler.getInstance().loginDriver(this);
	}

	// Fetch Data
	public boolean fetchData()
	{
		return OracleDBHandler.getInstance().fetchDriverData(this);
	}

	// Set Mark Active
	public boolean setMarkActive()
	{
		return OracleDBHandler.getInstance().markDriverActive(this);
	}

	// Check Ride Request Status
	public RideRequest checkRideRequestStatus() {
		return OracleDBHandler.getInstance().checkRideRequestStatus(this, 20000);
	}

	// Start Matchmaking service in Server
	public RideRequest startMatchmaking() {
		// TODO: Implement policy of checking rides
		while(true)
		{
			RideRequest ride = OracleDBHandler.getInstance().checkRideRequestStatus(this, 20000);

			// NULL means exception or timeout
			if (ride != null) {
				// 33 means something happened: logout or deactivated
				if (ride.getFindStatus() != 33) {
					return ride;
				} else {
					return null;
				}
			}
		} // end while
	}

	// Get Ride for Driver
	public Ride getRideForDriver(RideRequest rideRequest)
	{
		return getRide(rideRequest);
	}

	// Saving Driver Location
	public boolean saveDriverLocation()
	{
		return OracleDBHandler.getInstance().saveDriverLocation(this);
	}

	// Getting Driver Location
	public void getDriverLocation()
	{
		setCurrentLocation(OracleDBHandler.getInstance().getDriverLocation(this));
	}

	// Reset Driver
	public boolean resetDriver() {
		return OracleDBHandler.getInstance().resetDriver(this);
	}

	public ArrayList<Vehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(ArrayList<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}

	public boolean sendRegistrationRequest()
	{
		return OracleDBHandler.getInstance().sendRegistrationRequest(this);
	}

	public boolean sendVehicleRequest() {
		return OracleDBHandler.getInstance().sendVehicleRequest(this);
	}
	public boolean respondToVehicleRequest() {
		return OracleDBHandler.getInstance().respondToVehicleRequest(this);
	}
}
