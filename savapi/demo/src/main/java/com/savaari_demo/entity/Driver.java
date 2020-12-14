package com.savaari_demo.entity;

import com.savaari_demo.OracleDBHandler;
import org.json.JSONObject;

public class Driver extends User
{
	// Main Attributes
	private static final String LOG_TAG = Driver.class.getSimpleName();
	Boolean isActive;
	Boolean isTakingRide;
	int rideRequestStatus;

	// Main Constructors
	public Driver() {
		super();
	}
	public Driver(Location currentLocation, Boolean isActive, Boolean isTakingRide) {
		super();
		setCurrentLocation(currentLocation);
		setIsActive(isActive);
		setIsTakingRide(isTakingRide);
	}
	
	// Getters and Setters
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public Boolean getIsTakingRide() {
		return isTakingRide;
	}
	public void setIsTakingRide(Boolean isTakingRide) {
		this.isTakingRide = isTakingRide;
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
	public JSONObject getDriverLocation()
	{
		setCurrentLocation(OracleDBHandler.getInstance().getDriverLocation(this));
		JSONObject result = new JSONObject();

		if (getCurrentLocation() == null) {
			result.put("STATUS_CODE", 404);
		}
		else {
			result.put("STATUS_CODE", 200);
			result.put("LATITUDE", getCurrentLocation().getLatitude());
			result.put("LONGITUDE", getCurrentLocation().getLongitude());
		}
		return result;
	}

	// Reset Driver
	public boolean resetDriver() {
		return OracleDBHandler.getInstance().resetDriver(this);
	}
}
