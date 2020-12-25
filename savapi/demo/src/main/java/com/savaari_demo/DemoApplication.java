
package com.savaari_demo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.savaari_demo.controllers.CRUDController;
import com.savaari_demo.controllers.LocationController;
import com.savaari_demo.controllers.MatchmakingController;
import com.savaari_demo.database.DBHandler;
import com.savaari_demo.entity.*;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
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

	private static HashMap<String, DBHandler> databaseHandlers;

	/* MAIN METHOD */
	public static void main(String[] args)
	{
		objectMapper = new ObjectMapper();
		objectMapper.setVisibility(objectMapper.getSerializationConfig().getDefaultVisibilityChecker()
				.withFieldVisibility(JsonAutoDetect.Visibility.ANY)
				.withGetterVisibility(JsonAutoDetect.Visibility.NONE)
				.withSetterVisibility(JsonAutoDetect.Visibility.NONE)
				.withCreatorVisibility(JsonAutoDetect.Visibility.NONE));

		matchmakingController = new MatchmakingController();
		crudController = new CRUDController();
		locationController = new LocationController();

		//databaseHandlers = new HashMap<>();

		SpringApplication.run(DemoApplication.class, args);
	}


	//* API REQUESTS

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
	public String loginRider(@RequestBody Map<String, String> allParams, HttpServletRequest request)
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

			if (request.getSession(false) == null) {
				request.getSession(true);
			}
		}

		return result.toString();
	}

	@RequestMapping(value = "/persistRiderLogin", method = RequestMethod.POST)
	public String persistRiderLogin(@RequestBody Map<String, String> allParams, HttpServletRequest request)
	{
		//TODO: if verified using token
		if (request.getSession(false) == null) {
			request.getSession(true);
		}

		return new JSONObject().put("STATUS_CODE", 200).toString();
	}

	@RequestMapping(value = "/login_driver", method = RequestMethod.POST)
	public String loginDriver(@RequestBody Map<String, String> allParams, HttpServletRequest request)
	{
		Driver driver = new Driver();
		driver.setEmailAddress(allParams.get("username"));
		driver.setPassword(allParams.get("password"));

		Integer userID = crudController.loginDriver(driver);

		// Package response
		JSONObject result = new JSONObject();

		if (userID == null) {
			result.put("STATUS_CODE", 404);
			result.put("USER_ID", -1);
		}
		else {
			result.put("STATUS_CODE", 200);
			result.put("USER_ID", userID);

			if (request.getSession(false) == null) {
				request.getSession(true);
			}
		}

		return result.toString();
	}

	@RequestMapping(value = "/registerDriver", method = RequestMethod.POST)
	public String registerDriver(@RequestBody Map<String, String> allParams, HttpServletRequest request)
	{
		if (request.getSession(false) == null) {
			return null;
		}
		Driver driver = new Driver();
		driver.setUserID(Integer.parseInt(allParams.get("USER_ID")));
		driver.setFirstName(allParams.get("FIRST_NAME"));
		driver.setLastName(allParams.get("LAST_NAME"));
		driver.setPhoneNo(allParams.get("PHONE_NO"));
		driver.setCNIC(allParams.get("CNIC"));
		driver.setLicenseNumber(allParams.get("LICENSE_NUMBER"));

		boolean status = crudController.registerDriver(driver);

		// Package response
		JSONObject result = new JSONObject();
		if (status) {
			result.put("STATUS", 200);
		} else {
			result.put("STATUS", 400);
		}
		return result.toString();
	}

	@RequestMapping(value = "/persistDriverLogin", method = RequestMethod.POST)
	public String persistDriverLogin(@RequestBody Map<String, String> allParams, HttpServletRequest request)
	{
		// TODO Verify Driver Account
		if (request.getSession(false) == null) {
			request.getSession(true);
		}
		return new JSONObject().put("STATUS", 200).toString();
	}

	// TODO: Add layer that checks user is logged out in database
	@RequestMapping(value = "/logout_rider", method = RequestMethod.POST)
	public String logoutRider(@RequestBody Map<String, String> allParams, HttpServletRequest request)
	{
		request.getSession().invalidate();
		return new JSONObject().put("STATUS_CODE", 200).toString();
	}

	@RequestMapping(value = "/logout_driver", method = RequestMethod.POST)
	public String logoutDriver(@RequestBody Map<String, String> allParams, HttpServletRequest request)
	{
		request.getSession().invalidate();
		return new JSONObject().put("STATUS_CODE", 200).toString();
	}
	/* End of section*/


	/* Fetch user data methods
	@GetMapping("/rider_details")
	public String riderDetails(Model model, HttpSession session)
	{
		List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if (messages != null) {
			System.out.println("Rider deets called");
			return crudController.riderDetails().toString();
		}
		else {
			return null;
		}
	}

	@GetMapping("/driver_details")
	public String driverDetails()
	{
		return crudController.driverDetails().toString();
	}*/

	@RequestMapping(value = "/rider_data", method = RequestMethod.POST)
	public String riderData(@RequestBody Map<String, String> allParams, HttpServletRequest request)
	{
		if (request.getSession(false) == null) {
			return null;
		}

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
	public String driverData(@RequestBody Map<String, String> allParams, HttpServletRequest request)
	{
		if (request.getSession(false) == null) {
			return null;
		}

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

	@RequestMapping(value = "/selectActiveVehicle", method = RequestMethod.POST)
	public String selectActiveVehicle(@RequestBody Map<String, String> allParams, HttpServletRequest request) {

		if (request.getSession(false) == null) {
			return null;
		}

		Driver driver = new Driver();
		driver.setUserID(Integer.parseInt(allParams.get("USER_ID")));
		driver.setActiveVehicleID(Integer.parseInt(allParams.get("ACTIVE_VEHICLE_ID")));

		JSONObject result = new JSONObject();
		boolean vehicleSet = crudController.setActiveVehicle(driver);

		result.put("STATUS", ((vehicleSet)? 200 : 404));
		return result.toString();
	}
	/* End of section */

	// TODO: deleteRider
	// TODO: deleteDriver

	/* Rider-side matchmaking method*/
	@RequestMapping(value = "/findDriver", method = RequestMethod.POST)
	public String findDriver(@RequestBody Map<String, String> allParams, HttpServletRequest request)
	{
		if (request.getSession(false) == null) {
			return null;
		}

		Rider rider = new Rider();
		rider.setUserID(Integer.parseInt(allParams.get("USER_ID")));

		Ride fetchedRide = matchmakingController.findDriver(rider,
				new Location(Double.parseDouble(allParams.get("SOURCE_LAT")),
						Double.parseDouble(allParams.get("SOURCE_LONG")), null),
				new Location(Double.parseDouble(allParams.get("DEST_LAT")),
						Double.parseDouble(allParams.get("DEST_LONG")), null),
				Integer.parseInt(allParams.get("PAYMENT_MODE")), Integer.parseInt(allParams.get("RIDE_TYPE")));

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
	public String setMarkActive(@RequestBody Map<String, String> allParams, HttpServletRequest request)
	{
		if (request.getSession(false) == null) {
			return null;
		}

		Driver driver = new Driver();
		driver.setUserID(Integer.parseInt(allParams.get("USER_ID")));
		driver.setActive(Boolean.valueOf(allParams.get("ACTIVE_STATUS")));

		JSONObject json = new JSONObject();

		if (matchmakingController.setMarkActive(driver)) {
			json.put("STATUS", 200);
		}
		else {
			json.put("STATUS", 404);
		}
		return json.toString();
	}

	@RequestMapping(value = "/startMatchmaking", method = RequestMethod.POST)
	public String startMatchmaking(@RequestBody Map<String, String> allParams, HttpServletRequest request)
	{
		if (request.getSession(false) == null) {
			return null;
		}

		Driver driver = new Driver();
		driver.setUserID(Integer.parseInt(allParams.get("USER_ID")));

		RideRequest rideRequest = matchmakingController.startMatchmaking(driver);
		String result = null;

		if (rideRequest != null) {
			try {
				result = objectMapper.writeValueAsString(rideRequest);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return result;
	}

	@RequestMapping(value = "/checkRideRequestStatus", method = RequestMethod.POST)
	public String checkRideRequestStatus(@RequestBody Map<String, String> allParams, HttpServletRequest request)
	{
		if (request.getSession(false) == null) {
			return null;
		}
		Driver driver = new Driver();
		driver.setUserID(Integer.parseInt(allParams.get("USER_ID")));

		RideRequest rideRequest = matchmakingController.checkRideRequestStatus(driver);
		String result = null;

		if (rideRequest != null) {
			try {
				result = objectMapper.writeValueAsString(rideRequest);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return result;
	}

	@RequestMapping(value = "/checkRideStatus", method = RequestMethod.POST)
	public String checkRideStatus(@RequestBody Map<String, String> allParams, HttpServletRequest request)
	{
		if (request.getSession(false) == null) {
			return null;
		}

		RideRequest rideRequest = new RideRequest();
		rideRequest.getDriver().setUserID(Integer.parseInt(allParams.get("USER_ID")));
		rideRequest.getRider().setUserID(Integer.parseInt(allParams.get("RIDER_ID")));

		Ride ride = matchmakingController.getRideForDriver(rideRequest);

		String result = null;
		if (ride != null) {
			try {
				result = objectMapper.writeValueAsString(ride);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return result;
	}

	@RequestMapping(value = "/confirmRideRequest", method = RequestMethod.POST)
	public String confirmRideRequest(@RequestBody Map<String, String> allParams, HttpServletRequest request)
	{
		if (request.getSession(false) == null) {
			return null;
		}

		RideRequest rideRequest = new RideRequest();

		rideRequest.getDriver().setUserID(Integer.parseInt(allParams.get("USER_ID")));
		rideRequest.getRider().setUserID(Integer.parseInt(allParams.get("RIDER_ID")));
		rideRequest.setFindStatus(Integer.parseInt(allParams.get("FOUND_STATUS")));

		JSONObject jsonObject = new JSONObject();
		if (matchmakingController.confirmRideRequest(rideRequest)) {
			jsonObject.put("STATUS", 200);
		} else {
			jsonObject.put("STATUS", 404);
		}
		return jsonObject.toString();
	}
	@RequestMapping(value = "/markArrival", method = RequestMethod.POST)
	public String markDriverArrival(@RequestBody Map<String, String> allParams, HttpServletRequest request)
	{
		if (request.getSession(false) == null) {
			return null;
		}

		Ride ride = new Ride();
		ride.setRideID(Integer.parseInt(allParams.get("RIDE_ID")));

		JSONObject jsonObject = new JSONObject();
		if (matchmakingController.markDriverArrival(ride)) {
			jsonObject.put("STATUS", 200);
		} else {
			jsonObject.put("STATUS", 404);
		}
		return jsonObject.toString();
	}

	@RequestMapping(value = "/startRideDriver", method = RequestMethod.POST)
	public String startRideDriver(@RequestBody Map<String, String> allParams, HttpServletRequest request)
	{
		if (request.getSession(false) == null) {
			return null;
		}

		Ride ride = new Ride();
		ride.setRideID(Integer.parseInt(allParams.get("RIDE_ID")));

		JSONObject jsonObject = new JSONObject();
		if (matchmakingController.startRideDriver(ride)) {
			jsonObject.put("STATUS", 200);
		} else {
			jsonObject.put("STATUS", 404);
		}
		return jsonObject.toString();
	}

	@RequestMapping(value = "/markArrivalAtDestination", method = RequestMethod.POST)
	public String markArrivalAtDestination(@RequestBody Map<String, String> allParams, HttpServletRequest request)
	{
		if (request.getSession(false) == null) {
			return null;
		}

		System.out.println("MARK ARRIVAL CALLED");

		Ride ride = new Ride();
		ride.setRideID(Integer.parseInt(allParams.get("RIDE_ID")));
		ride.setDistanceTravelled(Double.parseDouble(allParams.get("DIST_TRAVELLED")));
		ride.getDriver().setUserID(Integer.parseInt(allParams.get("DRIVER_ID")));

		JSONObject result = new JSONObject();
		double fare = matchmakingController.markArrivalAtDestination(ride);
		if (fare > 0) {
			result.put("FARE", fare);
		} else {

		}
		return result.toString();
	}

	//TODO: send payment, not change and package into ride
	@RequestMapping(value = "/endRideWithPayment", method = RequestMethod.POST)
	public String endRideWithPayment(@RequestBody Map<String, String> allParams, HttpServletRequest request)
	{
		if (request.getSession(false) == null) {
			return null;
		}

		// Unwrapping Objects
		Ride ride = new Ride();
		ride.setRideID(Integer.parseInt(allParams.get("RIDE_ID")));
		ride.getDriver().setUserID(Integer.parseInt(allParams.get("DRIVER_ID")));
		ride.setPaymentMethod(Integer.parseInt(allParams.get("PAYMENT_MODE")));

		JSONObject jsonObject = new JSONObject();
		if (matchmakingController.endRideWithPayment(ride, Double.parseDouble(allParams.get("AMNT_PAID")),
                Double.parseDouble(allParams.get("CHANGE")))) {
			jsonObject.put("STATUS", 200);
		}
		else {
			jsonObject.put("STATUS", 404);
		}
		return jsonObject.toString();
	}

	/* End of section */


	/* Ride system calls */

	@RequestMapping(value = "/getRideForRider", method = RequestMethod.POST)
	public String getRideForRider(@RequestBody Map<String, String> allParams, HttpServletRequest request)
	{
		if (request.getSession(false) == null) {
			return null;
		}

		Rider rider = new Rider();
		rider.setUserID(Integer.parseInt(allParams.get("USER_ID")));

		Ride fetchedRide = matchmakingController.getRideForRider(rider);

		if (fetchedRide != null) {
			try {
				return objectMapper.writeValueAsString(fetchedRide);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

	@RequestMapping(value = "/getRideStatus", method = RequestMethod.POST)
	public String getRideStatus(@RequestBody Map<String, String> allParams, HttpServletRequest request)
	{
		if (request.getSession(false) == null) {
			return null;
		}

		Ride ride = new Ride();
		ride.setRideID(Integer.parseInt(allParams.get("RIDE_ID")));

		matchmakingController.getRideStatus(ride);

		if (ride.getRideStatus() != Ride.DEFAULT) {
			JSONObject result = new JSONObject();
			result.put("RIDE_STATUS", ride.getRideStatus());
			result.put("FARE", ride.getFare());
			return result.toString();
		}
		else {
			return null;
		}
	}

	@RequestMapping(value = "/acknowledgeEndOfRide", method = RequestMethod.POST)
	public String acknowledgeEndOfRide(@RequestBody Map<String, String> allParams, HttpServletRequest request)
	{
		if (request.getSession(false) == null) {
			return null;
		}

		Ride ride = new Ride();
		ride.setRideID(Integer.parseInt(allParams.get("RIDE_ID")));
		ride.setRider(new Rider());
		ride.getRider().setUserID(Integer.parseInt(allParams.get("RIDER_ID")));

		JSONObject result = new JSONObject();
		result.put("STATUS_CODE", ((matchmakingController.acknowledgeEndOfRide(ride))? 200 : 404));
		return result.toString();
	}

	@RequestMapping(value = "/giveFeedbackForDriver", method = RequestMethod.POST)
	public String giveFeedbackForDriver(@RequestBody Map<String, String> allParams, HttpServletRequest request)
	{
		if (request.getSession(false) == null) {
			return null;
		}

		Ride ride = new Ride();
		ride.setRideID(Integer.parseInt(allParams.get("RIDE_ID")));
		ride.getDriver().setUserID(Integer.parseInt(allParams.get("DRIVER_ID")));

		JSONObject result = new JSONObject();
		boolean feedbackSubmitted = matchmakingController.giveFeedbackForDriver(ride, Float.parseFloat(allParams.get("RATING")));

		result.put("STATUS", ((feedbackSubmitted)? 200 : 404));
		return result.toString();
	}

	@RequestMapping(value = "/giveFeedbackForRider", method = RequestMethod.POST)
	public String giveFeedbackForRider(@RequestBody Map<String, String> allParams, HttpServletRequest request)
	{
		if (request.getSession(false) == null) {
			return null;
		}

		Ride ride = new Ride();
		ride.setRideID(Integer.parseInt(allParams.get("RIDE_ID")));
		ride.getRider().setUserID(Integer.parseInt(allParams.get("RIDER_ID")));

		JSONObject result = new JSONObject();
		boolean feedbackSubmitted = matchmakingController.giveFeedbackForRider(ride, Float.parseFloat(allParams.get("RATING")));

		result.put("STATUS", ((feedbackSubmitted)? 200 : 404));
		return result.toString();
	}

	/* End of section */

	/* Location update methods*/
	@RequestMapping(value = "/saveDriverLocation", method = RequestMethod.POST)
	public String saveDriverLocation(@RequestBody Map<String, String> allParams, HttpServletRequest request)
	{
		if (request.getSession(false) == null) {
			return null;
		}

		Driver driver = new Driver();
		driver.setUserID(Integer.parseInt(allParams.get("USER_ID")));
		driver.setCurrentLocation(new Location(Double.valueOf(allParams.get("LATITUDE")), Double.valueOf(allParams.get("LONGITUDE")),
				Long.parseLong(allParams.get("TIMESTAMP"))));

		boolean locationSaved = locationController.saveDriverLocation(driver);

		JSONObject result = new JSONObject();
		result.put("STATUS", ((locationSaved)? 200 : 404));
		return result.toString();
	}

	@RequestMapping(value = "/saveRiderLocation", method = RequestMethod.POST)
	public String saveRiderLocation(@RequestBody Map<String, String> allParams, HttpServletRequest request)
	{
		if (request.getSession(false) == null) {
			return null;
		}

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
	public String getDriverLocations(@RequestBody Map<String, String> allParams, HttpServletRequest request)
	{
		if (request.getSession(false) == null) {
			return null;
		}

		try {
			ArrayList<Location> locations = locationController.getDriverLocations();

			if (locations == null) {
				return null;
			} else {
				return objectMapper.writeValueAsString(locations);
			}
		}
		catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/getRiderLocations", method = RequestMethod.POST)
	public String getRiderLocations(@RequestBody Map<String, String> allParams, HttpServletRequest request)
	{
		if (request.getSession(false) == null) {
			return null;
		}

		try {
			ArrayList<Location> locations = locationController.getRiderLocations();

			if (locations == null) {
				return null;
			} else {
				return objectMapper.writeValueAsString(locations);
			}
		}
		catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/getDriverLocation", method = RequestMethod.POST)
	public String getDriverLocation(@RequestBody Map<String, String> allParams, HttpServletRequest request)
	{
		if (request.getSession(false) == null) {
			return null;
		}

		Driver driver = new Driver();
		driver.setUserID(Integer.parseInt(allParams.get("USER_ID")));

		locationController.getDriverLocation(driver);

		JSONObject result = new JSONObject();

		if (driver.getCurrentLocation() == null) {
			result.put("STATUS_CODE", 404);
		}
		else {
			result.put("STATUS_CODE", 200);
			result.put("LATITUDE", driver.getCurrentLocation().getLatitude());
			result.put("LONGITUDE", driver.getCurrentLocation().getLongitude());
		}
		return result.toString();
	}

	@RequestMapping(value = "/getRiderLocation", method = RequestMethod.POST)
	public String getRiderLocation(@RequestBody Map<String, String> allParams, HttpServletRequest request)
	{
		if (request.getSession(false) == null) {
			return null;
		}

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


	/* Vehicle methods */

	/*
	* Param: STATUS - previous status
	* If STATUS = Vehicle.VH_REJECTED, must have old registrationRequestID
	* ELIF STATUS = VH_DEFAULT, send new request
	* */
	@RequestMapping(value = "/sendVehicleRequest", method = RequestMethod.POST)
	public String sendVehicleRequest(@RequestBody Map<String, String> allParams, HttpServletRequest request) {

		Driver driver = new Driver();
		driver.setUserID(Integer.parseInt(allParams.get("DRIVER_ID")));

		ArrayList<Vehicle> vehicles = new ArrayList<>();
		Vehicle vehicle = new Vehicle();
		if (allParams.containsKey("REGISTRATION_REQ_ID")) {
			System.out.println("Setting reg req id: " + Integer.parseInt(allParams.get("REGISTRATION_REQ_ID")));
			vehicle.setVehicleID(Integer.parseInt(allParams.get("REGISTRATION_REQ_ID")));
		}
		else {
			System.out.println("Default reg req id");
			vehicle.setVehicleID(Vehicle.DEFAULT_ID);
		}
		vehicle.setMake(allParams.get("MAKE"));
		vehicle.setModel(allParams.get("MODEL"));
		vehicle.setYear(allParams.get("YEAR"));
		vehicle.setNumberPlate(allParams.get("NUMBER_PLATE"));
		vehicle.setColor(allParams.get("COLOR"));
		vehicle.setStatus(Integer.parseInt(allParams.get("STATUS")));

		vehicles.add(vehicle);
		driver.setVehicles(vehicles);

		JSONObject result = new JSONObject();
		boolean requestSent = crudController.sendVehicleRequest(driver);

        result.put("STATUS", ((requestSent)?200:404));
		return result.toString();
	}

	/* End of section*/


	/* Admin system methods*/

	/*
	* Param STATUS - new status
	* VH_REQUEST_REJECTED  or VH_REQUEST_ACCEPTED
	* VEHICLE_TYPE_ID
	* DRIVER_ID and REGISTRATION_REQ_ID
	* */
	@RequestMapping(value = "/respondToVehicleRequest", method = RequestMethod.POST)
	public String respondToVehicleRequest(@RequestBody Map<String, String> allParams, HttpServletRequest request) {
		/* TODO: Admin login required?
		if (request.getSession(false) == null) {
			return null;
		} */

		Driver driver = new Driver();
		driver.setUserID(Integer.parseInt(allParams.get("DRIVER_ID")));

		ArrayList<Vehicle> vehicles = new ArrayList<>();
		Vehicle vehicleRequest = new Vehicle();
		vehicleRequest.setVehicleID(Integer.parseInt(allParams.get("REGISTRATION_REQ_ID")));
		vehicleRequest.setVehicleTypeID(Integer.parseInt(allParams.get("VEHICLE_TYPE_ID")));
		vehicleRequest.setStatus(Integer.parseInt(allParams.get("STATUS")));
		vehicles.add(vehicleRequest);

		driver.setVehicles(vehicles);

        JSONObject result = new JSONObject();
		boolean responseSent = crudController.respondToVehicleRequest(driver);
        result.put("STATUS", ((responseSent)?200:404));
        return result.toString();
	}
	@RequestMapping(value = "/respondToDriverRequest", method = RequestMethod.POST)
	public String respondToDriverRequest(@RequestBody Map<String, String> allParams, HttpServletRequest request)
	{
		/* */
		Driver driver = new Driver();
		driver.setUserID(Integer.parseInt(allParams.get("DRIVER_ID")));
		driver.setStatus(Integer.parseInt(allParams.get("STATUS")));

		boolean status = crudController.respondToDriverRequest(driver);

		// Packaging Response
		JSONObject jsonObject = new JSONObject();
		if (status) {
			jsonObject.put("STATUS", 200);
		} else {
			jsonObject.put("STATUS", 404);
		}
		return jsonObject.toString();
	}
	/* End of section*/


	// Test method
    @RequestMapping(value = "/jacksonTest", method = RequestMethod.POST)
    public String sendVehicleRequest(@RequestBody String body, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        try {
            Driver driver = objectMapper.readValue(body, Driver.class);
            jsonObject.put("STATUS", 200);
            return jsonObject.toString();
        }
        catch (Exception e) {
            System.out.println("jacksonTest: exception");
            jsonObject.put("STATUS", 404);
            return jsonObject.toString();
        }
    }

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}

}
