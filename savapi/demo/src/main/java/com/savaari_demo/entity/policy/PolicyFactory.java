package com.savaari_demo.entity.policy;

import com.savaari_demo.entity.Ride;

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

    public Policy determinePolicy(Ride ride) {
        if (ride.getRideParameters().isSplittingFare()) {
            return DefaultPolicy.getInstance();
        }
        else {
            //return FareSplitPolicy.newInstance();
            return DefaultPolicy.getInstance();
        }
    }

    public Policy determinePolicy(int policyID) {
        if (policyID == 1) {
            return DefaultPolicy.getInstance();
        }
        else {
            //return FareSplitPolicy.newInstance();
            return DefaultPolicy.getInstance();
        }
    }
}
