/**
 * Represents a link between two stations.
 * A Link is a weak entity whose primary key is composed of two station IDs.
 * This ensures that any two pairs of stations only have a single Link, rather
 * than an inbound and outbound Link object.
 *
 * Because a Link is a weak entity set, we implement the Serializable class
 * to override the equals() and hashCode() functions (i.e. we tell JPA how to
 * tell any two Links apart from one another)
 *
 * @author maxmm
 *
 */

package com.mmyron.db363.entitiy;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Check;
import org.hibernate.annotations.Checks;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = {
	@UniqueConstraint(
		columnNames = {
			"origin_name", "origin_route",
			"dest_name", "dest_route"
		}
	)
})
@Checks({
	// station name non-equivalence constraint
	@Check(constraints = "origin_name <> dest_name"),
	// station route equivalence constraint
	@Check(constraints = "origin_route = dest_route"),
})
public class Link {
	@EmbeddedId
	private LinkPK id;

	@ManyToOne
	@MapsId("origin")
	@JoinColumns({
		@JoinColumn(name="origin_name", referencedColumnName = "name", columnDefinition = "VARCHAR(64)", nullable = false),
		@JoinColumn(name="origin_route", referencedColumnName = "train_route", columnDefinition = "VARCHAR(48)", nullable = false),
	})
	private Station originStation;

	@ManyToOne
	@MapsId("dest")
	@JoinColumns({
		@JoinColumn(name="dest_name", referencedColumnName = "name", columnDefinition = "VARCHAR(64)", nullable = false),
		@JoinColumn(name="dest_route", referencedColumnName = "train_route", columnDefinition = "VARCHAR(48)", nullable = false),
	})
	private Station destStation;

	@Check(constraints = "duration > 0")
	@Column(nullable = false)
	private Integer duration;

	@Check(constraints = "distance > 0")
	@Column(nullable = false)
	private Integer distance;

	@OneToMany(mappedBy = "link")
	@JsonIgnore
	private Set<Train> trains = new HashSet<>();
	
	public Link() {}

	public Link(StationPK origin, StationPK dest, Integer duration, Integer distance) {
		id = new LinkPK(origin, dest);
		this.duration = duration;
		this.distance = distance;
	}

	// getters & setters

	public LinkPK getId() {
		return id;
	}
	
	public void setId(LinkPK linkPK) {
		this.id = linkPK;
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
	
	public Integer getDuration() {
		return duration;
	}
	
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	
	public Integer getDistance() {
		return distance;
	}
	
	public void setDistance(Integer distance) {
		this.distance = distance;
	}
}
