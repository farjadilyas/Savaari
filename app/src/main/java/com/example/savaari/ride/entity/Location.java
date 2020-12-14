package com.example.savaari.ride.entity;

import com.google.android.gms.maps.model.LatLng;

public class Location
{
	// Main Attributes
	Double latitude;
	Double longitude;
	Long timestamp;
	
	// Main Constructors
	public Location() {
		super();
	}

	public Location(LatLng latLng) {
		this.latitude = latLng.latitude;
		this.longitude = latLng.longitude;
		this.timestamp = null;
	}
	public Location(Double latitude, Double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.timestamp = null;
	}
	public Location(Double latitude, Double longitude, Long timestamp) {
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
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
}
