package com.mmyron.db363.entitiy;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
@Embeddable
public class Station {
	// composite primary key
	@EmbeddedId
	private StationPK id;

	@OneToMany(mappedBy="originStation")
	private Set<Link> inboundLinks = new HashSet<>();

	@OneToMany(mappedBy="destStation")
	private Set<Link> outboundLinks = new HashSet<>();
	
	@Column(name="loading_time", nullable = false)
	private Integer loadingTime;
	
	public Station () {}

	// getters & setters

	public StationPK getId() {
		return id;
	}

	public void setId(StationPK id) {
		this.id = id;
	}

	public Integer getLoadingTime() {
		return loadingTime;
	}

	public void setLoadingTime(Integer loadingTime) {
		this.loadingTime = loadingTime;
	}
}
