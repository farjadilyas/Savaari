
package com.savaari_demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.savaari_demo.controllers.CRUDController;
import com.savaari_demo.controllers.LocationController;
import com.savaari_demo.controllers.MatchmakingController;
import com.savaari_demo.entity.Driver;
import com.savaari_demo.entity.Location;
import com.savaari_demo.entity.Ride;
import com.savaari_demo.entity.Rider;

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

	private static ObjectMapper objectMapper;

	// MAIN METHOD
	public static void main(String[] args)
	{
		/*
		Rider rider = new Rider();
		rider.setUserID(3);
		rider.setEmailAddress("ilyasfarjad@gmail.com");
		rider.setLastLocation(new Location(3.,3.,null));

		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = "fail";

		try {
			jsonString = objectMapper.writeValueAsString(rider);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		System.out.println("Output: " + jsonString);

		try {
			rider = objectMapper.readValue(jsonString, Rider.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		System.out.println("Rider ID: " + rider.getUserID()); */

		objectMapper = new ObjectMapper();
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
		System.out.println(allParams.toString());
		String username = allParams.get("username");
		String email_address = allParams.get("email_address");
		String password = allParams.get("password");

		JSONObject result = new JSONObject();

		if (crudController.addRider(username, email_address, password)) {
			result.put("STATUS_CODE", 200);
		}
		else {
			result.put("STATUS_CODE", 404);
		}
		return result.toString();
	}

	@RequestMapping(value = "/add_driver", method = RequestMethod.POST)
	public String addDriver(@RequestBody Map<String, Object> allParams)
	{
		System.out.println(allParams.toString());

		String username = (String) allParams.get("username");
		String email_address = (String) allParams.get("email_address");
		String password = (String) allParams.get("password");
		JSONObject result = new JSONObject();
		if (crudController.addDriver(username, email_address, password)) {
			result.put("STATUS_CODE", 200);
		}
		else {
			result.put("STATUS_CODE", 404);
		}

		return result.toString();
	}
	/* End of section */

	/* Authenticate user methods */
	@RequestMapping(value = "/login_rider", method = RequestMethod.POST)
	public String loginRider(@RequestBody Map<String, String> allParams)
	{
		Rider rider = new Rider();
		rider.setEmailAddress(allParams.get("username"));
		rider.setPassword(allParams.get("password"));

		Integer userID = crudController.loginRider(rider);

		// Package response
		JSONObject result = new JSONObject();

		if (userID == null) {
			result.put("STATUS_CODE", 404);
			result.put("USER_ID", -1);
		}
		else {
			result.put("STATUS_CODE", 200);
			result.put("USER_ID", userID);
		}

		return result.toString();
	}

	@RequestMapping(value = "/login_driver", method = RequestMethod.POST)
	public String loginDriver(@RequestBody Map<String, String> allParams)
	{
		Driver driver = new Driver();
		driver.setEmailAddress(allParams.get("username"));
		driver.setPassword(allParams.get("password"));

		return crudController.loginDriver(driver).toString();
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
	} */

	@RequestMapping(value = "/rider_data", method = RequestMethod.POST)
	public String riderData(@RequestBody Map<String, String> allParams)
	{
		Rider rider = new Rider();
		rider.setUserID(Integer.parseInt(allParams.get("USER_ID")));
		String result = null;

		if (crudController.riderData(rider)) {
			try {
				result = objectMapper.writeValueAsString(rider);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return null;
			}
		}

		return result;
	}

	@RequestMapping(value = "/driver_data", method = RequestMethod.POST)
	public String driverData(@RequestBody Map<String, String> allParams)
	{
		Driver driver = new Driver();
		driver.setUserID(Integer.parseInt(allParams.get("USER_ID")));

		String result = null;

		if (crudController.driverData(driver)) {
			try {
				result = objectMapper.writeValueAsString(driver);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		return result;
	}
	/* End of section */

	// TODO: deleteRider
	// TODO: deleteDriver

	/* Rider-side matchmaking method*/
	@RequestMapping(value = "/findDriver", method = RequestMethod.POST)
	public String findDriver(@RequestBody Map<String, String> allParams)
	{
		Rider rider = new Rider();
		rider.setUserID(Integer.parseInt(allParams.get("USER_ID")));

		Ride fetchedRide = matchmakingController.findDriver(rider,
				new Location(Double.parseDouble(allParams.get("SOURCE_LAT")),
						Double.parseDouble(allParams.get("SOURCE_LONG")), null),
				new Location(Double.parseDouble(allParams.get("DEST_LAT")),
						Double.parseDouble(allParams.get("DEST_LONG")), null));

		String result = null;

		if (fetchedRide != null) {
			try {
				result = objectMapper.writeValueAsString(fetchedRide);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		return result;
	}
	/* End of section*/


	/* Driver-side matchmaking methods */
	@RequestMapping(value = "/setMarkActive", method = RequestMethod.POST)
	public String setMarkActive(@RequestBody Map<String, String> allParams)
	{
		Driver driver = new Driver();
		driver.setUserID(Integer.parseInt(allParams.get("USER_ID")));
		driver.setIsActive(Boolean.valueOf(allParams.get("ACTIVE_STATUS")));

		JSONObject json = new JSONObject();

		if (matchmakingController.setMarkActive(driver)) {
			json.put("STATUS", 200);
		}
		else {
			json.put("STATUS", 404);
		}
		return json.toString();
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

	@RequestMapping(value = "/markArrivalAtDestination", method = RequestMethod.POST)
	public String markArrivalAtDestination(@RequestBody Map<String, String> allParams)
	{
		System.out.println("MARK ARRIVAL CALLED");
		return matchmakingController.markArrivalAtDestination(allParams.get("RIDE_ID"), allParams.get("DIST_TRAVELLED"), allParams.get("DRIVER_ID")).toString();
	}

	@RequestMapping(value = "/endRideWithPayment", method = RequestMethod.POST)
	public String endRideWithPayment(@RequestBody Map<String, String> allParams)
	{
		return matchmakingController.endRideWithPayment(allParams.get("RIDE_ID"), allParams.get("AMNT_PAID"), allParams.get("DRIVER_ID")).toString();
	}

	/* End of section */


	/* Ride system calls */

	@RequestMapping(value = "/getRideForRider", method = RequestMethod.POST)
	public String getRideForRider(@RequestBody Map<String, String> allParams)
	{
		Rider rider = new Rider();
		rider.setUserID(Integer.parseInt(allParams.get("USER_ID")));

		Ride fetchedRide = matchmakingController.getRideForRider(rider);

		String result = null;

		if (fetchedRide != null) {
			try {
				result = objectMapper.writeValueAsString(fetchedRide);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		return result;
	}

	@RequestMapping(value = "/getRideStatus", method = RequestMethod.POST)
	public String getRideStatus(@RequestBody Map<String, String> allParams)
	{
		Ride ride = new Ride();
		ride.setRideID(Integer.parseInt(allParams.get("RIDE_ID")));

		int rideStatus = matchmakingController.getRideStatus(ride);

		JSONObject result = null;

		if (rideStatus != Ride.RS_DEFAULT) {
			result = new JSONObject();
			result.put("RIDE_STATUS", rideStatus);
		}

		return result.toString();
	}

	@RequestMapping(value = "/acknowledgeEndOfRide", method = RequestMethod.POST)
	public String acknowledgeEndOfRide(@RequestBody Map<String, String> allParams)
	{
		Ride ride = new Ride();
		ride.setRideID(Integer.parseInt(allParams.get("RIDE_ID")));
		ride.setRider(new Rider());
		ride.getRider().setUserID(Integer.parseInt(allParams.get("RIDER_ID")));

		JSONObject result = new JSONObject();
		result.put("STATUS_CODE", ((matchmakingController.acknowledgeEndOfRide(ride))? 200 : 404));
		return result.toString();
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
		Rider rider = new Rider();
		rider.setUserID(Integer.parseInt(allParams.get("USER_ID")));
		rider.setCurrentLocation(new Location(Double.valueOf(allParams.get("LATITUDE")), Double.valueOf(allParams.get("LONGITUDE")),
				Long.parseLong(allParams.get("TIMESTAMP"))));

		boolean locationSaved = locationController.saveRiderLocation(rider);

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
		Rider rider = new Rider();
		rider.setUserID(Integer.parseInt(allParams.get("USER_ID")));

		locationController.getRiderLocation(rider);

		JSONObject result = new JSONObject();

		if (rider.getCurrentLocation() == null) {
			result.put("STATUS_CODE", 404);
		}
		else {
			result.put("STATUS_CODE", 200);
			result.put("LATITUDE", rider.getCurrentLocation().getLatitude());
			result.put("LONGITUDE", rider.getCurrentLocation().getLongitude());
		}
		return result.toString();
	}
	/* End of section */


	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}

}
