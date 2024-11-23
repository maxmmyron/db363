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
	@Column(length = 48)
	private String trainRoute;
	
	public StationPK() {
		super();
	}

	public StationPK(String name, String trainRoute) {
		super();
		this.name = name;
		this.trainRoute = trainRoute;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof StationPK stationPK)) return false;
		return Objects.equals(name, stationPK.name) && Objects.equals(trainRoute, stationPK.trainRoute);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name, trainRoute);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTrainRoute() {
		return trainRoute;
	}

	public void setTrainRoute(String trainRoute) {
		this.trainRoute = trainRoute;
	}
}
