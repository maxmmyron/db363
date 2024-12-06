package com.mmyron.db363.entitiy;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class Station {
	// composite primary key
	@EmbeddedId
	private StationPK id;

	@OneToMany(mappedBy="origin")
	@JsonIgnore
	private Set<Link> inboundLinks = new HashSet<>();

	@OneToMany(mappedBy="dest")
	@JsonIgnore
	private Set<Link> outboundLinks = new HashSet<>();
	
	@Column(name="loading_time", nullable = false)
	private Integer loadingTime;
}
