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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.mmyron.db363.entitiy.Link;
import com.mmyron.db363.entitiy.LinkPK;
import com.mmyron.db363.entitiy.Station;
import com.mmyron.db363.entitiy.StationPK;
import com.mmyron.db363.repo.LinkRepo;
import com.mmyron.db363.repo.StationRepo;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(path="/api/links")
public class LinkController {
	// get auto-generated bean
	@Autowired
	private LinkRepo linkRepo;
	
	// read
	
	@GetMapping(path="/")
	public @ResponseBody Iterable<Link> getLinks() {
		return linkRepo.findAll();
	}
	
	@GetMapping(path="/{route}")
	public @ResponseBody Link getLink(@PathVariable String route, @RequestParam String origin, @RequestParam String dest) {
		StationPK o = new StationPK(origin, route);
		StationPK d = new StationPK(dest, route);
		Optional<Link> l = linkRepo.findById(new LinkPK(o, d));
		if(l.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Link does not exist between" + origin + " and " + dest + " on route " + route);
		return l.get();
	}
}
