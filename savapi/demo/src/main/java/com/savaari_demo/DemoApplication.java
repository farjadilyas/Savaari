
package com.savaari_demo;
import com.savaari_demo.controllers.CRUDController;
import com.savaari_demo.controllers.LocationController;
import com.savaari_demo.controllers.MatchmakingController;

import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@SpringBootApplication
@RestController
public class DemoApplication
{
	// Main Attributes
	private static MatchmakingController matchmakingController;
	private static CRUDController crudController;
	private static LocationController locationController;

	// MAIN METHOD
	public static void main(String[] args) 
	{
		matchmakingController = new MatchmakingController();
		crudController = new CRUDController();
		locationController = new LocationController();

		SpringApplication.run(DemoApplication.class, args);
	}

	// API REQUESTS

	/* Add new user methods */
	@RequestMapping(value = "/add_rider", method = RequestMethod.POST)
	public String addRider(@RequestBody Map<String, String> allParams)
	{
		String username = allParams.get("username");
		String email_address = allParams.get("email_address");
		String password = allParams.get("password");

		return crudController.addRider(username, email_address, password).toString();
	}

	@RequestMapping(value = "/add_driver", method = RequestMethod.POST)
	public String addDriver(@RequestBody Map<String, Object> allParams)
	{
		System.out.println(allParams.toString());
		String username = (String) allParams.get("username");
		String email_address = (String) allParams.get("email_address");
		String password = (String) allParams.get("password");

		return crudController.addDriver(username, email_address, password).toString();
	}
	/* End of section */


	/* Authenticate user methods */
	@RequestMapping(value = "/login_rider", method = RequestMethod.POST)
	public String loginRider(@RequestBody Map<String, String> allParams)
	{
		String email_address = allParams.get("username");
		String password = allParams.get("password");

		System.out.println("LOGIN QUERY: " + email_address + " " + password);

		return crudController.loginRider(email_address, password).toString();
	}

	@RequestMapping(value = "/login_driver", method = RequestMethod.POST)
	public String loginDriver(@RequestBody Map<String, String> allParams)
	{
		String email_address = allParams.get("username");
		String password = allParams.get("password");

		return crudController.loginDriver(email_address, password).toString();
	}
	/* End of section*/


	/* Fetch user data methods
	@GetMapping("/rider_details")
	public String riderDetails()
	{
		System.out.println("Rider deets called");
		return crudController.riderDetails().toString();
	}

	@GetMapping("/driver_details")
	public String driverDetails()
	{
		return crudController.driverDetails().toString();
	}*/

	@RequestMapping(value = "/rider_data", method = RequestMethod.POST)
	public String riderData(@RequestBody Map<String, String> allParams)
	{
		String userID = allParams.get("USER_ID");
		return crudController.riderData(userID).toString();
	}

	@RequestMapping(value = "/driver_data", method = RequestMethod.POST)
	public String driverData(@RequestBody Map<String, String> allParams)
	{
		String userID = allParams.get("USER_ID");
		return crudController.driverData(userID).toString();
	}
	/* End of section */


	//TODO: deleteRider
	//TODO: deleteDriver


	/* Rider-side matchmaking method*/
	@RequestMapping(value = "/findDriver", method = RequestMethod.POST)
	public String findDriver(@RequestBody Map<String, String> allParams)
	{
		String userID = allParams.get("USER_ID"),
				sourceLatitude = allParams.get("SOURCE_LAT"),
				sourceLongitude = allParams.get("SOURCE_LONG"),
				destinationLatitude = allParams.get("DEST_LAT"),
				destinationLongitude = allParams.get("DEST_LONG");

		return matchmakingController.findDriver(userID, sourceLatitude, sourceLongitude,
				destinationLatitude, destinationLongitude).toString();
	}
	/* End of section*/


	/* Driver-side matchmaking methods */
	@RequestMapping(value = "/setMarkActive", method = RequestMethod.POST)
	public String setMarkActive(@RequestBody Map<String, String> allParams)
	{
		String userID = allParams.get("USER_ID");
		String activeStatus = allParams.get("ACTIVE_STATUS");
		return matchmakingController.setMarkActive(userID, activeStatus).toString();
	}

	@RequestMapping(value = "/checkRideStatus", method = RequestMethod.POST)
	public String checkRideStatus(@RequestBody Map<String, String> allParams)
	{
		return matchmakingController.getRideForDriver(allParams.get("USER_ID")).toString();
	}

	@RequestMapping(value = "/confirmRideRequest", method = RequestMethod.POST)
	public String confirmRideRequest(@RequestBody Map<String, String> allParams)
	{
		String userID = allParams.get("USER_ID");
		String found_status = allParams.get("FOUND_STATUS");
		String riderID = allParams.get("RIDER_ID");

		return matchmakingController.confirmRideRequest(userID, found_status, riderID).toString();
	}
	@RequestMapping(value = "/markArrival", method = RequestMethod.POST)
	public String markDriverArrival(@RequestBody Map<String, String> allParams)
	{
		return matchmakingController.markDriverArrival(allParams.get("RIDE_ID")).toString();
	}

	@RequestMapping(value = "/startRideDriver", method = RequestMethod.POST)
	public String startRideDriver(@RequestBody Map<String, String> allParams)
	{
		return matchmakingController.startRideDriver(allParams.get("RIDE_ID")).toString();
	}

	@RequestMapping(value = "/endRideDriver", method = RequestMethod.POST)
	public String endRideDriver(@RequestBody Map<String, String> allParams)
	{
		return matchmakingController.endRideDriver(allParams.get("RIDE_ID")).toString();
	}

	/* End of section */


	/* Ride system calls */

	@RequestMapping(value = "/getRideForRider", method = RequestMethod.POST)
	public String getRideForRider(@RequestBody Map<String, String> allParams)
	{
		String riderID = allParams.get("USER_ID");
		return matchmakingController.getRideForRider(riderID).toString();
	}

	@RequestMapping(value = "/getRideStatus", method = RequestMethod.POST)
	public String getRideStatus(@RequestBody Map<String, String> allParams)
	{
		String rideID = allParams.get("RIDE_ID");
		return matchmakingController.getRideStatus(rideID).toString();
	}

	/* End of section */

	/* Location update methods*/
	@RequestMapping(value = "/saveDriverLocation", method = RequestMethod.POST)
	public String saveDriverLocation(@RequestBody Map<String, String> allParams)
	{
		String userID = allParams.get("USER_ID"),
				latitude = allParams.get("LATITUDE"),
				longitude = allParams.get("LONGITUDE"),
				timestamp = allParams.get("TIMESTAMP");

		boolean locationSaved = locationController.saveDriverLocations(userID, latitude, longitude, timestamp);

		JSONObject result = new JSONObject();
		result.put("STATUS", ((locationSaved)? 200 : 404));
		return result.toString();
	}

	@RequestMapping(value = "/saveRiderLocation", method = RequestMethod.POST)
	public String saveRiderLocation(@RequestBody Map<String, String> allParams)
	{
		String userID = allParams.get("USER_ID"),
				latitude = allParams.get("LATITUDE"),
				longitude = allParams.get("LONGITUDE"),
				timestamp = allParams.get("TIMESTAMP");

		boolean locationSaved = locationController.saveRiderLocations(userID, latitude, longitude, timestamp);

		JSONObject result = new JSONObject();
		result.put("STATUS", ((locationSaved)? 200 : 404));
		return result.toString();
	}

	@RequestMapping(value = "/getDriverLocations", method = RequestMethod.POST)
	public String getDriverLocations(@RequestBody Map<String, String> allParams)
	{
		return locationController.getDriverLocations().toString();
	}

	@RequestMapping(value = "/getRiderLocations", method = RequestMethod.POST)
	public String getRiderLocations(@RequestBody Map<String, String> allParams)
	{
		return locationController.getRiderLocations().toString();
	}

	@RequestMapping(value = "/getDriverLocation", method = RequestMethod.POST)
	public String getDriverLocation(@RequestBody Map<String, String> allParams)
	{
		return locationController.getDriverLocation(allParams.get("USER_ID")).toString();
	}

	@RequestMapping(value = "/getRiderLocation", method = RequestMethod.POST)
	public String getRiderLocation(@RequestBody Map<String, String> allParams)
	{
		return locationController.getRiderLocation(allParams.get("USER_ID")).toString();
	}
	/* End of section */


	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}

}
