package com.infy.rewardpoints.service;

import com.infy.rewardpoints.exception.RewardPointsException;
import com.infy.rewardpoints.models.CustomerDTO;

/**
 * Service interface for managing customer-related operations.
 */
public interface CustomerService {
	/**
	 * Saves a new customer or updates an existing one based on the provided DTO.
	 * @param customerDTO the customer data to save
	 * @return the ID of the saved customer
	 * @throws RewardPointsException if validation fails or saving fails
	 */
	public long saveCustomer(CustomerDTO customerDTO) throws RewardPointsException;

}
