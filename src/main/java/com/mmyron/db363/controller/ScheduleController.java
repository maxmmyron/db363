package com.mmyron.db363.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.mmyron.db363.entitiy.Passenger;
import com.mmyron.db363.entitiy.Station;
import com.mmyron.db363.entitiy.StationPK;
import com.mmyron.db363.entitiy.Train;
import com.mmyron.db363.repo.PassengerRepo;
import com.mmyron.db363.util.TrainDirection;

@Controller
@RequestMapping(path="/api/passengers")
public class ScheduleController {
	// get auto-generated bean
	@Autowired
	private PassengerRepo passengerRepo;
	
	// create
	
	@PostMapping(path="/create")
	public @ResponseBody Passenger addPassenger(@RequestParam(name="first_name", required = true) String f, @RequestParam(name="last_name", required = true) String l) {
		Passenger p = new Passenger(f, l);
		passengerRepo.save(p);
		return p;
	}
	
	// read
	
	@GetMapping(path="/")
	public @ResponseBody Iterable<Passenger> getPassengers() {
		return passengerRepo.findAll();
	}
	
	@GetMapping(path="/{id}")
	public @ResponseBody Passenger getPassenger(@PathVariable Long id) {
		Optional<Passenger> p = passengerRepo.findById(id);
		if(p.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No passenger found with ID " + id);
		return p.get();
	}
	
	// update
	
	@PutMapping(path="/{id}")
	public @ResponseBody Passenger updatePassenger(@PathVariable Long id, @RequestParam(name="first_name") String f, @RequestParam(name="last_name") String l) {
		Passenger p = passengerRepo.findById(id).orElse(null);
		if(p == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error updating passenger: Passenger" + id + " does not exist."); 
		
		p.setFirstName(f == null ? p.getFirstName() : f);
		p.setLastName(l == null ? p.getFirstName() : l);
		
		passengerRepo.save(p);
		
		return p;
	}
	
	// delete
	@DeleteMapping(path="/{id}")
	public @ResponseBody boolean deletePassenger(@PathVariable Long id) {
		Optional<Passenger> p = passengerRepo.findById(id);
		if(p.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No passenger found with ID " + id);
		passengerRepo.delete(p.get());
		return true;
	}
}
