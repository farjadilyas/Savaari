package com.savaari_demo.entity;

import com.savaari_demo.OracleDBHandler;

public class Driver extends User
{
	// Main Attributes
	private static final String LOG_TAG = Driver.class.getSimpleName();
	Boolean active;
	Boolean takingRide;
	int rideRequestStatus;

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
}
