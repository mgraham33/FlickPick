package com.ljanda.restController.hello;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/object")	
public class restController {

	@GetMapping()	
	public String getObj() {
		return "GET Request";
	}
	
	@GetMapping("/name/{name}")	
	public String getObjName(@PathVariable(name = "name") String name) {
		return "GET Request, Object Name: " + name;
	}
	
	@GetMapping("/color")	
	public String getObjColorAndSize(@RequestParam String color, @RequestParam String size) {
		return "GET Request, Object Color: " + color + ", Size: " + size;
	}
	
	@PostMapping()	
	public String postObj() {
		return "POST Request";
	}
	
	@DeleteMapping()	
	public String deleteObj() {
		return "DELETE Request";	
	}
	@PutMapping()	
	public String putObj() {
		return "PUT Request";
	}
}
