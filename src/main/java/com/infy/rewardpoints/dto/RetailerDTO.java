package com.infy.rewardpoints.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RetailerDTO {
	@NotNull(message= "{retailer.name.absent}")
	private String name;
	private String email;
	private String contact;
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
