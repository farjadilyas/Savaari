package com.savaari_demo.entity;

import com.savaari_demo.DBHandler;
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
	public Driver(Location lastLocation, Boolean isActive, Boolean isTakingRide) {
		super();
		this.lastLocation = lastLocation;
		this.isActive = isActive;
		this.isTakingRide = isTakingRide;
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
	public JSONObject signup(DBHandler dbHandler)
	{
		// TODO Add logic of if Driver exists already
		JSONObject result = new JSONObject();
		if (dbHandler.addDriver(this)) {
			result.put("STATUS_CODE", 200);
		}
		else {
			result.put("STATUS_CODE", 404);
		}

		return result;
	}
	public JSONObject login(DBHandler dbHandler)
	{
		JSONObject result = new JSONObject();
		Integer userID = dbHandler.loginDriver(this);

		if (userID == null) {
			result.put("STATUS_CODE", 404);
			result.put("USER_ID", -1);
		} else {
			result.put("STATUS_CODE", 200);
			result.put("USER_ID", userID);
		}

		return result;
	}
	public boolean fetchData(DBHandler dbHandler)
	{
		return dbHandler.fetchDriverData(this);
	}
	public boolean setMarkActive(DBHandler dbHandler)
	{
		return dbHandler.markDriverActive(this);
	}
	public JSONObject checkRideRequestStatus(DBHandler dbHandler)
	{
		// TODO: Implement policy of checking rides
		while(true)
		{
			Ride ride = dbHandler.checkRideRequestStatus(this);
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
	public JSONObject confirmRideRequest(DBHandler dbHandler, Rider rider, int found_status)
	{
		Ride ride = new Ride();
		ride.setDriver(this);
		ride.setRider(rider);
		ride.setRideStatus(found_status == 1 ? 2 : 0);
		ride.setFindStatus(found_status + 1);

		JSONObject jsonObject = new JSONObject();
		if (dbHandler.confirmRideRequest(ride)) {
			jsonObject.put("STATUS", 200);

			Payment newPayment = dbHandler.addPayment();

			if (newPayment == null) {
				System.out.println(LOG_TAG + ":recordRide: payment is null");
			}
			else {
				// Payment created, set it in ride & record ride
				ride.setPayment(newPayment);
				dbHandler.recordRide(ride);
			}
		} else {
			jsonObject.put("STATUS", 404);
		}
		return jsonObject;
	}

	public JSONObject getRideForDriver(DBHandler dbHandler)
	{
		Ride ride = dbHandler.checkRideRequestStatus(this);
		JSONObject result = new JSONObject();

		if (ride == null) {
			result.put("STATUS_CODE", 404);
			result.put("IS_TAKING_RIDE", false);
		}
		else {
			ride.setDriver(this);
			result = dbHandler.getRide(ride);
			result.put("IS_TAKING_RIDE", (result.getInt("STATUS_CODE") == 200));
		}

		return result;
	}
	public boolean saveDriverLocation(DBHandler dbHandler)
	{
		return dbHandler.saveDriverLocation(this);
	}
	public JSONObject getDriverLocation(DBHandler dbHandler)
	{
		lastLocation = dbHandler.getDriverLocation(this);
		JSONObject result = new JSONObject();

		if (lastLocation == null) {
			result.put("STATUS_CODE", 404);
		}
		else {
			result.put("STATUS_CODE", 200);
			result.put("LATITUDE", lastLocation.getLatitude());
			result.put("LONGITUDE", lastLocation.getLongitude());
		}
		return result;
	}

	public JSONObject resetDriver(DBHandler dbHandler) {
		return dbHandler.resetDriver(this);
	}
}
