package com.mmyron.db363;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Train {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return id.toString();
	}
}
