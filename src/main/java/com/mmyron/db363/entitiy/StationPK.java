package com.mmyron.db363.entitiy;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class StationPK implements Serializable {
	@Column(length = 64)
	private String name;
	@Column(name="train_route", length = 48)
	private String route;
	
	public StationPK() {}

	public StationPK(String name, String route) {
		this.name = name;
		this.route = route;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof StationPK stationPK)) return false;
		return Objects.equals(name, stationPK.name) && Objects.equals(route, stationPK.route);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name, route);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}
}
