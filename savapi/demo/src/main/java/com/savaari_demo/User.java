package com.savaari_demo;

public class User 
{
    // Main Attributes
	private Integer userID;
	private String username;
	private String password;
	private String emailAddress;
	private String firstName;
	private String lastName;
	private String phoneNo;
	Location lastLocation;
	
	// Main Constructors
	public User() {
		super();
		userID = 0;
	}
	
	// Parameterized Constructor
	public User(Integer userID, String username, String password, String emailAddress, String firstName,
			String lastName, String phoneNo, Location lastLocation) {
		super();
		this.userID = userID;
		this.username = username;
		this.password = password;
		this.emailAddress = emailAddress;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNo = phoneNo;
		this.lastLocation = lastLocation;
	}
	
	// Getters and Setters
	public Integer getUserID() {
		return userID;
	}
	public void setUserID(Integer userID) {
		this.userID = userID;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public Location getLastLocation() {
		return lastLocation;
	}
	public void setLastLocation(Location lastLocation) {
		this.lastLocation = lastLocation;
	}
}
