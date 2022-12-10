package com.cs309.tutorial.tests;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class TestController {

	ArrayList<String> wordList;

	TestController() {
		wordList = new ArrayList<String>();
		wordList.add("Bubble");
		wordList.add("Sandwich");
		wordList.add("Football");
		wordList.add("Cy");
		wordList.add("Welch");
		wordList.add("Miller");
	}

	@GetMapping("/getTest")
	public String getTest(@RequestParam(value = "username", defaultValue = "World") String message) {
		return String.format("Hello, %s! You sent a get request with a parameter!", message);
	}

	@PostMapping("/postTest1")
	public String postTest1(@RequestParam(value = "username", defaultValue = "World") String message) {
		return String.format("Hello, %s! You sent a post request with a parameter!", message);
	}

	@PostMapping("/postTest2")
	public String postTest2(@RequestBody TestData testData) {
		return String.format("Hello, %s! You sent a post request with a requestbody!", testData.getMessage());
	}

	@DeleteMapping("/deleteTest/{index}")
	public String deleteTest(@PathVariable int index) {
		wordList.remove(index);
		return "Word List: " + wordList;
	}

	@PutMapping("/putTest/{word}")
	public String putTest(@PathVariable String word) {
		wordList.add(word);
		return "Word List: " + wordList;
	}
}
