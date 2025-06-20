package com.infy.rewardpoints.api;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.infy.rewardpoints.dto.CustomerDTO;
import com.infy.rewardpoints.dto.CustomerRewardSummaryDTO;
import com.infy.rewardpoints.dto.TransactionDTO;
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
	
	@Autowired
	private Environment environment;
	
	// get All customers by retailer Id 
	@GetMapping(value = "/retailer/customers/{retailerId}")
	public ResponseEntity<List<CustomerDTO>> viewCustomers(@PathVariable long retailerId) throws RewardPointsException{
		List<CustomerDTO> customerDtos = retailerService.viewCustomerDetails(retailerId);
		
		return new ResponseEntity<>(customerDtos,HttpStatus.OK);
	}
	
	// get All transaction done by customers under provided retailer ID
	@GetMapping(value = "/retailer/custransactions/{retailerId}")
	public ResponseEntity<List<TransactionDTO>> viewCustomerTransactions(@PathVariable long retailerId) throws RewardPointsException{
		List<TransactionDTO> transactionDtos = retailerService.transactionUnderRetailer(retailerId);
		
		return new ResponseEntity<>(transactionDtos,HttpStatus.OK);
	}
	
	// save transaction with points calculation based on $ amount
	@PostMapping(value ="/save/transaction")
	public ResponseEntity<String> saveTransaction(@RequestBody TransactionDTO transactionDTO) throws RewardPointsException{
		long transactionId = transactionService.saveTransaction(transactionDTO);
		String message = environment.getProperty("API.TRANSACTION_SAVE_SUCCESS") + ":"+ transactionId;
		
		return new ResponseEntity<>(message,HttpStatus.CREATED);
	}
	
	// get All Transactions of Customer by Customer Id 
	@GetMapping(value = "customer/{customerId}")
	public ResponseEntity<List<TransactionDTO>> transactionsByCustomer(@PathVariable long customerId) throws RewardPointsException{
		List<TransactionDTO> transactionDtos = transactionService.getTransactionsByCustomerId(customerId);
		
		return new ResponseEntity<>(transactionDtos,HttpStatus.OK);
	}
	
	//http://localhost:8080/rewardpoint/customer/summary?customerId=200&startDate=2025-04-01&endDate=2025-06-30
	@GetMapping("/customer/summary")
	public ResponseEntity<CustomerRewardSummaryDTO> getAllCustomerRewardSummaries(
			@RequestParam long customerId,
            @RequestParam String startDate,
            @RequestParam String endDate
			) throws RewardPointsException {
		
		LocalDate start;
	    LocalDate end;
	    try {
	        start = LocalDate.parse(startDate);
	        end = LocalDate.parse(endDate);
	    } catch (DateTimeParseException ex) {
	        throw new RewardPointsException("Service.INVALID_DATE_FORMAT");
	    }

	    if (start.isAfter(end)) {
	        throw new RewardPointsException("Service.START_DATE_AFTER_END");
	    }
		
		
		
		
	 CustomerRewardSummaryDTO response = transactionService.getCustomerRewardsLast3Months(customerId,startDate,endDate);
	    return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	// get all customers and there transactions by retailor ID 
	//http://localhost:8080/rewardpoint/retailer/summary?retailerId=200&startDate=2025-04-01&endDate=2025-06-30
	@GetMapping("/retailer/summary")
	public  ResponseEntity<List<CustomerRewardSummaryDTO>> getCustomersRewardByRetailerId(
			@RequestParam long retailerId,
            @RequestParam String startDate,
            @RequestParam String endDate
			) throws RewardPointsException{
		
		LocalDate start;
	    LocalDate end;
	    try {
	        start = LocalDate.parse(startDate);
	        end = LocalDate.parse(endDate);
	    } catch (DateTimeParseException ex) {
	        throw new RewardPointsException("Service.INVALID_DATE_FORMAT");
	    }

	    if (start.isAfter(end)) {
	        throw new RewardPointsException("Service.START_DATE_AFTER_END");
	    }
		
		List<CustomerRewardSummaryDTO> response = transactionService.getAllCustomerRewardsByRetailerId(retailerId,startDate,endDate);
	    return new ResponseEntity<>(response,HttpStatus.OK);
	}
	

	
	
}
