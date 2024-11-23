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

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

@Entity
public class Link {
	@EmbeddedId
	private LinkPK id;
	
	@Column(nullable = false)
	private Integer duration;
	@Column(nullable = false)
	private Integer distance;
	
	public Link(Station origin, Station terminus, Integer duration, Integer distance) {
		id = new LinkPK(origin, terminus);
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
