package com.infy.rewardpoints.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.infy.rewardpoints.entity.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
	
	public List<Transaction> findByCustomerCustomerId(long customerId);
	public List<Transaction> findByCustomerRetailerRetailerId(long retailerId);
	
	List<Transaction> findByCustomerCustomerIdAndTransactionDateBetween(
	        Long customerId,
	        Timestamp startDate,
	        Timestamp endDate);

}
