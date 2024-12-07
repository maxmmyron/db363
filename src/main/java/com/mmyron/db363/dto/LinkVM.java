package com.mmyron.db363.dto;

import com.mmyron.db363.entitiy.Link;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LinkVM {
	private StationVM origin;
	
	private StationVM dest;
	
	private Integer duration;
	
	private Integer distance;
	
	public LinkVM(Link link) {
		origin = new StationVM(link.getOrigin());
		dest = new StationVM(link.getDest());
		duration = link.getDuration();
		distance = link.getDistance();
	}
	
	@Override
	public String toString() {
		String originSt;
		try { originSt = origin.toString(); } catch (NullPointerException e) { originSt = null; }
		
		String destSt;
		try { destSt = dest.toString(); } catch (NullPointerException e) { destSt = null; }
		return "{ origin: " + originSt + ", dest: " + destSt + ", " + duration + "m , " + distance + "km }";
	}
}
