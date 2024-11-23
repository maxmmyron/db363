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
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;

@Entity
public class Ticket {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "passenger_id")
	private Passenger passenger;
	
	@ManyToOne
	@JoinColumn(name = "train_id")
	private Train train;
	
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="origin_name", referencedColumnName = "name", columnDefinition = "VARCHAR(64)"),
		@JoinColumn(name="origin_route", referencedColumnName = "trainRoute", columnDefinition = "VARCHAR(48)"),
	})
	private Station origin;
	
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="dest_name", referencedColumnName = "name", columnDefinition = "VARCHAR(64)"),
		@JoinColumn(name="dest_route", referencedColumnName = "trainRoute", columnDefinition = "VARCHAR(48)"),
	})
	private Station dest;
	
	@Column(nullable = false)
	private Date departure;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TrainDirection direction;
}
