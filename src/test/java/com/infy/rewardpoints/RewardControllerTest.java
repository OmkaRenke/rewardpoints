package com.infy.rewardpoints;

import com.infy.rewardpoints.controller.RewardController;
import com.infy.rewardpoints.exception.RewardPointsException;
import com.infy.rewardpoints.models.CustomerDTO;
import com.infy.rewardpoints.models.CustomerRewardSummaryMapper;
import com.infy.rewardpoints.models.TransactionDTO;
import com.infy.rewardpoints.models.TransactionResponseMapper;
import com.infy.rewardpoints.service.CustomerService;
import com.infy.rewardpoints.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RewardControllerTest {

	@InjectMocks
	private RewardController rewardController;

	@Mock
	private CustomerService customerService;

	@Mock
	private TransactionService transactionService;

	@Mock
	private Environment environment;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	// Test: Register Customer
	@Test
	void testRegisterCustomer_Valid() throws RewardPointsException {
		CustomerDTO dto = new CustomerDTO();
		dto.setName("Test User");
		dto.setContact("9876543210");
		dto.setEmail("test@example.com");

		when(customerService.saveCustomer(dto)).thenReturn(101L);
		when(environment.getProperty("API.CUSTOMER_SAVE_SUCCESS")).thenReturn("Customer Registered Successfully");

		ResponseEntity<String> response = rewardController.registerCustomer(dto);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals("Customer Registered Successfully:101", response.getBody());
	}

	// Test: Save Transaction
	@Test
	void testSaveTransaction_Valid() throws RewardPointsException {
		TransactionDTO dto = new TransactionDTO();
		dto.setAmount(BigDecimal.valueOf(150));
		dto.setTransactionNumber("TXN04");
		dto.setTransactionMode("UPI");
		dto.setTransactionDate(Timestamp.valueOf("2025-05-19 10:00:00"));

		CustomerDTO cust = new CustomerDTO();
		cust.setCustomerId(104L);
		dto.setCustomerDTO(cust);

		when(transactionService.saveTransaction(dto)).thenReturn(7L);
		when(environment.getProperty("API.TRANSACTION_SAVE_SUCCESS")).thenReturn("Transaction saved successfully");

		ResponseEntity<String> response = rewardController.saveTransaction(dto);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals("Transaction saved successfully:7", response.getBody());
	}

	// Test: Get All Transactions by Customer
	@Test
	void testTransactionsByCustomer_Valid() throws RewardPointsException {
		long customerId = 100L;

		TransactionResponseMapper tx1 = new TransactionResponseMapper();
		tx1.setTransactionId(1L);
		tx1.setTransactionNumber("TXN1001");
		tx1.setTransactionMode("CARD");
		tx1.setAmount(BigDecimal.valueOf(120));
		tx1.setTransactionDate(Timestamp.valueOf("2025-04-10 10:00:00"));
		tx1.setPointsEarned(90);

		when(transactionService.getTransactionsByCustomerId(customerId)).thenReturn(List.of(tx1));

		ResponseEntity<List<TransactionResponseMapper>> response = rewardController.transactionsByCustomer(customerId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(1, response.getBody().size());
		assertEquals("TXN1001", response.getBody().get(0).getTransactionNumber());
	}

	// Test: Get Monthly Reward Summary
	@Test
	void testGetCustomerRewardSummary_Valid() throws RewardPointsException {
		long customerId = 100L;
		String startDate = "2025-04-01";
		String endDate = "2025-06-30";

		CustomerRewardSummaryMapper summary = new CustomerRewardSummaryMapper();
		summary.setCustomerId(customerId);
		summary.setCustomerName("Kevin");
		summary.setTotalPoints(115);

		when(transactionService.getCustomerRewardsLast3Months(customerId, startDate, endDate)).thenReturn(summary);

		ResponseEntity<CustomerRewardSummaryMapper> response = rewardController
				.getAllCustomerRewardSummaries(customerId, startDate, endDate);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(115, response.getBody().getTotalPoints());
		assertEquals("Kevin", response.getBody().getCustomerName());
	}

	// Test: Register Customer - Already Exists (throws exception)
	@Test
	void testRegisterCustomer_AlreadyExists() throws RewardPointsException {
		CustomerDTO dto = new CustomerDTO();
		dto.setName("John");
		dto.setEmail("john@example.com");
		dto.setContact("1234567890");

		when(customerService.saveCustomer(dto)).thenThrow(new RewardPointsException("Service.CUSTOMER_ALREADY_EXISTS"));

		RewardPointsException ex = assertThrows(RewardPointsException.class, () -> {
			rewardController.registerCustomer(dto);
		});

		assertEquals("Service.CUSTOMER_ALREADY_EXISTS", ex.getMessage());
	}

	// Test: Save Transaction - Customer Not Found
	@Test
	void testSaveTransaction_CustomerNotFound() throws RewardPointsException {
		TransactionDTO dto = new TransactionDTO();
		dto.setAmount(BigDecimal.valueOf(90));
		dto.setTransactionNumber("TXN404");
		dto.setTransactionDate(Timestamp.valueOf("2025-05-01 10:00:00"));
		CustomerDTO cust = new CustomerDTO();
		cust.setCustomerId(999L);
		dto.setCustomerDTO(cust);

		when(transactionService.saveTransaction(dto))
				.thenThrow(new RewardPointsException("Service.CUSTOMER_NOT_FOUND"));

		RewardPointsException ex = assertThrows(RewardPointsException.class, () -> {
			rewardController.saveTransaction(dto);
		});

		assertEquals("Service.CUSTOMER_NOT_FOUND", ex.getMessage());
	}

	// Test: Get Transactions by Customer - Not Found
	@Test
	void testTransactionsByCustomer_NotFound() throws RewardPointsException {
		long customerId = 999L;

		when(transactionService.getTransactionsByCustomerId(customerId))
				.thenThrow(new RewardPointsException("Service.CUSTOMER_NOT_FOUND"));

		RewardPointsException ex = assertThrows(RewardPointsException.class, () -> {
			rewardController.transactionsByCustomer(customerId);
		});

		assertEquals("Service.CUSTOMER_NOT_FOUND", ex.getMessage());
	}

	// Test: Get Customer Summary - Invalid Date Format
	@Test
	void testGetCustomerSummary_InvalidDate() throws RewardPointsException {
		long customerId = 100L;
		String invalidStart = "abc-date";
		String validEnd = "2025-06-30";

		when(transactionService.getCustomerRewardsLast3Months(customerId, invalidStart, validEnd))
				.thenThrow(new RewardPointsException("Service.INVALID_DATE_FORMAT"));

		RewardPointsException ex = assertThrows(RewardPointsException.class, () -> {
			rewardController.getAllCustomerRewardSummaries(customerId, invalidStart, validEnd);
		});

		assertEquals("Service.INVALID_DATE_FORMAT", ex.getMessage());
	}

	// Test: Get Customer Summary - Start Date After End Date
	@Test
	void testGetCustomerSummary_StartDateAfterEndDate() throws RewardPointsException {
		long customerId = 100L;
		String startDate = "2025-07-01";
		String endDate = "2025-06-01";

		when(transactionService.getCustomerRewardsLast3Months(customerId, startDate, endDate))
				.thenThrow(new RewardPointsException("Service.START_DATE_AFTER_END"));

		RewardPointsException ex = assertThrows(RewardPointsException.class, () -> {
			rewardController.getAllCustomerRewardSummaries(customerId, startDate, endDate);
		});

		assertEquals("Service.START_DATE_AFTER_END", ex.getMessage());
	}

}