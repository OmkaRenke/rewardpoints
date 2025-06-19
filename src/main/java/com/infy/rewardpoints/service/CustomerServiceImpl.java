package com.infy.rewardpoints.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infy.rewardpoints.dto.CustomerDTO;
import com.infy.rewardpoints.dto.TransactionDTO;
import com.infy.rewardpoints.repository.CustomerRepository;
import com.infy.rewardpoints.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@Service(value="customerService")
@Transactional
public class CustomerServiceImpl implements CustomerService {
	
	private ModelMapper modelMapper = new ModelMapper();
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Override
	public long registerCustomer(CustomerDTO customerDTO) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<TransactionDTO> transactionUnderCustomer(long customerId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
