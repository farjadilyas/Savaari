/**
 * 
 */
package com.savaari_demo.entity;

import com.savaari_demo.DBHandler;
import org.json.JSONObject;

public class Rider extends User
{
	// Main Attributes
    int findStatus;

    // Main Methods
    public int getFindStatus() {
        return findStatus;
    }

    public void setFindStatus(int findStatus) {
        this.findStatus = findStatus;
    }


    // Methods for system interactions

    public JSONObject getRideForRider(DBHandler dbHandler) {
        Ride ride = dbHandler.checkRideRequestStatus(this);

        JSONObject result = new JSONObject();

        if (ride == null) {
            result.put("STATUS_CODE", 404);
            result.put("IS_TAKING_RIDE", false);
        }
        else if (ride.getRider().getFindStatus() != 2) {
            result.put("STATUS_CODE", 200);
            result.put("IS_TAKING_RIDE", false);
        }
        else {
            result = getRide(ride);
        }

        return result;
    }
}
