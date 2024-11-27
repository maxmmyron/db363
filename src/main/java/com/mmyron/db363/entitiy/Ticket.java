package com.mmyron.db363.entitiy;

import java.sql.Date;

import org.hibernate.annotations.Check;
import org.hibernate.annotations.Checks;

import com.mmyron.db363.util.TrainDirection;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity
@Checks({
	// station name non-equivalence constraint
	@Check(constraints = "origin_name <> dest_name"),
	// station route equivalence constraint
	@Check(constraints = "origin_route = dest_route"),
})
public class Ticket {
	@EmbeddedId
	private TicketPK id;
	
	@ManyToOne
	@MapsId("passengerId")
	private Passenger passenger;
	
	@ManyToOne
	@MapsId("trainId")
	private Train train;

	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="origin_name", referencedColumnName = "name", columnDefinition = "VARCHAR(64)", nullable = false),
		@JoinColumn(name="origin_route", referencedColumnName = "trainRoute", columnDefinition = "VARCHAR(48)", nullable = false),
	})
	private Station origin;

	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="dest_name", referencedColumnName = "name", columnDefinition = "VARCHAR(64)", nullable = false),
		@JoinColumn(name="dest_route", referencedColumnName = "trainRoute", columnDefinition = "VARCHAR(48)", nullable = false),
	})
	private Station dest;

	@Column(nullable = false)
	private Date departure;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TrainDirection direction;

	public Ticket() {}

	public Ticket(TicketPK id, Station origin, Station dest, Date departure, TrainDirection dir) {
		this.id = id;
		this.origin = origin;
		this.dest = dest;
		this.departure = departure;
		this.direction = dir;
	}

	// getters & setters

	public TicketPK getId() {
		return id;
	}
	
	public void setId(TicketPK id) {
		this.id = id;
	}

	public Station getOrigin() {
		return origin;
	}

	public void setOrigin(Station origin) {
		this.origin = origin;
	}

	public Station getDest() {
		return dest;
	}

	public void setDest(Station dest) {
		this.dest = dest;
	}

	public Date getDeparture() {
		return departure;
	}

	public void setDeparture(Date departure) {
		this.departure = departure;
	}

	public TrainDirection getDirection() {
		return direction;
	}

	public void setDirection(TrainDirection direction) {
		this.direction = direction;
	}
}
