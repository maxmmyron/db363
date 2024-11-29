package com.mmyron.db363.controller;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.mmyron.db363.entitiy.Schedule;
import com.mmyron.db363.entitiy.Station;
import com.mmyron.db363.entitiy.StationPK;
import com.mmyron.db363.entitiy.Train;
import com.mmyron.db363.repo.ScheduleRepo;
import com.mmyron.db363.repo.StationRepo;
import com.mmyron.db363.repo.TrainRepo;
import com.mmyron.db363.util.TrainDirection;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(path="/api/trains")
public class TrainController {
	// get auto-generated bean
	@Autowired
	private TrainRepo trainRepo;
	@Autowired
	private StationRepo stationRepo;
	@Autowired
	private ScheduleRepo schedRepo;
	
	// create
	
	@PostMapping(path="/create")
	public @ResponseBody Train addTrain(@RequestParam(name = "route", required = true) String route, @RequestParam(name = "station", required = true) String station, @RequestParam(name = "status", required = true) String status, @RequestParam(name = "schedule") Long schedId) {		
		Station s = stationRepo.findById(new StationPK(station, route)).orElse(null);
		
		Schedule sch = schedRepo.findById(schedId).orElse(null);
		
		if(!Stream.of(s, sch).allMatch((x) -> x != null)) {
			String err = "Error creating train: \n";
			if(s == null) err += "\t- Station" + station + " does not exist for route " + route + "\n";
			if(sch == null) err += "\t- No schedule found with id " + schedId + "\n";
			
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, err);
		}
		Train t = sch != null ? new Train(s, status, sch) : new Train(s, status); 
		trainRepo.save(t);
		
		return t;
	}
	
	// read
	
	@GetMapping(path="/")
	public @ResponseBody Iterable<Train> getTrains() {
		return trainRepo.findAll();
	}
	
	@GetMapping(path="/{id}")
	public @ResponseBody Train getTrain(@PathVariable Long id) {
		Optional<Train> t = trainRepo.findById(id);
		if(t.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No train found with ID " + id);
		return t.get();
	}
	
	// update
	
	@PutMapping(path="/{id}")
	public @ResponseBody Train updateTrain(@PathVariable Long id, @RequestParam(name="route") String route, @RequestParam(name = "station") String station, @RequestParam(name = "status") String status, @RequestParam(name = "schedule") Long schedId) {
		Train t = trainRepo.findById(id).orElse(null);
		if(t == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error updating train: Train " + id + " does not exist."); 
		
		route = route == null ? t.getStation().getId().getTrainRoute() : route;
		station = station == null ? t.getStation().getId().getName() : station;
		Station s = stationRepo.findById(new StationPK(station, route)).orElse(null);
		
		Schedule sch = schedId == null ? t.getSchedule() : schedRepo.findById(schedId).orElse(null);
		
		if(s == null || (sch == null && schedId != null)) {
			String err = "Error creating train: \n";
			if(s == null) err += "\t- Station" + station + " does not exist for route " + route + "\n";
			if(sch == null) err += "\t- No schedule found with id " + schedId + "\n";
			
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, err);
		}
		
		t.setStation(s);
		t.setStatus(status == null ? t.getStatus() : status);
		t.setSchedule(sch);
		
		trainRepo.save(t);
		
		return t;
	}
	
	// delete
	
	@DeleteMapping(path="/{id}")
	public @ResponseBody Boolean deleteTrain(@PathVariable Long id) {
		Optional<Train> t = trainRepo.findById(id);
		if(t.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No train found with ID " + id);
		trainRepo.delete(t.get());
		return true;
	}
}
