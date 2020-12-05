package com.savaari_demo.entity;

import java.sql.Timestamp;

public class Location 
{
	// Main Attributes
	Double latitude;
	Double longitude;
	Timestamp timestamp;
	
	// Main Constructors
	public Location() {
		super();
	}
	public Location(Double latitude, Double longitude, Timestamp timestamp) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.timestamp = timestamp;
	}
	
	// Getters and Setters
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
}
