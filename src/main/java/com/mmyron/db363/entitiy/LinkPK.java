/**
 * Link primary key class. Tells JPA how to tell any two links apart from one another.
 */

package com.mmyron.db363.entitiy;

import java.io.Serializable;
import java.util.Objects;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class LinkPK implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name="origin_id")
	@Column(nullable = false)
	private Station origin;
	
	@ManyToOne
	@JoinColumn(name="terminus_id")
	@Column(nullable = false)
	private Station terminus;
	
	public LinkPK(Station origin, Station terminus) {
		super();
		this.origin = origin;
		this.terminus = terminus;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof LinkPK linkPK)) return false;
		return Objects.equals(origin, linkPK.origin) && Objects.equals(terminus, linkPK.terminus);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(origin, terminus);
	}
}
