package com.mmyron.db363.entitiy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Train {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="trainroute_id")
	@Column(nullable = false)
	private String trainRoute;
	
	@ManyToOne
	@JoinColumn(name="station_id")
	@Column(nullable = false)
	private Station station;

	@Column(nullable = false)
	private String status;
	
	public Train() {}
	
	public Train(String route, Station station, String status) {
		this.trainRoute = route;
		this.station = station;
		this.status = status;
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
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	// overrides

	@Override
	public String toString() {
		return id.toString();
	}
}
