package com.infy.rewardpoints.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * Data Transfer Object for Customer.
 */
@Data
public class CustomerDTO {
	@NotNull(message = "customer.name.absent")
	private String name;
	private long customerId;
	/**
	 * Email address of the customer. Must be a valid email format.
	 */
	@Email(message = "customer.email.invalid")
	@NotNull(message = "customer.email.absent")
	private String email;
	/**
	 * Contact number of the customer. Must be exactly 10 digits (numeric only).
	 */
	@Pattern(regexp = "[0-9]{10}", message = "customer.contact.invalid")
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

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
}
