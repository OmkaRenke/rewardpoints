package com.infy.rewardpoints;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.infy.rewardpoints.exception.RewardPointsException;
import com.infy.rewardpoints.models.Customer;
import com.infy.rewardpoints.models.CustomerDTO;
import com.infy.rewardpoints.models.CustomerRewardSummaryMapper;
import com.infy.rewardpoints.models.Transaction;
import com.infy.rewardpoints.models.TransactionDTO;
import com.infy.rewardpoints.models.TransactionResponseMapper;
import com.infy.rewardpoints.repository.CustomerRepository;
import com.infy.rewardpoints.repository.TransactionRepository;
import com.infy.rewardpoints.service.CustomerService;
import com.infy.rewardpoints.service.CustomerServiceImpl;
import com.infy.rewardpoints.service.TransactionService;
import com.infy.rewardpoints.service.TransactionServiceImpl;

@SpringBootTest
class RewardpointsApplicationTests {

	@Mock
	private CustomerRepository customerRepository;
	@Mock
	private TransactionRepository transactionRepository;
	@InjectMocks
	private TransactionService transactionService = new TransactionServiceImpl();
	@InjectMocks
	private CustomerService customerService = new CustomerServiceImpl();

	// Save new customer successfully
	@Test
	void saveCustomer_success() throws RewardPointsException {
		CustomerDTO dto = new CustomerDTO();
		dto.setEmail("test@example.com");
		dto.setName("Test User");
		dto.setContact("9876543210");
		Mockito.when(customerRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
		Customer savedCustomer = new Customer();
		savedCustomer.setCustomerId(101L);
		savedCustomer.setEmail("test@example.com");
		Mockito.when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);
		long returnedId = customerService.saveCustomer(dto);
		Assertions.assertEquals(101L, returnedId);
	}

	// Customer already exists
	@Test
	void saveCustomer_alreadyExists() {
		CustomerDTO dto = new CustomerDTO();
		dto.setEmail("existing@example.com");
		dto.setName("Existing User");
		dto.setContact("9999999999");
		Customer existingCustomer = new Customer();
		existingCustomer.setCustomerId(500L);
		existingCustomer.setEmail("existing@example.com");
		Mockito.when(customerRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(existingCustomer));
		RewardPointsException exception = assertThrows(RewardPointsException.class, () -> {
			customerService.saveCustomer(dto);
		});
		Assertions.assertEquals("SERVICE.CUSTOMER.EXISTS", exception.getMessage());
	}

	// save transaction API method valid test
	@Test
	void saveTransaction() throws RewardPointsException {
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setCustomerId(1L);
		TransactionDTO dto = new TransactionDTO();
		dto.setCustomerDTO(customerDTO);
		dto.setAmount(BigDecimal.valueOf(120));
		Customer customer = new Customer();
		customer.setCustomerId(1L);
		Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
		Transaction saved = new Transaction();
		saved.setTransactionId(201L);
		Mockito.when(transactionRepository.save(any(Transaction.class))).thenReturn(saved);
		long txnId = transactionService.saveTransaction(dto);
		Assertions.assertEquals(201L, txnId);
	}

	// save transaction API method invalid test
	@Test
	void saveTransactionInvalid() throws RewardPointsException {
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setCustomerId(999L);
		TransactionDTO dto = new TransactionDTO();
		dto.setCustomerDTO(customerDTO);
		dto.setAmount(BigDecimal.valueOf(120));
		Mockito.when(customerRepository.findById(999L)).thenReturn(Optional.empty());
		RewardPointsException ex = assertThrows(RewardPointsException.class,
				() -> transactionService.saveTransaction(dto));
		Assertions.assertEquals("Service.CUSTOMER_NOT_FOUND", ex.getMessage());
	}

	// Customer exists and has transactions
	@Test
	void getTransactionsByCustomerId_success() throws RewardPointsException {
		long customerId = 100L;

		Customer customer = new Customer();
		customer.setCustomerId(customerId);
		customer.setName("Kevin");

		Transaction txn1 = new Transaction();
		txn1.setTransactionId(1L);
		txn1.setTransactionNumber("TXN123");
		txn1.setAmount(BigDecimal.valueOf(120));
		txn1.setTransactionDate(Timestamp.valueOf(LocalDateTime.now()));
		txn1.setPointsEarned(90);

		List<Transaction> txnList = List.of(txn1);

		Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
		Mockito.when(transactionRepository.findByCustomerCustomerId(customerId)).thenReturn(txnList);

		List<TransactionResponseMapper> result = transactionService.getTransactionsByCustomerId(customerId);

		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals("TXN123", result.get(0).getTransactionNumber());
	}

	// Customer not found for TransactionsByCustomerID
	@Test
	void getTransactionsByCustomerId_customerNotFound() {
		Mockito.when(customerRepository.findById(404L)).thenReturn(Optional.empty());

		RewardPointsException ex = assertThrows(RewardPointsException.class,
				() -> transactionService.getTransactionsByCustomerId(404L));

		Assertions.assertEquals("Service.CUSTOMER_NOT_FOUND", ex.getMessage());
	}

	// No transactions found for TransactionsByCustomerId
	@Test
	void getTransactionsByCustomerId_noTransactions() {
		long customerId = 200L;
		Customer customer = new Customer();
		customer.setCustomerId(customerId);

		Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
		Mockito.when(transactionRepository.findByCustomerCustomerId(customerId)).thenReturn(Collections.emptyList());

		RewardPointsException ex = assertThrows(RewardPointsException.class,
				() -> transactionService.getTransactionsByCustomerId(customerId));

		Assertions.assertEquals("Service.TRANSACTIONS_NOT_FOUND", ex.getMessage());
	}

	// get Customer Rewards Last 3 Months Valid test
	@Test
	void getCustomerRewardsLast3Months_Valid() throws RewardPointsException {
		Long customerId = 204L;
		String startDate = "2025-04-01";
		String endDate = "2025-06-30";

		Customer customer = new Customer();
		customer.setCustomerId(customerId);
		customer.setName("Kevin");

		Transaction transac1 = new Transaction();
		transac1.setTransactionId(1L);
		transac1.setTransactionDate(Timestamp.valueOf("2025-04-15 10:00:00"));
		transac1.setPointsEarned(40);

		Transaction transac2 = new Transaction();
		transac2.setTransactionId(2L);
		transac2.setTransactionDate(Timestamp.valueOf("2025-05-10 15:00:00"));
		transac2.setPointsEarned(50);

		List<Transaction> mockTransactions = List.of(transac1, transac2);

		Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
		Mockito.when(transactionRepository.findByCustomerCustomerIdAndTransactionDateBetween(customerId,
				any(Timestamp.class), any(Timestamp.class))).thenReturn(mockTransactions);

		CustomerRewardSummaryMapper response = transactionService.getCustomerRewardsLast3Months(customerId, startDate,
				endDate);

		Assertions.assertEquals(customerId, response.getCustomerId());
		Assertions.assertEquals("Kevin", response.getCustomerName());
		Assertions.assertEquals(2, response.getMonthlyRewards().size());
		Assertions.assertEquals(90, response.getTotalPoints());
	}

	// get Customer Rewards Last 3 Months Invalid test CustomerNotFound
	@Test
	void getCustomerRewardsLast3Months_CustomerNotFound() {
		Long customerId = 999L;
		String startDate = "2025-04-01";
		String endDate = "2025-06-30";

		Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

		RewardPointsException exception = assertThrows(RewardPointsException.class,
				() -> transactionService.getCustomerRewardsLast3Months(customerId, startDate, endDate));

		Assertions.assertEquals("Service.CUSTOMER_NOT_FOUND", exception.getMessage());
	}

	// get Customer Rewards Last 3 Months Invalid test NoTransactionsFound
	@Test
	void getCustomerRewardsLast3Months_NoTransactionsFound() {
		Long customerId = 204L;
		String startDate = "2025-04-01";
		String endDate = "2025-06-30";

		Customer customer = new Customer();
		customer.setCustomerId(customerId);
		customer.setName("Kevin");

		Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
		Mockito.when(transactionRepository.findByCustomerCustomerIdAndTransactionDateBetween(customerId,
				any(Timestamp.class), any(Timestamp.class))).thenReturn(Collections.emptyList());

		RewardPointsException exception = assertThrows(RewardPointsException.class,
				() -> transactionService.getCustomerRewardsLast3Months(customerId, startDate, endDate));

		Assertions.assertEquals("Service.TRANSACTIONS_NOT_FOUND", exception.getMessage());
	}
}
