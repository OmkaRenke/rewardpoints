package com.infy.rewardpoints.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.infy.rewardpoints.entity.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
	
	public List<Transaction> findByCustomerCustomerId(long customerId);
}
