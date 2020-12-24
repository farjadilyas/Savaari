package com.savaari_demo.entity;

import com.savaari_demo.DBHandlerFactory;

public class User {
    public static final int DEFAULT_ID = -1;

    private int userID;
    private String username;
    private String password;
    private String emailAddress;
    private String firstName;
    private String lastName;
    private String phoneNo;
    private Location currentLocation;
    private float rating;

    User() {
        userID = User.DEFAULT_ID;
    }

    public int getUserID() {
        return userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }


    // Methods for system interactions

    public Ride getRide(RideRequest rideRequest) {

        return DBHandlerFactory.getInstance().createDBHandler().getRide(rideRequest);
        //result.put("IS_TAKING_RIDE", (result.getInt("STATUS_CODE") == 200));
    }
}
