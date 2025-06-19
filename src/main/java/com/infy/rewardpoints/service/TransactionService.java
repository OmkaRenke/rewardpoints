package com.infy.rewardpoints.service;

import java.util.List;

import com.infy.rewardpoints.dto.TransactionDTO;

public interface TransactionService  {
	
	public List<TransactionDTO> getTransactionsByCustomerId(long customerId);
}
