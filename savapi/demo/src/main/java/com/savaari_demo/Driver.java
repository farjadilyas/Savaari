package com.savaari_demo;

public class Driver extends User 
{
	// Main Attributes
	Boolean isActive;
	Boolean isTakingRide;
	
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
}
