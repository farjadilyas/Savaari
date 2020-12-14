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
	public JSONObject checkRideRequestStatus()
	{
		// TODO: Implement policy of checking rides
		while(true)
		{
			Ride ride = OracleDBHandler.getInstance().checkRideRequestStatus(this);
			if (ride != null)
			{
				JSONObject jsonObject = new JSONObject();

				jsonObject.put("STATUS", 200);
				jsonObject.put("RIDER_NAME", ride.getRider().getUsername());
				jsonObject.put("RIDER_ID", ride.getRider().getUserID());
				jsonObject.put("SOURCE_LAT", ride.getPickupLocation().getLatitude());
				jsonObject.put("SOURCE_LONG", ride.getPickupLocation().getLongitude());
				jsonObject.put("DEST_LAT", ride.getDropoffLocation().getLatitude());
				jsonObject.put("DEST_LONG", ride.getDropoffLocation().getLongitude());

				return jsonObject;
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
	public JSONObject confirmRideRequest(Rider rider, int found_status)
	{
		Ride ride = new Ride();
		ride.setDriver(this);
		ride.setRider(rider);
		ride.setRideStatus(found_status == 1 ? 2 : 0);
		ride.setFindStatus(found_status + 1);

		JSONObject jsonObject = new JSONObject();
		if (OracleDBHandler.getInstance().confirmRideRequest(ride)) {

			if (ride.recordRide()) {
				jsonObject.put("STATUS", 200);
			}
			else {
				jsonObject.put("STATUS", 304);
			}

		} else {
			jsonObject.put("STATUS", 404);
		}
		return jsonObject;
	}

	// Get Ride for Driver
	public JSONObject getRideForDriver()
	{
		Ride ride = OracleDBHandler.getInstance().checkRideRequestStatus(this);
		JSONObject result = new JSONObject();

		if (ride == null) {
			result.put("STATUS_CODE", 404);
			result.put("IS_TAKING_RIDE", false);
		}
		else {
			ride.setDriver(this);
			result = OracleDBHandler.getInstance().getRide(ride);
			result.put("IS_TAKING_RIDE", (result.getInt("STATUS_CODE") == 200));
		}

		return result;
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
	public JSONObject resetDriver() {
		return OracleDBHandler.getInstance().resetDriver(this);
	}
}
