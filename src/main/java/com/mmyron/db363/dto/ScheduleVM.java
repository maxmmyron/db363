package com.mmyron.db363.dto;

import com.mmyron.db363.entitiy.Schedule;
import com.mmyron.db363.util.TrainDirection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleVM {
	private Long id;
	
	private StationVM origin;
	
	private StationVM dest;
	
	private TrainDirection direction;
	
	public ScheduleVM(Schedule s) {
		this.id = s.getId();
		this.origin = new StationVM(s.getOrigin());
		this.dest = new StationVM(s.getDest());
		this.direction = s.getDirection();
	}
}
