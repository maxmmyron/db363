package com.mmyron.db363.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mmyron.db363.entitiy.Ticket;
import com.mmyron.db363.repo.TicketRepo;

@Controller
@RequestMapping(path="/api/tickets")
public class TicketController {
	// get auto-generated bean
	@Autowired
	private TicketRepo ticketRepo;
	
	@PostMapping(path="/add")
	public @ResponseBody String addTicket() {
		Ticket t = new Ticket();
		ticketRepo.save(t);
		
		return "Saved";
	}
	
	@GetMapping(path="/all")
	public @ResponseBody Iterable<Ticket> getTickets() {
		return ticketRepo.findAll();
	}
	
	@GetMapping(path="/get/{id}")
	public @ResponseBody String getTicket(@PathVariable Long id) {
		Optional<Ticket> t = ticketRepo.findById(id);
		if(t.isEmpty()) return "No ticket found";
		return t.get().toString();
	}
}
