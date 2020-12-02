
package com.savaari_demo;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@SpringBootApplication
@RestController
public class DemoApplication
{
	// Main Attributes
	private static Controller controller;

	// MAIN METHOD
	public static void main(String[] args) 
	{
		controller = new Controller();
		SpringApplication.run(DemoApplication.class, args);
	}

	// API REQUESTS
	@GetMapping("/add_rider")
	public JSONObject addRider(@RequestParam Map<String, String> allParams)
	{
		String username = allParams.get("username");
		String email_address = allParams.get("email_address");
		String password = allParams.get("password");

		return controller.addRider(username, email_address, password);
	}

	@GetMapping("/add_driver")
	public JSONObject addDriver(@RequestParam Map<String, String> allParams)
	{
		String username = allParams.get("username");
		String email_address = allParams.get("email_address");
		String password = allParams.get("password");

		return controller.addDriver(username, email_address, password);
	}

	@GetMapping("/login_rider")
	public JSONObject loginRider(@RequestParam Map<String, String> allParams)
	{
		String email_address = allParams.get("username");
		String password = allParams.get("password");

		return controller.loginRider(email_address, password);
	}

	@GetMapping("/login_driver")
	public JSONObject loginDriver(@RequestParam Map<String, String> allParams)
	{
		String email_address = allParams.get("username");
		String password = allParams.get("password");

		return controller.loginDriver(email_address, password);
	}

	@GetMapping("/rider_details")
	public JSONArray riderDetails()
	{
		return controller.riderDetails();
	}

	@GetMapping("/driver_details")
	public JSONArray driverDetails()
	{
		return controller.driverDetails();
	}

	@GetMapping("/rider_data")
	public JSONObject riderData(@RequestParam Map<String, String> allParams)
	{
		String userID = allParams.get("USER_ID");
		return controller.riderData(userID);
	}

	@GetMapping("/driver_data")
	public JSONObject driverData(@RequestParam Map<String, String> allParams)
	{
		String userID = allParams.get("USER_ID");
		return controller.driverData(userID);
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}

}
