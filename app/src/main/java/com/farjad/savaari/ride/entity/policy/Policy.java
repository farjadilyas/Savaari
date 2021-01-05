package com.farjad.savaari.ride.entity.policy;

import com.farjad.savaari.ride.entity.Ride;
import com.farjad.savaari.ride.entity.RideType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DefaultPolicy.class, name = "DefaultPolicy")
}
)
public interface Policy {
    // Main Attributes
    ArrayList<RideType> rideTypes = new ArrayList<>();

    // Abstract methods
    void calculateFare(Ride ride);
    void calculateEstimatedFare(Ride ride);
    int getPolicyID();
}