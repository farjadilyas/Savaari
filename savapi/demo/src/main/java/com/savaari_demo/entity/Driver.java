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
	public RideRequest checkRideRequestStatus()
	{
		// TODO: Implement policy of checking rides
		while(true)
		{
			RideRequest ride = OracleDBHandler.getInstance().checkRideRequestStatus(this);
			if (ride != null)
			{
				return ride;
			}
			else
			{
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					System.out.println("checkRideStatus(): Exception thrown at sleep()");
					e.printStackTrace();
					return null;
				}
			}
		}
	}

	// Confirm Ride Request
	public boolean confirmRideRequest(RideRequest rideRequest)
	{
		Ride ride = new Ride();

		ride.setDriver(this);
		ride.setRider(rideRequest.getRider());
		ride.setRideStatus(rideRequest.getFindStatus() == 1 ? 2 : 0);
		ride.setFindStatus(rideRequest.getFindStatus() + 1);

		if (OracleDBHandler.getInstance().confirmRideRequest(ride)) {
			return ride.recordRide();
		}
		return false;
	}

	// Get Ride for Driver
	public Ride getRideForDriver(RideRequest rideRequest)
	{
		return OracleDBHandler.getInstance().getRide(rideRequest);
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
