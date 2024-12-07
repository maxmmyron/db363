package com.mmyron.db363.dto;

import com.mmyron.db363.entitiy.Station;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StationVM {
	private String name;
	
	private String route;
	
	private Integer loadingTime;
	
	public StationVM(Station s) {
		name = s.getId().getName();
		route = s.getId().getRoute();
		loadingTime = s.getLoadingTime();
	}
	
	@Override
	public String toString() {
		return "(" + name + ", " + route + ", " + loadingTime + "m)";
	}
}
