package com.infy.rewardpoints.controller;

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

import com.infy.rewardpoints.exception.RewardPointsException;
import com.infy.rewardpoints.models.CustomerDTO;
import com.infy.rewardpoints.models.CustomerRewardSummaryMapper;
import com.infy.rewardpoints.models.TransactionDTO;
import com.infy.rewardpoints.models.TransactionResponseMapper;
import com.infy.rewardpoints.service.CustomerService;
import com.infy.rewardpoints.service.TransactionService;

import jakarta.validation.Valid;

/**
 * REST controller for handling reward points-related operations such as
 * customer registration, transaction recording, and reward point summaries.
 */
@RestController
@RequestMapping("/rewardpoint")
@Validated
public class RewardController {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private Environment environment;
	/**
	 * Registers a new customer.
	 * 
	 * @param customerDTO the customer details to register
	 * @return success message with created customer ID
	 * @throws RewardPointsException if the customer already exists or validation
	 *                               fails
	 */
	@PostMapping(value = "/register/customer")
	public ResponseEntity<String> registerCustomer(@Valid @RequestBody CustomerDTO customerDTO)
			throws RewardPointsException {
		long customerId = customerService.saveCustomer(customerDTO);
		String message = environment.getProperty("API.CUSTOMER_SAVE_SUCCESS") + ":" + customerId;
		return new ResponseEntity<>(message, HttpStatus.CREATED);
	}

	/**
	 * Saves a transaction and calculates reward points based on the amount.
	 * 
	 * @param transactionDTO transaction data
	 * @return success message with transaction ID
	 * @throws RewardPointsException if the customer is not found
	 */
	@PostMapping(value = "/save/transaction")
	public ResponseEntity<String> saveTransaction(@Valid @RequestBody TransactionDTO transactionDTO)
			throws RewardPointsException {
		long transactionId = transactionService.saveTransaction(transactionDTO);
		String message = environment.getProperty("API.TRANSACTION_SAVE_SUCCESS") + ":" + transactionId;

		return new ResponseEntity<>(message, HttpStatus.CREATED);
	}

	/**
	 * Retrieves all transactions made by a specific customer.
	 * 
	 * @param customerId the customer's ID
	 * @return list of transactions
	 * @throws RewardPointsException if customer or transactions are not found
	 */
	@GetMapping(value = "customer/{customerId}")
	public ResponseEntity<List<TransactionResponseMapper>> transactionsByCustomer(@PathVariable long customerId)
			throws RewardPointsException {
		List<TransactionResponseMapper> transactionDtos = transactionService.getTransactionsByCustomerId(customerId);

		return new ResponseEntity<>(transactionDtos, HttpStatus.OK);
	}

	/**
	 * Returns reward summary of a customer grouped by month for a given date range.
	 * Example:
	 * /rewardpoint/customer/summary?customerId=200&startDate=2025-04-01&endDate=2025-06-30
	 * 
	 * @param customerId the customer's ID
	 * @param startDate  start of the range (yyyy-MM-dd)
	 * @param endDate    end of the range (yyyy-MM-dd)
	 * @return monthly breakdown and total points earned
	 * @throws RewardPointsException if dates are invalid or customer not found
	 */
	@GetMapping("/customer/summary")
	public ResponseEntity<CustomerRewardSummaryMapper> getAllCustomerRewardSummaries(@RequestParam long customerId,
			@RequestParam String startDate, @RequestParam String endDate) throws RewardPointsException {
		CustomerRewardSummaryMapper response = transactionService.getCustomerRewardsLast3Months(customerId, startDate,
				endDate);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
