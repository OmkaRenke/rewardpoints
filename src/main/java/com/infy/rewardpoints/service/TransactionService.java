package com.infy.rewardpoints.service;

import java.util.List;

import com.infy.rewardpoints.dto.CustomerRewardSummaryDTO;
import com.infy.rewardpoints.dto.TransactionDTO;
import com.infy.rewardpoints.exception.RewardPointsException;

public interface TransactionService  {
	
	public List<TransactionDTO> getTransactionsByCustomerId(long customerId) throws RewardPointsException;

	public long saveTransaction(TransactionDTO transactionDTO) throws RewardPointsException;
	
	public CustomerRewardSummaryDTO getCustomerRewardsLast3Months(long customerId,
		    String startDate,
		    String endDate) throws RewardPointsException;

	public List<CustomerRewardSummaryDTO> getAllCustomerRewardsByRetailerId( long retailerId,String startDate,
		    String endDate) throws RewardPointsException ;
}
