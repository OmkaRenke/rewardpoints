package com.infy.rewardpoints.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infy.rewardpoints.dto.TransactionDTO;
import com.infy.rewardpoints.repository.CustomerRepository;
import com.infy.rewardpoints.repository.RetailerRepository;
import com.infy.rewardpoints.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@Service(value="transactionService")
@Transactional
public class TransactionServiceImpl implements TransactionService {
	
private ModelMapper modelMapper = new ModelMapper();
	
	@Autowired
	private RetailerRepository retailerRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;


	@Override
	public List<TransactionDTO> getTransactionsByCustomerId(long customerId) {
		// TODO Auto-generated method stub
		return null;
	}

}
