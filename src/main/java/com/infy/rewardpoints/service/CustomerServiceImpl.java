package com.infy.rewardpoints.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infy.rewardpoints.exception.RewardPointsException;
import com.infy.rewardpoints.models.Customer;
import com.infy.rewardpoints.models.CustomerDTO;
import com.infy.rewardpoints.repository.CustomerRepository;

import jakarta.transaction.Transactional;

/**
 * Implementation of CustomerService that handles customer registration and
 * persistence logic.
 */
@Service(value = "customerService")
@Transactional
public class CustomerServiceImpl implements CustomerService {
	private ModelMapper modelMapper = new ModelMapper();
	@Autowired
	private CustomerRepository customerRepository;

	/**
	 * Saves a new customer to the system if the email doesn't already exist.
	 *
	 * @param customerDTO the DTO containing customer details
	 * @return the ID of the saved customer
	 * @throws RewardPointsException if a customer with the same email already
	 *                               exists
	 */
	@Override
	public long addCustomer(CustomerDTO customerDTO) throws RewardPointsException {
		Optional<Customer> existingCustomer = customerRepository.findByEmail(customerDTO.getEmail());
		if (existingCustomer.isPresent()) {
			throw new RewardPointsException("SERVICE.CUSTOMER.EXISTS");
		}
		Customer customer = modelMapper.map(customerDTO, Customer.class);
		return customerRepository.save(customer).getCustomerId();
	}
}
