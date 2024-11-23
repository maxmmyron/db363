package com.mmyron.db363.entitiy;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
@Embeddable
public class Station {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String name;

	@ManyToOne
	@JoinColumn(name="trainroute_id")
	@Column(nullable = false)
	private String trainRoute;
	
	@OneToOne
	@Column(nullable = true)
	private Link inboundLink;
	
	@OneToOne
	@Column(nullable = true)
	private Link outboundLink;
	
	@Column(nullable = false)
	private Integer loadingTime;
	
	// getters & setters
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Link getInboundLink() {
		return inboundLink;
	}

	public void setInboundLink(Link inbound) {
		this.inboundLink = inbound;
	}
	
	public void setInboundStation(Station inbound, Integer duration, Integer distance) {
		Link l = new Link(this, inbound, duration, distance);
		this.inboundLink = l;
	}

	public Link getOutboundLink() {
		return outboundLink;
	}

	public void setOutboundLink(Link outbound) {
		this.outboundLink = outbound;
	}
	
	public void setOutboundStation(Station outbound, Integer duration, Integer distance) {
		Link l = new Link(this, outbound, duration, distance);
		this.outboundLink = l;
	}

	public Integer getLoadingTime() {
		return loadingTime;
	}

	public void setLoadingTime(Integer loadingTime) {
		this.loadingTime = loadingTime;
	}
}
