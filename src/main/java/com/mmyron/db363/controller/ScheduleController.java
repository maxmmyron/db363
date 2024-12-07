package com.mmyron.db363.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
import com.mmyron.db363.dto.ScheduleVM;
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
@CrossOrigin(origins = "*")
@RequestMapping(path="/api/schedules")
public class ScheduleController {
	@Autowired
	private ScheduleRepo scheduleRepo;

	@Autowired
	private StationRepo stationRepo;

	// create

	@PostMapping(path="/create", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ScheduleVM addSchedule(@RequestBody ScheduleVM schedule) {
		Station o = stationRepo.findById(new StationPK(schedule.getOrigin().getName(), schedule.getOrigin().getRoute())).orElse(null);
		Station d = stationRepo.findById(new StationPK(schedule.getDest().getName(), schedule.getDest().getRoute())).orElse(null);

		TrainDirection tDir = schedule.getDirection();

		if(!Stream.of(o, d, tDir).allMatch((x) -> x != null)) {
			String err = "Error creating scheudle: \n";
			if(o == null) err += "\t- Station" + schedule.getOrigin().getName() + " does not exist on route " + schedule.getOrigin().getRoute() + "\n";
			if(d == null) err += "\t- Station" + schedule.getDest().getName() + " does not exist on route " + schedule.getDest().getRoute() + "\n";
			if(tDir == null) err += "\t- Direction is invalid (must be INBOUND or OUTBOUND)";

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, err);
		}

		return new ScheduleVM(scheduleRepo.save(new Schedule(o, d, tDir)));
	}

	// read

	@GetMapping(path="/")
	public @ResponseBody Iterable<ScheduleVM> getSchedules() {
		List<ScheduleVM> schedules = new ArrayList<>();
		for(Schedule s : scheduleRepo.findAll()) {
			schedules.add(new ScheduleVM(s));
		}
		return schedules;
	}

	@GetMapping(path="/{id}")
	public @ResponseBody ScheduleVM getSchedule(@PathVariable Long id) {
		Optional<Schedule> s = scheduleRepo.findById(id);
		if(s.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No schedule found with ID " + id);
		return new ScheduleVM(s.get());
	}

	// update

	@PutMapping(path="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ScheduleVM updateSchedule(@PathVariable Long id, @RequestBody ScheduleVM schedule) {
		Schedule s = scheduleRepo.findById(id).orElse(null);
		if(s == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error updating schedule: Schedule " + id + " does not exist.");

		Station o = schedule.getOrigin() == null ? s.getOrigin() : stationRepo.findById(new StationPK(schedule.getOrigin().getName(), schedule.getOrigin().getRoute())).orElse(null);
		Station d = schedule.getDest() == null ? s.getDest() : stationRepo.findById(new StationPK(schedule.getDest().getName(), schedule.getDest().getRoute())).orElse(null);
		TrainDirection tDir = null;

		try {
			tDir = schedule.getDirection() == null ? s.getDirection() : schedule.getDirection();
		} catch(IllegalArgumentException e) {
			tDir = null;
		}

		if(!Stream.of(o, d, tDir).allMatch((x) -> x != null)) {
			String err = "Error creating scheudle: \n";
			if(o == null) err += "\t- Station" + schedule.getOrigin().getName() + " does not exist on route " + schedule.getOrigin().getRoute() + "\n";
			if(d == null) err += "\t- Station" + schedule.getDest().getName() + " does not exist on route " + schedule.getDest().getRoute() + "\n";
			if(tDir == null) err += "\t- Direction is invalid (must be INBOUND or OUTBOUND)";

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, err);
		}

		s.setOrigin(o);
		s.setDest(d);
		s.setDirection(tDir);

		return new ScheduleVM(scheduleRepo.save(s));
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
