package com.mmyron.db363.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mmyron.db363.entitiy.Train;
import com.mmyron.db363.repo.TrainRepo;

@Controller
@RequestMapping(path="/api/trains")
public class TrainController {
	// get auto-generated bean
	@Autowired
	private TrainRepo trainRepo;
	
	@PostMapping(path="/add")
	public @ResponseBody String addTrain() {
		Train t = new Train();
		trainRepo.save(t);
		
		return "Saved";
	}
	
	@GetMapping(path="/all")
	public @ResponseBody Iterable<Train> getTrains() {
		return trainRepo.findAll();
	}
	
	@GetMapping(path="/get/{id}")
	public @ResponseBody String getTrain(@PathVariable Long id) {
		Optional<Train> t = trainRepo.findById(id);
		if(t.isEmpty()) return "No train found";
		return t.get().toString();
	}
}
