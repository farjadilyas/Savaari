
package com.savaari_demo;
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



	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}

}
