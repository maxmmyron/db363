package com.mmyron.db363.entitiy;

import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Check;
import org.hibernate.annotations.Checks;

import com.mmyron.db363.util.TrainDirection;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = {
	@UniqueConstraint(
		columnNames = {
			"origin_name", "origin_route",
			"dest_name", "dest_route",
		}
	)
})
@Checks({
	// station name non-equivalence constraint
	@Check(constraints = "origin_name <> dest_name"),
	// station route equivalence constraint
	@Check(constraints = "origin_route = dest_route"),
})
public class Schedule {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="origin_name", referencedColumnName = "name", columnDefinition = "VARCHAR(64)", nullable = false),
		@JoinColumn(name="origin_route", referencedColumnName = "train_route", columnDefinition = "VARCHAR(48)", nullable = false),
	})
	private Station originStation;

	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="dest_name", referencedColumnName = "name", columnDefinition = "VARCHAR(64)", nullable = false),
		@JoinColumn(name="dest_route", referencedColumnName = "train_route", columnDefinition = "VARCHAR(48)", nullable = false),
	})
	private Station destStation;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private TrainDirection direction;

	@OneToMany(mappedBy="schedule")
	private Set<Train> trains = new HashSet<>();

	public Schedule() {}

	public Schedule(Station origin, Station dest, TrainDirection dir) {
		this.originStation = origin;
		this.destStation = dest;
		this.direction = dir;
	}

	// getters & setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Station getOriginStation() {
		return originStation;
	}

	public void setOriginStation(Station originStation) {
		this.originStation = originStation;
	}

	public Station getDestStation() {
		return destStation;
	}

	public void setDestStation(Station destStation) {
		this.destStation = destStation;
	}
	
	public TrainDirection getDirection() {
		return direction;
	}
	
	public void setDirection(TrainDirection dir) {
		this.direction = dir;
	}

	public Set<Train> getTrains() {
		return trains;
	}

	public void setTrains(Set<Train> trains) {
		this.trains = trains;
	}
}
