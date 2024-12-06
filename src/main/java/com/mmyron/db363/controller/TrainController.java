package com.mmyron.db363.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.mmyron.db363.dto.ScheduleVM;
import com.mmyron.db363.dto.TrainVM;
import com.mmyron.db363.entitiy.Link;
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
	public @ResponseBody TrainVM addTrain(@RequestBody TrainVM train) {		
		Station s = stationRepo.findById(new StationPK(train.getStation().getName(), train.getStation().getRoute())).orElse(null);
		
		Schedule sch = schedRepo.findById(train.getSchedule().getId()).orElse(null);
		
		if(sch == null && train.getSchedule().getId() != null || s == null && train.getStation() != null) {
			String err = "Error creating train: \n";
			if(s == null) err += "\t- Station" + train.getStation().getName() + " does not exist for route " + train.getStation().getRoute() + "\n";
			if(sch == null) err += "\t- No schedule found with id " + train.getSchedule().getId() + "\n";
			
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, err);
		} 
		
		return new TrainVM(trainRepo.save(new Train(s, train.getStatus(), sch)));
	}
	
	// read
	
	@GetMapping(path="/")
	public @ResponseBody Iterable<TrainVM> getTrains() {
		List<TrainVM> trains = new ArrayList<>();
		for(Train s : trainRepo.findAll()) {
			trains.add(new TrainVM(s));
		}
		return trains;
	}
	
	@GetMapping(path="/{id}")
	public @ResponseBody TrainVM getTrain(@PathVariable Long id) {
		Optional<Train> t = trainRepo.findById(id);
		if(t.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No train found with ID " + id);
		return new TrainVM(t.get());
	}
	
	// update
	
	@PutMapping(path="/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public @ResponseBody TrainVM updateTrain(@PathVariable Long id, @RequestBody TrainVM train) {
		Train t = trainRepo.findById(id).orElse(null);
		if(t == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error updating train: Train " + id + " does not exist.");
		
		Station s;
		if(train.getStation() == null) s = t.getStation();
		else s = stationRepo.findById(new StationPK(train.getStation().getName(), train.getStation().getRoute())).orElse(null);
		
		Schedule sch = train.getSchedule().getId() == null ? t.getSchedule() : schedRepo.findById(train.getSchedule().getId()).orElse(null);
		
		if(s == null && train.getStation() != null || sch == null && train.getSchedule() != null) {
			String err = "Error creating train: \n";
			if(s == null) err += "\t- Station" + train.getStation().getName() + " does not exist for route " + train.getStation().getRoute() + "\n";
			if(sch == null) err += "\t- No schedule found with id " + train.getSchedule().getId() + "\n";
			
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, err);
		}
		
		t.setSchedule(sch);
		t.setStation(s);
		
		if(train.getLink() != null) {
			StationPK o = new StationPK(train.getLink().getOrigin(), train.getLink().getRoute());
			StationPK d = new StationPK(train.getLink().getDest(), train.getLink().getRoute());
			Link l = new Link(o, d, train.getLink().getDuration(), train.getLink().getDistance());
			t.setLink(l);
		}
		
		t.setSchedDep(train.getSchedDep() == null ? t.getSchedDep() : train.getSchedDep());
		
		t.setStationArrival(train.getStationArrival() == null ? t.getStationArrival() : train.getStationArrival());
		t.setStationDep(train.getStationDep() == null ? t.getStationDep() : train.getStationDep());
		t.setStatus(train.getStatus() == null ? t.getStatus() : train.getStatus());
		
		return new TrainVM(trainRepo.save(t));
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
