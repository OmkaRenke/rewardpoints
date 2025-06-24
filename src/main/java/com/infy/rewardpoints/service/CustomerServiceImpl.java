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

	
	@Override
	public long registerCustomer(CustomerDTO customerDTO) {
		
		return 0;
	}

	
	
}
