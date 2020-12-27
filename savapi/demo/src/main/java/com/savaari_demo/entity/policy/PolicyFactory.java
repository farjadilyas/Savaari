package com.savaari_demo.entity.policy;

import com.savaari_demo.entity.RideRequest;

public class PolicyFactory {

    private static PolicyFactory instance = null;

    private PolicyFactory() {

    }

    public synchronized static PolicyFactory getInstance() {
        if (instance == null) {
            instance = new PolicyFactory();
        }
        return instance;
    }

    public Policy determinePolicy(RideRequest rideRequest) {
        if (rideRequest.isSplittingFare()) {
            return DefaultPolicy.newInstance();
        }
        else {
            //return FareSplitPolicy.newInstance();
            return DefaultPolicy.newInstance();
        }
    }

    public Policy determinePolicy(int policyID) {
        if (policyID == 1) {
            return DefaultPolicy.newInstance();
        }
        else {
            //return FareSplitPolicy.newInstance();
            return DefaultPolicy.newInstance();
        }
    }
}
