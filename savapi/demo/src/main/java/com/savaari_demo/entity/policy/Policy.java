package com.savaari_demo.entity.policy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.savaari_demo.entity.Ride;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DefaultPolicy.class, name = "DefaultPolicy")
    }
)

public interface Policy {
    // Abstract methods
    double calculateFare(Ride ride);
    void calculateEstimatedFare(Ride ride);
    int getPolicyID();
}