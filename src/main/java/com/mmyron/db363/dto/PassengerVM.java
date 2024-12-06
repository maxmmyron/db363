package com.mmyron.db363.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.NonNull;

import com.mmyron.db363.entitiy.Passenger;
import com.mmyron.db363.entitiy.Ticket;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PassengerVM {
	private Long id;
	
	@NonNull
	private String firstName;
	
	@NonNull
	private String lastName;
	
	public PassengerVM(Passenger p) {
		id = p.getId();
		firstName = p.getFirstName();
		lastName = p.getLastName();
	}
}
