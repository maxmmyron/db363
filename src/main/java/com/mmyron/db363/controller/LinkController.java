package com.mmyron.db363.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.mmyron.db363.dto.LinkVM;
import com.mmyron.db363.entitiy.Link;
import com.mmyron.db363.entitiy.LinkPK;
import com.mmyron.db363.entitiy.StationPK;
import com.mmyron.db363.repo.LinkRepo;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(path="/api/links")
public class LinkController {
	// get auto-generated bean
	@Autowired
	private LinkRepo linkRepo;
	
	// read
	
	@GetMapping(path="/")
	public @ResponseBody Iterable<LinkVM> getLinks() {
		List<LinkVM> links = new ArrayList<>();
		for(Link l : linkRepo.findAll()) links.add(new LinkVM(l)); 
		return links;
	}
	
	@GetMapping(path="/{route}")
	public @ResponseBody LinkVM getLink(@PathVariable String route, @RequestParam(name = "origin") String origin, @RequestParam(name="dest") String dest) {
		StationPK o = new StationPK(origin, route);
		StationPK d = new StationPK(dest, route);
		Optional<Link> l = linkRepo.findById(new LinkPK(o, d));
		if(l.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Link does not exist between" + origin + " and " + dest + " on route " + route);
		return new LinkVM(l.get());
	}
}
