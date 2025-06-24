package com.infy.rewardpoints.service;

import java.util.List;

import com.infy.rewardpoints.exception.RewardPointsException;
import com.infy.rewardpoints.models.CustomerRewardSummaryMapper;
import com.infy.rewardpoints.models.TransactionDTO;
import com.infy.rewardpoints.models.TransactionResponseMapper;

/**
 * Service interface for handling transaction-related operations, including
 * saving transactions and calculating customer reward points.
 */
public interface TransactionService {
	/**
	 * Retrieves all transactions for a given customer.
	 * @param customerId the ID of the customer
	 * @return list of transactions made by the customer
	 * @throws RewardPointsException if the customer is not found or has no
	 *                               transactions
	 */
	public List<TransactionResponseMapper> getTransactionsByCustomerId(long customerId) throws RewardPointsException;
	/**
	 * Saves a transaction and calculates reward points based on the amount.
	 *
	 * @param transactionDTO the DTO containing transaction data
	 * @return the saved transaction ID
	 * @throws RewardPointsException if the customer is not found or data is invalid
	 */
	public long saveTransaction(TransactionDTO transactionDTO) throws RewardPointsException;
	/**
	 * Calculates the reward summary for a customer over the last 3 months, grouping
	 * rewards by month and computing total reward points.
	 * @param customerId the ID of the customer
	 * @param startDate  start date in "yyyy-MM-dd" format
	 * @param endDate    end date in "yyyy-MM-dd" format
	 * @return the monthly and total reward points summary
	 * @throws RewardPointsException if the customer or data is invalid
	 */
	public CustomerRewardSummaryMapper getCustomerRewardsLast3Months(long customerId, String startDate, String endDate)
			throws RewardPointsException;
}
