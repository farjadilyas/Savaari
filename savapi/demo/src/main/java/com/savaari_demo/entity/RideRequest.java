package com.savaari_demo.entity;

public class RideRequest {

    public static final int
    MS_DEFAULT = 0,
    MS_REQ_RECEIVED = 1,
    MS_REQ_ACCEPTED = 2;

    // Match status - used in business logic
    public static final int
            NOT_SENT = -1,
            NO_CHANGE = 0,
            REJECTED = 1,
            FOUND = 2;

    // Match return status  - returned to clients
    public static final int
            DEFAULT = 50,
            PAIRED = 51,
            ALREADY_PAIRED = 52,
            NOT_PAIRED = 53,
            NOT_FOUND = 54,
            STATUS_ERROR = 55;

    protected Rider rider;
    protected Driver driver;
    protected Location pickupLocation;
    private String pickupTitle;
    protected Location dropoffLocation;
    protected String dropoffTitle;
    protected int findStatus;
    private Integer paymentMethod;
    private int rideType;

    public RideRequest() {
        rider = new Rider();
        driver = new Driver();
    }

    public Rider getRider() {
        return rider;
    }

    public void setRider(Rider rider) {
        this.rider = rider;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Location getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(Location pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public Location getDropoffLocation() {
        return dropoffLocation;
    }

    public void setDropoffLocation(Location dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
    }

    public String getPickupTitle() {
        return pickupTitle;
    }

    public void setPickupTitle(String pickupTitle) {
        this.pickupTitle = pickupTitle;
    }

    public String getDropoffTitle() {
        return dropoffTitle;
    }

    public void setDropoffTitle(String dropoffTitle) {
        this.dropoffTitle = dropoffTitle;
    }

    public int getFindStatus() {
        return findStatus;
    }

    public void setFindStatus(int findStatus) {
        this.findStatus = findStatus;
    }

    public void setPaymentMethod(Integer paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Integer getPaymentMethod() {
        return paymentMethod;
    }

    public int getRideType() {
        return rideType;
    }

    public void setRideType(int rideType) {
        this.rideType = rideType;
    }
}
