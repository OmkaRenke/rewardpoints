package com.infy.rewardpoints.service;

import java.util.List;

import com.infy.rewardpoints.dto.CustomerDTO;
import com.infy.rewardpoints.dto.TransactionDTO;

public interface CustomerService {
	
	public long registerCustomer( CustomerDTO customerDTO);
	
	public List<TransactionDTO> transactionUnderCustomer(long customerId);
}
