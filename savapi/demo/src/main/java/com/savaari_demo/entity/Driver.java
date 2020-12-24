package com.savaari_demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.savaari_demo.DBHandlerFactory;

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

	@JsonProperty("CNIC")
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
		return DBHandlerFactory.getInstance().createDBHandler().addDriver(this);
	}

	// Login
	public Integer login()
	{
		return DBHandlerFactory.getInstance().createDBHandler().loginDriver(this);
	}

	// Fetch Data
	public boolean fetchData()
	{
		return DBHandlerFactory.getInstance().createDBHandler().fetchDriverData(this);
	}

	// Set Mark Active
	public boolean setMarkActive()
	{
		return DBHandlerFactory.getInstance().createDBHandler().markDriverActive(this);
	}

	// Check Ride Request Status
	public RideRequest checkRideRequestStatus() {
		return DBHandlerFactory.getInstance().createDBHandler().checkRideRequestStatus(this, 20000);
	}

	// Start Matchmaking service in Server
	public RideRequest startMatchmaking() {
		// TODO: Implement policy of checking rides
		while(true)
		{
			RideRequest ride = DBHandlerFactory.getInstance().createDBHandler().checkRideRequestStatus(this, 20000);

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
		return DBHandlerFactory.getInstance().createDBHandler().saveDriverLocation(this);
	}

	// Getting Driver Location
	public void getDriverLocation()
	{
		setCurrentLocation(DBHandlerFactory.getInstance().createDBHandler().getDriverLocation(this));
	}

	// Reset Driver
	public boolean resetDriver() {
		return DBHandlerFactory.getInstance().createDBHandler().resetDriver(this);
	}

	public ArrayList<Vehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(ArrayList<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}

	public boolean sendRegistrationRequest()
	{
		return DBHandlerFactory.getInstance().createDBHandler().sendRegistrationRequest(this);
	}

	public boolean sendVehicleRequest() {
		return DBHandlerFactory.getInstance().createDBHandler().sendVehicleRequest(this);
	}
	public boolean respondToVehicleRequest() {
		return DBHandlerFactory.getInstance().createDBHandler().respondToVehicleRequest(this);
	}

	public boolean setActiveVehicle() {
		return DBHandlerFactory.getInstance().createDBHandler().setActiveVehicle(this);
	}
}
