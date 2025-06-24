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
}
