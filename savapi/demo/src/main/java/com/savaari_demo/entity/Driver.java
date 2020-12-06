package com.savaari_demo.entity;

import org.json.JSONObject;
import com.savaari_demo.DBHandler;

public class Driver extends User
{
	// Main Attributes
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
	public JSONObject setMarkActive(DBHandler dbHandler)
	{
		if (dbHandler.markDriverActive(driver))
		{
			JSONObject json = checkRideRequestStatus(driver);
			if (json == null) {
				json = new JSONObject();
				json.put("STATUS", 404);
			}
			return json;
		}
		else
		{
			JSONObject json = new JSONObject();
			json.put("STATUS", 404);
			return json;
		}
	}

}
