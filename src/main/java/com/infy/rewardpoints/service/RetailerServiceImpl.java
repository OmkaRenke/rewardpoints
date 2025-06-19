package com.infy.rewardpoints.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infy.rewardpoints.dto.CustomerDTO;
import com.infy.rewardpoints.dto.RetailerDTO;
import com.infy.rewardpoints.entity.Customer;
import com.infy.rewardpoints.exception.RewardPointsException;
import com.infy.rewardpoints.repository.CustomerRepository;
import com.infy.rewardpoints.repository.RetailerRepository;
import com.infy.rewardpoints.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@Service(value = "retailerService")
@Transactional
public class RetailerServiceImpl  implements RetailerService{
	
	private ModelMapper modelMapper = new ModelMapper();
	
	@Autowired
	private RetailerRepository retailerRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;

	@Override
	public long registerRetailer(RetailerDTO retailerDTO) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<CustomerDTO> viewCustomerDetails(long retailerId) throws RewardPointsException {
		List <Customer> customerList = customerRepository.findByRetailerRetailerId(retailerId);
		if(customerList.isEmpty()) {
			throw new RewardPointsException("Service.RETAILERS_CUSTOMER_NOT_FOUND");
		}
		List <CustomerDTO> custDTO = new ArrayList<>();
		
		customerList.stream().forEach(customer->{
			CustomerDTO customerDTO = modelMapper.map(customer, CustomerDTO.class);
			RetailerDTO retailerDTO = modelMapper.map(customer.getRetailer(), RetailerDTO.class);
			
			customerDTO.setRetailerDTO(retailerDTO);
			
			custDTO.add(customerDTO);
		});
		return custDTO;
	}

}
