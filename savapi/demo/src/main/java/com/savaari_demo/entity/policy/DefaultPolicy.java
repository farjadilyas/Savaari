package com.savaari_demo.entity.policy;

import com.savaari_demo.entity.Ride;

public class DefaultPolicy implements Policy {

    // Main Attributes
    private static DefaultPolicy instance = null;

    // Main Methods
    private DefaultPolicy() {
    }

    public synchronized static Policy getInstance() {
        if (instance == null) {
            instance = new DefaultPolicy();
        }
        return instance;
    }

    // Functions

    @Override
    public void calculateFare(Ride ride) {
        System.out.println("Dist travelled: " + (ride.getDistanceTravelled()/1000));
        System.out.println("Ride duration: " +((ride.getRideDuration()/1000.0)/60.0));
        System.out.println("Start time: " + ride.getStartTime());
        System.out.println("End time: " + ride.getEndTime());

        double calculatedFare = Math.max(ride.getRideType().getBaseFare()
                + ride.getRideType().getPerKMCharge()*(ride.getDistanceTravelled()/1000)
                + ((ride.getRideDuration()/1000.0)/60.0)*ride.getRideType().getPerMinuteCharge(),
                ride.getRideType().getMinimumFare());

        ride.setFare(calculatedFare);
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
