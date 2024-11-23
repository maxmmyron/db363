package com.mmyron.db363.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mmyron.db363.entitiy.Passenger;
import com.mmyron.db363.repo.PassengerRepo;

@Controller
@RequestMapping(path="/api/passengers")
public class PassengerController {
	// get auto-generated bean
	@Autowired
	private PassengerRepo passengerRepo;
	
	@PostMapping(path="/add")
	public @ResponseBody String addPassenger(String f, String l) {
		Passenger p = new Passenger(f, l);
		passengerRepo.save(p);
		
		return "Saved";
	}
	
	@GetMapping(path="/all")
	public @ResponseBody Iterable<Passenger> getPassengers() {
		return passengerRepo.findAll();
	}
	
	@GetMapping(path="/get/{id}")
	public @ResponseBody String getPassenger(@PathVariable Long id) {
		Optional<Passenger> p = passengerRepo.findById(id);
		if(p.isEmpty()) return "No passenger found";
		return p.get().toString();
	}
}
