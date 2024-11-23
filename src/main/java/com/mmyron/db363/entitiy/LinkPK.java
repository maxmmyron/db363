/**
 * Link primary key class. Tells JPA how to tell any two links apart from one another.
 */

package com.mmyron.db363.entitiy;

import java.io.Serializable;
import java.util.Objects;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class LinkPK implements Serializable {
	@Column(nullable = false)
	private StationPK origin;
	@Column(nullable = false)
	private StationPK dest;
	
	public LinkPK() {
		super();
	}
	
	public LinkPK(StationPK origin, StationPK dest) {
		super();
		this.origin = origin;
		this.dest = dest;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof LinkPK linkPK)) return false;
		return Objects.equals(origin, linkPK.origin) && Objects.equals(dest, linkPK.dest);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(origin, dest);
	}
}
