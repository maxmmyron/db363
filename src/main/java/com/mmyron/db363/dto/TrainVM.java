package com.mmyron.db363.dto;

import java.time.LocalDateTime;

import org.springframework.lang.Nullable;

import com.mmyron.db363.entitiy.Schedule;
import com.mmyron.db363.entitiy.Train;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrainVM {
	private Long id;
	
	private StationVM station;
	
	private LinkVM link;
	
	private LocalDateTime stationDep;
	
	private LocalDateTime stationArrival;
	
	private ScheduleVM schedule;

	private LocalDateTime schedDep;

	private String status;
	
	public TrainVM(Train t) {
		id = t.getId();
		station = t.getStation() == null ? null : new StationVM(t.getStation());
		link = new LinkVM(t.getLink());
		stationDep = t.getStationDep();
		stationArrival = t.getStationArrival();
		schedule = t.getSchedule() == null ? null : new ScheduleVM(t.getSchedule());
		schedDep = t.getSchedDep();
		status = t.getStatus();
	}
	
	@Override
	public String toString() {
		String st;
		try { st = station.toString(); } catch (NullPointerException e) { st = null; }
		return "id: " + id + "; station: "+ st + "; link: " + link.toString() + ";" ;
	}
}
