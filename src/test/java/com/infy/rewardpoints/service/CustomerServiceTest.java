package com.infy.rewardpoints.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

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
import com.infy.rewardpoints.repository.CustomerRepository;
import com.infy.rewardpoints.repository.TransactionRepository;

@SpringBootTest
public class CustomerServiceTest {
	@Mock
	private CustomerRepository customerRepository;
	@Mock
	private TransactionRepository transactionRepository;
	@InjectMocks
	private TransactionService transactionService = new TransactionServiceImpl();
	@InjectMocks
	private CustomerService customerService = new CustomerServiceImpl();

	// Add new customer successfully
	@Test
	void addCustomer_success() throws RewardPointsException {
		CustomerDTO dto = new CustomerDTO();
		dto.setEmail("test@example.com");
		dto.setName("Test User");
		dto.setContact("9876543210");
		Mockito.when(customerRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
		Customer savedCustomer = new Customer();
		savedCustomer.setCustomerId(101L);
		savedCustomer.setEmail("test@example.com");
		Mockito.when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);
		long returnedId = customerService.addCustomer(dto);
		Assertions.assertEquals(101L, returnedId);
	}

	// Customer already exists
	@Test
	void addCustomer_alreadyExists() {
		CustomerDTO dto = new CustomerDTO();
		dto.setEmail("existing@example.com");
		dto.setName("Existing User");
		dto.setContact("9999999999");
		Customer existingCustomer = new Customer();
		existingCustomer.setCustomerId(500L);
		existingCustomer.setEmail("existing@example.com");
		Mockito.when(customerRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(existingCustomer));
		RewardPointsException exception = assertThrows(RewardPointsException.class, () -> {
			customerService.addCustomer(dto);
		});
		Assertions.assertEquals("SERVICE.CUSTOMER.EXISTS", exception.getMessage());
	}
}
