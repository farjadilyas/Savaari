package com.savaari_demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.savaari_demo.database.DBHandlerFactory;

public class Administrator {
    public static final int DEFAULT_ID = -1;

    private int userID;
    private String emailAddress;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNo;
    @JsonProperty("CNIC")
    private String CNIC;
    private int credentials;

    public Administrator() {
        userID = DEFAULT_ID;
    }

    public Administrator(int userID) {
        setUserID(userID);
    }

    public Administrator(int userID, String emailAddress, String password, String firstName, String lastName,
                         String phoneNo, String CNIC, int credentials) {
        this.userID = userID;
        this.emailAddress = emailAddress;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNo = phoneNo;
        this.CNIC = CNIC;
        this.credentials = credentials;
    }

    public void Initialize(int userID, String emailAddress, String password, String firstName, String lastName,
                      String phoneNo, String CNIC, int credentials) {
        setUserID(userID);
        setEmailAddress(emailAddress);
        setPassword(password);
        setFirstName(firstName);
        setLastName(lastName);
        setPassword(password);
        setCNIC(CNIC);
        setCredentials(credentials);
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        if (userID > 0) {
            this.userID = userID;
        }
        else {
            this.userID = DEFAULT_ID;
        }
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

    public String getCNIC() {
        return CNIC;
    }

    public void setCNIC(String CNIC) {
        this.CNIC = CNIC;
    }

    public int getCredentials() {
        return credentials;
    }

    public void setCredentials(int credentials) {
        this.credentials = credentials;
    }

    public boolean login() {
        return DBHandlerFactory.getInstance().createDBHandler().loginAdmin(this);
    }
}
