package com.infy.rewardpoints.repository;

import org.springframework.data.repository.CrudRepository;

import com.infy.rewardpoints.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
	
		
}
