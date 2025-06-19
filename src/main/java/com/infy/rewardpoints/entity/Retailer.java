package com.infy.rewardpoints.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity 
public class Retailer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long retailerId;
	
	private String name;
	private String email;
	private String contact;
	public long getRetailerId() {
		return retailerId;
	}
	public void setRetailerId(long retailerId) {
		this.retailerId = retailerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	
}
