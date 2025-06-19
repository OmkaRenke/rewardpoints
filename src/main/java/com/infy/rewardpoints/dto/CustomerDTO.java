package com.infy.rewardpoints.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerDTO {
	@NotNull(message= "{customer.name.absent}")
	private String name;
	private long customerId;
	private String email;
	private String contact;
	private RetailerDTO retailerDTO;
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
	public RetailerDTO getRetailerDTO() {
		return retailerDTO;
	}
	public void setRetailerDTO(RetailerDTO retailerDTO) {
		this.retailerDTO = retailerDTO;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	
	
}
