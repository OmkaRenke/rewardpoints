package com.infy.rewardpoints.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * Entity representing a Customer in the rewards program. Each customer can have
 * multiple transactions and earns points based on their transaction history.
 */
@Data
@Entity
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long customerId;
	private String name;
	private String email;
	private String contact;
}
