package com.mmyron.db363.entitiy;

import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Check;
import org.hibernate.annotations.Checks;

import com.mmyron.db363.util.TrainDirection;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Train {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 48)
	private String trainRoute;

	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="station_name", referencedColumnName = "name", nullable = true),
		@JoinColumn(name="station_route", referencedColumnName = "trainRoute", nullable = true),
	})
	private Station station;
	
	private Time departure; 

	@Column(nullable = false)
	private String trainStatus;
	
	@ManyToOne
	private Schedule schedule = null;
	
	@OneToMany(mappedBy = "train")
	private Set<Ticket> tickets = new HashSet<>();

	public Train() {}

	public Train(String route, Station station, String trainStatus) {
		this.trainRoute = route;
		this.station = station;
		this.trainStatus = trainStatus;
	}
	
	public Train(String route, Station station, String trainStatus, Schedule schedule) {
		this.trainRoute = route;
		this.station = station;
		this.trainStatus = trainStatus;
		this.schedule = schedule;
	}

	// getters & setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTrainRoute() {
		return trainRoute;
	}

	public void setTrainRoute(String trainRoute) {
		this.trainRoute = trainRoute;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public String getStatus() {
		return trainStatus;
	}

	public void setStatus(String trainStatus) {
		this.trainStatus = trainStatus;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}
	
	// overrides

	@Override
	public String toString() {
		return id.toString();
	}
}
