package com.mmyron.db363.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

import com.mmyron.db363.dto.ScheduleVM;
import com.mmyron.db363.dto.TicketVM;
import com.mmyron.db363.entitiy.Passenger;
import com.mmyron.db363.entitiy.Schedule;
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

	@PostMapping(path="/create", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody TicketVM addTicket(@RequestBody TicketVM ticket) {
		Passenger p = passengerRepo.findById(ticket.getPassenger().getId()).orElse(null);
		Train t = trainRepo.findById(ticket.getTrain().getId()).orElse(null);
		Station o = stationRepo.findById(new StationPK(ticket.getOrigin().getName(), ticket.getOrigin().getRoute())).orElse(null);
		Station d = stationRepo.findById(new StationPK(ticket.getDest().getName(), ticket.getDest().getRoute())).orElse(null);
		TrainDirection tDir = null;
		LocalDateTime time = null;

		try {
			tDir = ticket.getDirection();
		} catch(IllegalArgumentException e) {
			tDir = null;
		}

		try {
			time = ticket.getDeparture();
		} catch (DateTimeParseException e) {
			time = null;
		}

		if(!Stream.of(p, t, o, d, tDir, time).allMatch((x) -> x != null)) {
			String err = "Error creating ticket: \n";
			if(p == null) err += "\t- Passenger " + ticket.getPassenger().getId() + "does not exist\n";
			if(t == null) err += "\t- Train " + ticket.getTrain().getId() + "does not exist\n";
			if(o == null) err += "\t- Station" + ticket.getOrigin().getName() + " does not exist on route " + ticket.getOrigin().getRoute() + "\n";
			if(d == null) err += "\t- Station" + ticket.getDest().getName() + " does not exist on route " + ticket.getDest().getRoute() + "\n";
			if(tDir == null) err += "\t- Direction is invalid (must be INBOUND or OUTBOUND)";
			if(time == null) err += "\t- Could not parse time " + ticket.getDeparture() +"\n";

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, err);
		}

		return new TicketVM(ticketRepo.save(new Ticket(p, t, o, d, time, tDir)));
	}

	// read

	@GetMapping(path="/")
	public @ResponseBody Iterable<TicketVM> getTickets() {
		List<TicketVM> tickets = new ArrayList<>();
		for(Ticket t : ticketRepo.findAll()) {
			tickets.add(new TicketVM(t));
		}
		return tickets;
	}

	@GetMapping(path="/{id}")
	public @ResponseBody TicketVM getTicket(@PathVariable Long id) {
		Optional<Ticket> t = ticketRepo.findById(id);
		if(t.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No ticket found with ID " + id);
		return new TicketVM(t.get());
	}

	// update

	@PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody TicketVM updateTicket(@PathVariable Long id, @RequestBody TicketVM ticket) {
		Ticket tx = ticketRepo.findById(id).orElse(null);
		if(tx == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error updating ticket: No ticket found with ID " + id);

		Passenger p = ticket.getPassenger() == null ? tx.getPassenger() : passengerRepo.findById(ticket.getPassenger().getId()).orElse(null);
		Train t = ticket.getTrain() == null ? tx.getTrain() : trainRepo.findById(ticket.getTrain().getId()).orElse(null);
		Station o = ticket.getOrigin() == null ? tx.getOrigin() : stationRepo.findById(new StationPK(ticket.getOrigin().getName(), ticket.getOrigin().getRoute())).orElse(null);
		Station d = ticket.getDest() == null ? tx.getDest() : stationRepo.findById(new StationPK(ticket.getDest().getName(), ticket.getDest().getRoute())).orElse(null);
		TrainDirection tDir = null;
		LocalDateTime time = null;

		try {
			tDir = ticket.getDirection() == null ? tx.getDirection() : ticket.getDirection();
		} catch(IllegalArgumentException e) {
			tDir = null;
		}

		try {
			time = ticket.getDeparture() == null ? tx.getDeparture() : ticket.getDeparture();
		} catch (DateTimeParseException e) {
			time = null;
		}

		if(!Stream.of(p, t, o, d, tDir, time).allMatch((x) -> x != null)) {
			String err = "Error creating ticket: \n";
			if(p == null) err += "\t- Passenger " + ticket.getPassenger().getId() + "does not exist\n";
			if(t == null) err += "\t- Train " + ticket.getTrain().getId() + "does not exist\n";
			if(o == null) err += "\t- Station" + ticket.getOrigin().getName() + " does not exist on route " + ticket.getOrigin().getRoute() + "\n";
			if(d == null) err += "\t- Station" + ticket.getDest().getName() + " does not exist on route " + ticket.getDest().getRoute() + "\n";
			if(tDir == null) err += "\t- Direction is invalid (must be INBOUND or OUTBOUND)";
			if(time == null) err += "\t- Could not parse time " + ticket.getDeparture() +"\n";

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, err);
		}

		// special validation case: train route must eq. station routes
		if(!ticket.getTrain().getSchedule().getOrigin().getRoute().equals(ticket.getOrigin().getRoute())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error updating ticket: Train on route" + ticket.getTrain().getSchedule().getOrigin().getRoute() + " cannot be assigned to ticket with route " + ticket.getOrigin().getRoute());

		tx.setId(new TicketPK(p.getId(), t.getId()));
		tx.setOrigin(o);
		tx.setDest(d);
		tx.setDeparture(time);
		tx.setDirection(tDir);

		return new TicketVM(ticketRepo.save(tx));
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
