package com.mmyron.db363.entitiy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;

@Entity
public class Train {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 48)
	private String trainRoute;

	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="station_name", referencedColumnName = "name", columnDefinition = "VARCHAR(64)"),
		@JoinColumn(name="station_route", referencedColumnName = "trainRoute", columnDefinition = "VARCHAR(48)"),
	})
	private Station station;

	@Column(nullable = false)
	private String trainStatus;

	public Train() {}

	public Train(String route, Station station, String trainStatus) {
		this.trainRoute = route;
		this.station = station;
		this.trainStatus = trainStatus;
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

	// overrides

	@Override
	public String toString() {
		return id.toString();
	}
}
