package com.mmyron.db363.controller;

import java.sql.Date;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

import com.mmyron.db363.entitiy.Passenger;
import com.mmyron.db363.entitiy.Station;
import com.mmyron.db363.entitiy.StationPK;
import com.mmyron.db363.entitiy.Ticket;
import com.mmyron.db363.entitiy.TicketPK;
import com.mmyron.db363.entitiy.Train;import com.mmyron.db363.repo.PassengerRepo;
import com.mmyron.db363.repo.StationRepo;
import com.mmyron.db363.repo.TicketRepo;
import com.mmyron.db363.repo.TrainRepo;
import com.mmyron.db363.util.TrainDirection;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(path="/api/tickets")
public class TicketController {
	// get auto-generated bean
	@Autowired
	private TicketRepo ticketRepo;
	@Autowired
	private PassengerRepo passengerRepo;
	@Autowired
	private TrainRepo trainRepo;
	@Autowired
	private StationRepo stationRepo;
	
	// create
	
	@PostMapping(path="/create")
	public @ResponseBody Ticket addTicket(
			@RequestParam(name = "passenger", required = true) Long pId, 
			@RequestParam(name = "train", required = true) Long tId,
			@RequestParam(name = "route", required = true) String route,
			@RequestParam(name = "origin", required = true) String origin, 
			@RequestParam(name = "dest", required = true) String dest, 
			@RequestParam(name = "departure", required = true) Date dep, 
			@RequestParam(name = "dir", required = true) String dir) {
		Passenger p = passengerRepo.findById(pId).orElse(null);
		Train t = trainRepo.findById(tId).orElse(null);
		Station o = stationRepo.findById(new StationPK(origin, route)).orElse(null);
		Station d = stationRepo.findById(new StationPK(dest, route)).orElse(null);
		TrainDirection tDir = null;
	
		try {
			tDir = TrainDirection.valueOf(dir);
		} catch(IllegalArgumentException e) {
			tDir = null;
		}
		
		if(!Stream.of(p, t, o, d, tDir).allMatch((x) -> x != null)) {
			String err = "Error creating ticket: \n";
			if(p == null) err += "\t- Passenger " + pId + "does not exist\n";
			if(t == null) err += "\t- Train " + tId + "does not exist\n";
			if(o == null) err += "\t- Station" + origin + " does not exist on route " + route + "\n";
			if(d == null) err += "\t- Station" + dest + " does not exist on route " + route + "\n";
			if(tDir == null) err += "\t- Direction is invalid (must be INBOUND or OUTBOUND)";
			
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, err);
		}
		
		Ticket tx = new Ticket(new TicketPK(pId, tId), o, d, dep, tDir);
		ticketRepo.save(tx);
		
		return tx;
	}
	
	// read
	
	@GetMapping(path="/")
	public @ResponseBody Iterable<Ticket> getTickets() {
		return ticketRepo.findAll();
	}
	
	@GetMapping(path="/{id}")
	public @ResponseBody Ticket getTicket(@PathVariable Long id) {
		Optional<Ticket> t = ticketRepo.findById(id);
		if(t.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No ticket found with ID " + id);
		return t.get();
	}
	
	// update

	@PutMapping("/{id}")
	public @ResponseBody Ticket updateTicket(
			@PathVariable Long id, 
			@RequestParam(name = "passenger") Long pId, 
			@RequestParam(name = "train") Long tId,
			@RequestParam(name = "route") String route,
			@RequestParam(name = "origin") String origin, 
			@RequestParam(name = "dest") String dest, 
			@RequestParam(name = "departure") Date dep, 
			@RequestParam(name = "dir") String dir) {
		Ticket tx = ticketRepo.findById(id).orElse(null);
		if(tx == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error updating ticket: No ticket found with ID " + id);
		
		if(pId == null) pId = tx.getId().getPassengerId();
		if(tId == null) tId = tx.getId().getTrainId();
		
		// generic validation
		Passenger p = passengerRepo.findById(pId).orElse(null);
		Train t = trainRepo.findById(tId).orElse(null);
		
		if(t != null && t.getSchedule() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error updating ticket: Train " + tId + "has no assigned schedule");
		
		String trainScheudleRoute = t.getSchedule().getOriginStation().getId().getTrainRoute();
		
		route = route == null ? trainScheudleRoute : route;
		origin = origin == null ? tx.getOrigin().getId().getName() : origin;
		dest = dest == null ? tx.getDest().getId().getName() : dest;
		
		Station o = stationRepo.findById(new StationPK(origin, route)).orElse(null);
		Station d = stationRepo.findById(new StationPK(dest, route)).orElse(null);
		
		TrainDirection tDir = null;
	
		try {
			tDir = TrainDirection.valueOf(dir);
		} catch(IllegalArgumentException e) {
			tDir = null;
		}
		
		if(!Stream.of(p, t, o, d, tDir).allMatch((x) -> x != null)) {
			String err = "Error updating ticket: \n";
			if(p == null) err += "\t- Passenger " + pId + "does not exist\n";
			if(t == null) err += "\t- Train " + tId + "does not exist\n";
			if(o == null) err += "\t- Station" + origin + " does not exist on route " + route + "\n";
			if(d == null) err += "\t- Station" + dest + " does not exist on route " + route + "\n";
			if(tDir == null) err += "\t- Direction is invalid (must be INBOUND or OUTBOUND)";
			
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, err);
		}
	
		// special validation case: train route must eq. station routes
		if(!trainScheudleRoute.equals(route)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error updating ticket: Train on route" + trainScheudleRoute + " cannot be assigned to ticket with route " + route);
		
		tx.setId(new TicketPK(pId, tId));
		tx.setOrigin(o);
		tx.setDest(d);
		tx.setDeparture(dep);
		tx.setDirection(tDir);
		
		ticketRepo.save(tx);
		
		return tx;
	} 
	
	// delete
	
	@DeleteMapping("/{id}")
	public @ResponseBody boolean deleteTicket(@PathVariable Long id) {
		Optional<Ticket> t = ticketRepo.findById(id);
		if(t.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No ticket found with ID " + id);
		ticketRepo.delete(t.get());
		return true;
	}
}
