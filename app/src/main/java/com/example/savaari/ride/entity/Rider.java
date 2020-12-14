package com.example.savaari.ride.entity;

public class Rider extends User {

    // Main Attributes
    int findStatus;


    public Rider() {
        findStatus = -1;
    }

    // Main Methods
    public int getFindStatus() {
        return findStatus;
    }

    public void setFindStatus(int findStatus) {
        this.findStatus = findStatus;
    }

}
