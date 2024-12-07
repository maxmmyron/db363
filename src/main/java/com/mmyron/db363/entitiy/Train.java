package com.mmyron.db363.entitiy;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
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
	private LocalDateTime stationDep = null;

	@Column(name="station_arrival")
	private LocalDateTime stationArrival = null;

	@ManyToOne
	@JoinColumn(name="schedule_id", referencedColumnName = "id", nullable = true)
	private Schedule schedule = null;

	@Column(name="schedule_departure")
	private LocalDateTime schedDep;

	@Column(name="train_status", nullable = false)
	private String status;

	@OneToMany(mappedBy = "train")
	@JsonIgnore
	private Set<Ticket> tickets = new HashSet<>();

	public Train(Station station, String trainStatus) {
		this.station = station;
		this.status = trainStatus;
	}

	public Train(Station station, String trainStatus, Schedule schedule) {
		this.station = station;
		this.status = trainStatus;
		this.schedule = schedule;
	}
	
	@Override
	public String toString() {
		String st;
		try { st = station.toString(); } catch (NullPointerException e) { st = null; }
		return "id: " + id + "; station: "+ st + "; link: " + link.toString() + ";" ;
	}
}
