package com.cs309.tutorial.tests;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class TestController {

	ArrayList<Integer> arr;

	TestController() {
		arr = new ArrayList<Integer>();
		arr.add(1);
		arr.add(2);
		arr.add(3);
	}

	@GetMapping("/getTest")
	public String getTest(@RequestParam(value = "username", defaultValue = "World") String message) {
		return String.format("Hello, %s! You sent a get request with a parameter! 'Hello World' is the default.", message);
	}
	
	@PostMapping("/postTest1")
	public String postTest1(@RequestParam(value = "username", defaultValue = "World") String message) {
		return String.format("Hello, %s! You sent a post request with a parameter! 'Hello world' is the default.", message);
	}
	
	@PostMapping("/postTest2")
	public String postTest2(@RequestBody TestData testData) {
		return String.format("Hello, %s! You sent a post request with a requestbody! Postman is needed for this test.", testData.getMessage());
	}
	
	@DeleteMapping("/deleteTest/{id}")
	public String deleteTest(@PathVariable("id") int id) {
		arr.remove(new Integer(id));
		return "Arr: " + arr;
	}
	
	@PutMapping("/putTest/{num}")
	public String putTest(@PathVariable int num) {
		arr.add(new Integer(num));
		return "Arr: " + arr;
	}
}
