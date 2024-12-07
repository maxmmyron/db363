package com.mmyron.db363.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.mmyron.db363.dto.PassengerVM;
import com.mmyron.db363.entitiy.Passenger;
import com.mmyron.db363.entitiy.Station;
import com.mmyron.db363.entitiy.StationPK;
import com.mmyron.db363.entitiy.Train;
import com.mmyron.db363.repo.PassengerRepo;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(path="/api/passengers")
public class PassengerController {
	// get auto-generated bean
	@Autowired
	private PassengerRepo passengerRepo;

	// create

	@PostMapping(path="/create")
	public @ResponseBody PassengerVM addPassenger(@RequestBody PassengerVM pVM) {
		Passenger p = new Passenger();
		p.setFirstName(pVM.getFirstName());
		p.setLastName(pVM.getLastName());
		return new PassengerVM(passengerRepo.save(p));
	}

	// read

	@GetMapping(path="/")
	public @ResponseBody Iterable<PassengerVM> getPassengers() {
		List<PassengerVM> ps = new ArrayList<>();
		for(Passenger p : passengerRepo.findAll()) {
			ps.add(new PassengerVM(p));
		}
		return ps;
	}

	@GetMapping(path="/{id}")
	public @ResponseBody PassengerVM getPassenger(@PathVariable Long id) {
		Optional<Passenger> p = passengerRepo.findById(id);
		if(p.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No passenger found with ID " + id);
		return new PassengerVM(p.get());
	}

	// update

	@PutMapping(path="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody PassengerVM updatePassenger(@PathVariable Long id, @RequestBody PassengerVM passenger)
	{
		Passenger p = passengerRepo.findById(id).orElse(null);
		if(p == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error updating passenger: Passenger" + id + " does not exist.");

		if(passenger.getFirstName() != null) p.setFirstName(passenger.getFirstName());
		if(passenger.getLastName() != null) p.setLastName(passenger.getLastName());

		return new PassengerVM(passengerRepo.save(p));
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
