package com.mmyron.db363.dto;

import java.time.LocalDateTime;

import com.mmyron.db363.entitiy.Ticket;
import com.mmyron.db363.util.TrainDirection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TicketVM {
	private PassengerVM passenger;
	
	private TrainVM train;
	
	private StationVM origin;
	
	private StationVM dest;
	
	private LocalDateTime departure;
	
	private TrainDirection direction;
	
	public TicketVM(Ticket t) {
		passenger = new PassengerVM(t.getPassenger());
		train = new TrainVM(t.getTrain());
		origin = new StationVM(t.getOrigin());
		dest = new StationVM(t.getDest());
		departure = t.getDeparture();
		direction = t.getDirection();
	}
}
