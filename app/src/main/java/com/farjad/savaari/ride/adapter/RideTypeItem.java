package com.farjad.savaari.ride.adapter;

public class RideTypeItem {

    private int rideTypeImage;
    private String rideTypeName,  rideTypePrice;

    public RideTypeItem(int rideTypeImage, String rideTypeName, String rideTypePrice) {
        this.rideTypeImage = rideTypeImage;
        this.rideTypeName = rideTypeName;
        this.rideTypePrice = rideTypePrice;
    }

    public int getRideTypeImage() {
        return rideTypeImage;
    }

    public void setRideTypeImage(int rideTypeImage) {
        this.rideTypeImage = rideTypeImage;
    }

    public String getRideTypeName() {
        return rideTypeName;
    }

    public void setRideTypeName(String rideTypeName) {
        this.rideTypeName = rideTypeName;
    }

    public String getRideTypePrice() {
        return rideTypePrice;
    }

    public void setRideTypePrice(String rideTypePrice) {
        this.rideTypePrice = rideTypePrice;
    }
}
