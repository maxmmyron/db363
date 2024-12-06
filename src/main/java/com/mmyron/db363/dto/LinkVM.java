package com.mmyron.db363.dto;

import com.mmyron.db363.entitiy.Link;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LinkVM {
	private String origin;
	
	private String dest;
	
	private String route;
	
	private Integer duration;
	
	private Integer distance;
	
	public LinkVM(Link link) {
		origin = link.getOrigin().getId().getName();
		dest = link.getDest().getId().getName();
		route = link.getOrigin().getId().getRoute();
		duration = link.getDuration();
		distance = link.getDistance();
	}
}
