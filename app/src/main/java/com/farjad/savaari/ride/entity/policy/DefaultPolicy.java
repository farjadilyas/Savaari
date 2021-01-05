package com.farjad.savaari.ride.entity.policy;
import com.farjad.savaari.ride.entity.Ride;
import com.farjad.savaari.ride.entity.RideType;

public class DefaultPolicy implements Policy {

    // Main Attributes
    private static DefaultPolicy instance = null;

    // Main Methods
    private DefaultPolicy() {
    }

    public synchronized static Policy newInstance() {
        if (instance == null) {
            instance = new DefaultPolicy();
        }
        return instance;
    }

    // Functions

    @Override
    public void calculateFare(Ride ride) {
        RideType rideType = null;

        // Fetch & assign correct ride type or assume list is in order
        for (RideType currentRideType : rideTypes) {
            if (currentRideType.getTypeID() == ride.getRideParameters().getRideType().getTypeID()) {
                rideType = currentRideType;
                break;
            }
        }

        if (rideType == null) {
            return;
        }

        double calculatedFare = rideType.getBaseFare()
                + rideType.getPerKMCharge()*ride.getDistanceTravelled()
                + ((ride.getRideDuration()/1000.0)/60.0)*rideType.getPerMinuteCharge();
    }

    @Override
    public void calculateEstimatedFare(Ride ride) {
        ride.setEstimatedFare(200);
    }

    @Override
    public int getPolicyID() {
        return 1;
    }
}
