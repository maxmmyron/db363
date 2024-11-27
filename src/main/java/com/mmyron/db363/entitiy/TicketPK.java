package com.mmyron.db363.entitiy;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;

@Embeddable
public class TicketPK implements Serializable {
	@Column(name = "passenger_id", nullable = false)
	private Long passengerId;
	
	@Column(name = "train_id", nullable = false)
	private Long trainId;
	
	public TicketPK () {}
	
	public TicketPK (Long passengerId, Long trainId) {
		super();
		this.passengerId = passengerId;
		this.trainId = trainId;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TicketPK ticketPK)) return false;
		return Objects.equals(passengerId, ticketPK.passengerId) && Objects.equals(trainId, ticketPK.trainId);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(passengerId, trainId);
	}
	
	// getters & setters
	
	public Long getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(Long id) {
		this.passengerId = id;
	}

	public Long getTrainId() {
		return trainId;
	}

	public void setTrainId(Long id) {
		this.trainId = id;
	}
}
