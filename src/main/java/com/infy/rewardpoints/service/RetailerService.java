package com.infy.rewardpoints.service;

import java.util.List;

import com.infy.rewardpoints.dto.CustomerDTO;
import com.infy.rewardpoints.dto.RetailerDTO;
import com.infy.rewardpoints.dto.TransactionDTO;
import com.infy.rewardpoints.exception.RewardPointsException;

public interface RetailerService {
	
	public long registerRetailer(RetailerDTO retailerDTO);
	
	public List<CustomerDTO> viewCustomerDetails(long retailerId) throws RewardPointsException;
	
	public List<TransactionDTO> transactionUnderRetailer(long retailerId) throws RewardPointsException;
}
