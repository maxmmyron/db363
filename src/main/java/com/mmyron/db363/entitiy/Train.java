package com.mmyron.db363.entitiy;

import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Check;
import org.hibernate.annotations.Checks;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="station_name", referencedColumnName = "name", nullable = true),
		@JoinColumn(name="station_route", referencedColumnName = "train_route", nullable = true)
	})
	private Station station;

	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="link_origin_name", referencedColumnName = "origin_name", columnDefinition = "VARCHAR(64)"),
		@JoinColumn(name="link_origin_route", referencedColumnName = "origin_route", columnDefinition = "VARCHAR(48)"),
		@JoinColumn(name="link_dest_name", referencedColumnName = "dest_name", columnDefinition = "VARCHAR(64)"),
		@JoinColumn(name="link_dest_route", referencedColumnName = "dest_route", columnDefinition = "VARCHAR(48)")
	})
	private Link link;

	@Column(name="station_departure")
	private Time stationDep = null;

	@Column(name="station_arrival")
	private Time stationArrival = null;

	@ManyToOne
	@JoinColumn(name="schedule_id", referencedColumnName = "id", nullable = true)
	private Schedule schedule = null;

	@Column(name="schedule_departure")
	private Time schedDep;

	@Column(name="train_status", nullable = false)
	private String status;

	@OneToMany(mappedBy = "train")
	@JsonIgnore
	private Set<Ticket> tickets = new HashSet<>();

	public Train() {}

	public Train(Station station, String trainStatus) {
		this.station = station;
		this.status = trainStatus;
	}

	public Train(Station station, String trainStatus, Schedule schedule) {
		this.station = station;
		this.status = trainStatus;
		this.schedule = schedule;
	}

	// getters & setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public void setStatus(String trainStatus) {
		this.status = trainStatus;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}
	
	public Link getLink() {
		return link;
	}

	public void setLink(Link link) {
		this.link = link;
	}

	public Time getStationDep() {
		return stationDep;
	}

	public void setStationDep(Time stationDep) {
		this.stationDep = stationDep;
	}

	public Time getStationArrival() {
		return stationArrival;
	}

	public void setStationArrival(Time stationArrival) {
		this.stationArrival = stationArrival;
	}

	public Time getSchedDep() {
		return schedDep;
	}

	public void setSchedDep(Time schedDep) {
		this.schedDep = schedDep;
	}

	// overrides

	@Override
	public String toString() {
		return id.toString();
	}
}
