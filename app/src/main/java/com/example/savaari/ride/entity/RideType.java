package com.example.savaari.ride.entity;

public class RideType {
    private int typeID = 1;
    private String name = "Smol";
    private int maxPassengers = 4;
    private double baseFare = 17.71;
    private double perKMCharge = 12.13;
    private double perMinuteCharge = 3.61;
    private double minimumFare = 71;

    public RideType(int typeID, String name, int maxPassengers, double baseFare, double perKMCharge,
             double perMinuteCharge, double minimumFare) {
        this.typeID = typeID;
        this.name = name;
        this.maxPassengers = maxPassengers;
        this.baseFare = baseFare;
        this.perKMCharge = perKMCharge;
        this.perMinuteCharge = perMinuteCharge;
        this.minimumFare = minimumFare;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxPassengers() {
        return maxPassengers;
    }

    public void setMaxPassengers(int maxPassengers) {
        this.maxPassengers = maxPassengers;
    }

    public double getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(double baseFare) {
        this.baseFare = baseFare;
    }

    public double getPerKMCharge() {
        return perKMCharge;
    }

    public void setPerKMCharge(double perKMCharge) {
        this.perKMCharge = perKMCharge;
    }

    public double getPerMinuteCharge() {
        return perMinuteCharge;
    }

    public void setPerMinuteCharge(double perMinuteCharge) {
        this.perMinuteCharge = perMinuteCharge;
    }

    public double getMinimumFare() {
        return minimumFare;
    }

    public void setMinimumFare(double minimumFare) {
        this.minimumFare = minimumFare;
    }
}
