package com.infy.rewardpoints.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.infy.rewardpoints.models.Transaction;

/**
 * Repository interface for accessing Transaction data. Provides methods to
 * retrieve transactions by customer and date range.
 */
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
	/**
	 * Retrieves all transactions for a given customer by their ID.
	 *
	 * @param customerId the ID of the customer
	 * @return list of transactions made by the customer
	 */
	public List<Transaction> findByCustomerCustomerId(long customerId);
	/**
	 * Retrieves all transactions for a customer within a specific date range.
	 * @param customerId the ID of the customer
	 * @param startDate  start of the date range
	 * @param endDate    end of the date range
	 * @return list of transactions in the specified date range
	 */
	List<Transaction> findByCustomerCustomerIdAndTransactionDateBetween(Long customerId, Timestamp startDate,
			Timestamp endDate);
}
