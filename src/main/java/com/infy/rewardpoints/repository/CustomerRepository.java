package com.infy.rewardpoints.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.infy.rewardpoints.models.Customer;

/**
 * Repository interface for accessing Customer data from the database.
 */
public interface CustomerRepository extends CrudRepository<Customer, Long> {
	/**
	 * Finds a customer by email address.
	 * @param email the email of the customer to search for
	 * @return an Optional containing the customer, if found
	 */
	Optional<Customer> findByEmail(String email);
}
