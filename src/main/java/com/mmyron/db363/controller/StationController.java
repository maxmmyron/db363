package com.mmyron.db363.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.mmyron.db363.dto.ScheduleVM;
import com.mmyron.db363.dto.StationVM;
import com.mmyron.db363.entitiy.Schedule;
import com.mmyron.db363.entitiy.Station;
import com.mmyron.db363.entitiy.StationPK;
import com.mmyron.db363.repo.LinkRepo;
import com.mmyron.db363.repo.StationRepo;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(path="/api/stations")
public class StationController {
	@Autowired
	private StationRepo stationRepo;
	
	@GetMapping(path="/")
	public @ResponseBody Iterable<StationVM> getStations() {
		List<StationVM> stations = new ArrayList<>();
		for(Station s : stationRepo.findAll()) {
			stations.add(new StationVM(s));
		}
		return stations;
	}
	
	@GetMapping(path="")
	public @ResponseBody StationVM getStation(@RequestParam String route, @RequestParam String name) {
		Optional<Station> s = stationRepo.findById(new StationPK(name, route));
		if(s.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Station " + name + " does not exist on route " + route);
		return new StationVM(s.get());
	}
}
