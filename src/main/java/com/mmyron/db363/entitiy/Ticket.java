package com.mmyron.db363.entitiy;

import java.sql.Date;

import com.mmyron.db363.util.TrainDirection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Ticket {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="passenger_id")
	@Column(nullable = false)
	private Passenger passenger;
	
	@ManyToOne
	@JoinColumn(name="train_id")
	@Column(nullable = false)
	private Train train;
	
	@ManyToOne
	@JoinColumn(name="source_id")
	@Column(nullable = false)
	private Station source;
	
	@ManyToOne
	@JoinColumn(name="dest_id")
	@Column(nullable = false)
	private Station dest;
	
	@Column(nullable = false)
	private Date departure;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TrainDirection direction;
}
