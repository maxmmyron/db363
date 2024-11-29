package com.mmyron.db363.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.mmyron.db363.entitiy.Station;
import com.mmyron.db363.entitiy.StationPK;
import com.mmyron.db363.repo.LinkRepo;
import com.mmyron.db363.repo.StationRepo;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(path="/api/stations")
public class StationController {
	// get auto-generated bean
	@Autowired
	private StationRepo stationRepo;
	
	// read
	
	@GetMapping(path="/")
	public @ResponseBody Iterable<Station> getStations() {
		return stationRepo.findAll();
	}
	
//	@GetMapping(path="/{route}")
//	public @ResponseBody Iterable<Station> getStationsByRoute (@PathVariable String route) { 
//		return stationRepo.findAllByRoute(route);
//	}
	
	@GetMapping(path="/{route}/{name}")
	public @ResponseBody Station getStation(@PathVariable String route, @PathVariable String name) {
		Optional<Station> s = stationRepo.findById(new StationPK(name, route));
		if(s.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Station " + name + " does not exist on route " + route);
		return s.get();
	}
}
