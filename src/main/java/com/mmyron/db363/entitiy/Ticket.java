package com.mmyron.db363.entitiy;

import java.time.LocalDateTime;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Checks({
	// station name non-equivalence constraint
	@Check(constraints = "origin_name <> dest_name"),
	// station route equivalence constraint
	@Check(constraints = "origin_route = dest_route"),
})
@Getter
@Setter
@NoArgsConstructor
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
		@JoinColumn(name="origin_route", referencedColumnName = "train_route", columnDefinition = "VARCHAR(48)", nullable = false),
	})
	private Station origin;

	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="dest_name", referencedColumnName = "name", columnDefinition = "VARCHAR(64)", nullable = false),
		@JoinColumn(name="dest_route", referencedColumnName = "train_route", columnDefinition = "VARCHAR(48)", nullable = false),
	})
	private Station dest;

	@Column(nullable = false)
	private LocalDateTime departure;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TrainDirection direction;

	public Ticket(Passenger passenger, Train train, Station origin, Station dest, LocalDateTime departure, TrainDirection dir) {
		this.id = new TicketPK(passenger.getId(), train.getId());
		this.passenger = passenger;
		this.train = train;
		this.origin = origin;
		this.dest = dest;
		this.departure = departure;
		this.direction = dir;
	}
}
