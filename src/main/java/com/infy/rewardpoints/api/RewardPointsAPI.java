package com.infy.rewardpoints.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infy.rewardpoints.dto.CustomerDTO;
import com.infy.rewardpoints.exception.RewardPointsException;
import com.infy.rewardpoints.service.CustomerService;
import com.infy.rewardpoints.service.RetailerService;
import com.infy.rewardpoints.service.TransactionService;

@RestController
@RequestMapping("/rewardpoint")
@Validated
public class RewardPointsAPI {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired 
	private RetailerService retailerService;
	
	@Autowired 
	private TransactionService transactionService;
	
	
	// get All customers by retailer Id 
	@GetMapping(value = "/retailer/customers/{retailerId}")
	public ResponseEntity<List<CustomerDTO>> viewCustomers(@PathVariable long retailerId) throws RewardPointsException{
		List<CustomerDTO> customerDtos = retailerService.viewCustomerDetails(retailerId);
		
		return new ResponseEntity<>(customerDtos,HttpStatus.OK);
	}
}
