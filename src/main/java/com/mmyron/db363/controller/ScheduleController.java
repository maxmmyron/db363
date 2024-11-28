package com.mmyron.db363.controller;

import java.util.Optional;
import java.util.stream.Stream;

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
import com.mmyron.db363.entitiy.Schedule;
import com.mmyron.db363.entitiy.Station;
import com.mmyron.db363.entitiy.StationPK;
import com.mmyron.db363.entitiy.Train;
import com.mmyron.db363.repo.PassengerRepo;
import com.mmyron.db363.repo.ScheduleRepo;
import com.mmyron.db363.repo.StationRepo;
import com.mmyron.db363.util.TrainDirection;

@Controller
@RequestMapping(path="/api/schedules")
public class ScheduleController {
	@Autowired
	private ScheduleRepo scheduleRepo;
	
	@Autowired
	private StationRepo stationRepo;
	
	// create
	
	@PostMapping(path="/create")
	public @ResponseBody Schedule addSchedule(@RequestParam String origin, @RequestParam String dest, @RequestParam String route, @RequestParam String dir) {
		Station o = stationRepo.findById(new StationPK(origin, route)).orElse(null);
		Station d = stationRepo.findById(new StationPK(dest, route)).orElse(null);
		
		TrainDirection tDir = null;
		
		try {
			tDir = TrainDirection.valueOf(dir);
		} catch(IllegalArgumentException e) {
			tDir = null;
		}
		
		if(!Stream.of(o, d, tDir).allMatch((x) -> x != null)) {
			String err = "Error creating scheudle: \n";
			if(o == null) err += "\t- Station" + origin + " does not exist on route " + route + "\n";
			if(d == null) err += "\t- Station" + dest + " does not exist on route " + route + "\n";
			if(tDir == null) err += "\t- Direction is invalid (must be INBOUND or OUTBOUND)";
			
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, err);
		}
		
		Schedule sch = new Schedule(o, d, tDir);
		scheduleRepo.save(sch);
		
		return sch;
	}
	
	// read
	
	@GetMapping(path="/")
	public @ResponseBody Iterable<Schedule> getSchedules() {
		return scheduleRepo.findAll();
	}
	
	@GetMapping(path="/{id}")
	public @ResponseBody Schedule getSchedule(@PathVariable Long id) {
		Optional<Schedule> s = scheduleRepo.findById(id);
		if(s.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No schedule found with ID " + id);
		return s.get();
	}
	
	// update
	
	@PutMapping(path="/{id}")
	public @ResponseBody Schedule updateSchedule(@PathVariable Long id) {
		Schedule s = scheduleRepo.findById(id).orElse(null);
		if(s == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error updating schedule: Schedule " + id + " does not exist."); 
		
		scheduleRepo.save(s);
		return s;
	}
	
	// delete
	@DeleteMapping(path="/{id}")
	public @ResponseBody boolean deletePassenger(@PathVariable Long id) {
		Optional<Schedule> s = scheduleRepo.findById(id);
		if(s.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No schedule found with ID " + id);
		scheduleRepo.delete(s.get());
		return true;
	}
}
