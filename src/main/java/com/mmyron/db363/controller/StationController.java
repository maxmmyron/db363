package com.mmyron.db363.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mmyron.db363.entitiy.Station;
import com.mmyron.db363.repo.StationRepo;

@Controller
@RequestMapping(path="/api/trains")
public class StationController {
	// get auto-generated bean
	@Autowired
	private StationRepo stationRepo;
	
	@PostMapping(path="/add")
	public @ResponseBody String addStation() {
		Station s = new Station();
		stationRepo.save(s);
		
		return "Saved";
	}
	
	@GetMapping(path="/all")
	public @ResponseBody Iterable<Station> getStation() {
		return stationRepo.findAll();
	}
	
	@GetMapping(path="/get/{id}")
	public @ResponseBody String getStation(@PathVariable Long id) {
		Optional<Station> s = stationRepo.findById(id);
		if(s.isEmpty()) return "No station found";
		return s.get().toString();
	}
}
