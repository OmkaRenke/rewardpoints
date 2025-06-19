package com.infy.rewardpoints.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.infy.rewardpoints.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
	
	List<Customer> findByRetailerRetailerId(long retailerId);
}
