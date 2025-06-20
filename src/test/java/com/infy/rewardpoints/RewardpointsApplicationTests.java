package com.infy.rewardpoints;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import com.infy.rewardpoints.dto.CustomerDTO;
import com.infy.rewardpoints.dto.CustomerRewardSummaryDTO;
import com.infy.rewardpoints.dto.RetailerDTO;
import com.infy.rewardpoints.dto.TransactionDTO;
import com.infy.rewardpoints.entity.Customer;
import com.infy.rewardpoints.entity.Retailer;
import com.infy.rewardpoints.entity.Transaction;
import com.infy.rewardpoints.exception.RewardPointsException;
import com.infy.rewardpoints.repository.CustomerRepository;
import com.infy.rewardpoints.repository.RetailerRepository;
import com.infy.rewardpoints.repository.TransactionRepository;
import com.infy.rewardpoints.service.TransactionService;
import com.infy.rewardpoints.service.TransactionServiceImpl;

@SpringBootTest
class RewardpointsApplicationTests {

	 private ModelMapper modelMapper = new ModelMapper();
	 
	 @Mock
	 private CustomerRepository customerRepository;
	 @Mock 
	 private RetailerRepository retailerRepository;
	 @Mock
	 private TransactionRepository transactionRepository;
	 
	 @InjectMocks
	 private TransactionService transactionService = new TransactionServiceImpl();
	 
	 
	  // save transaction API method valid test
	 @Test
	 void saveTransaction() throws RewardPointsException{
		 
		 Customer customer = new Customer();
		 customer.setCustomerId(204L);
		 
		 Retailer retailer = new Retailer();
		 retailer.setRetailerId(101L);
		 
		 Transaction transaction = new Transaction();
		 transaction.setTransactionId(221);
		 transaction.setAmount(BigDecimal.valueOf(120.0));
		 
		 customer.setRetailer(retailer);
		 transaction.setCustomer(customer);
		 
		 CustomerDTO customerDTO = new CustomerDTO();
		 customerDTO.setCustomerId(204L);
		 RetailerDTO retailerDTO = new RetailerDTO();
		 retailerDTO.setRetailerId(101L);
		 TransactionDTO transactionDTO =new TransactionDTO();
		 transactionDTO.setTransactionId(221);
		 
		 customerDTO.setRetailerDTO(retailerDTO);
		 transactionDTO.setCustomerDTO(customerDTO);
		 
		 
		 Mockito.when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
		 
		 Assertions.assertEquals(101,transactionService.saveTransaction(transactionDTO));
	 }
	  // save transaction API method invalid test
		 @Test
		 void saveTransactionInvalid() throws RewardPointsException{
			
			 
			 CustomerDTO customerDTO = new CustomerDTO();
			 customerDTO.setCustomerId(204);
			 RetailerDTO retailerDTO = new RetailerDTO();
			 retailerDTO.setRetailerId(101);
			 TransactionDTO transactionDTO =new TransactionDTO();
			 transactionDTO.setTransactionId(221);
			 
			 customerDTO.setRetailerDTO(retailerDTO);
			 transactionDTO.setCustomerDTO(customerDTO);
			 
			 
			 Mockito.when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());
			 
			 RewardPointsException exception = assertThrows(RewardPointsException.class,()->transactionService.saveTransaction(transactionDTO));
			 
			 Assertions.assertEquals("Service.CUSTOMER_NOT_FOUND",exception.getMessage());
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
		     Mockito.when(transactionRepository.findByCustomerCustomerIdAndTransactionDateBetween(
		             customerId,
		             any(Timestamp.class),
		             any(Timestamp.class)))
		             .thenReturn(mockTransactions);

		     CustomerRewardSummaryDTO response = transactionService.getCustomerRewardsLast3Months(customerId, startDate, endDate);

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

		     RewardPointsException exception = assertThrows(
		             RewardPointsException.class,
		             () -> transactionService.getCustomerRewardsLast3Months(customerId, startDate, endDate)
		     );

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
		     Mockito.when(transactionRepository.findByCustomerCustomerIdAndTransactionDateBetween(
		             customerId,
		             any(Timestamp.class),
		             any(Timestamp.class)))
		             .thenReturn(Collections.emptyList());

		     RewardPointsException exception = assertThrows(
		             RewardPointsException.class,
		             () -> transactionService.getCustomerRewardsLast3Months(customerId, startDate, endDate)
		     );

		     Assertions.assertEquals("Service.TRANSACTIONS_NOT_FOUND", exception.getMessage());
		 }
		 
		 //  get All Customer Rewards By RetailerId Valid
		 @Test
		 void getAllCustomerRewardsByRetailerId_Valid() throws RewardPointsException {
		     long retailerId = 100L;
		     String startDate = "2025-04-01";
		     String endDate = "2025-06-30";

		     Retailer retailer = new Retailer();
		     retailer.setRetailerId(retailerId);
		     retailer.setName("Jack");

		     Customer customer1 = new Customer();
		     customer1.setCustomerId(200L);
		     customer1.setName("Kevin");

		     Transaction tx1 = new Transaction();
		     tx1.setTransactionId(1L);
		     tx1.setTransactionDate(Timestamp.valueOf("2025-04-10 12:00:00"));
		     tx1.setPointsEarned(40);

		     Mockito.when(retailerRepository.findById(retailerId)).thenReturn(Optional.of(retailer));
		     Mockito.when(customerRepository.findByRetailerRetailerId(retailerId)).thenReturn(List.of(customer1));
		     Mockito.when(transactionRepository.findByCustomerCustomerIdAndTransactionDateBetween(
		             200L,
		             any(Timestamp.class),
		             any(Timestamp.class)
		     )).thenReturn(List.of(tx1));
		     
		    
		     Mockito.when(modelMapper.map(any(Transaction.class), TransactionDTO.class))
		             .thenReturn(new TransactionDTO());

		     List<CustomerRewardSummaryDTO> result = transactionService.getAllCustomerRewardsByRetailerId(
		             retailerId, startDate, endDate);

		     Assertions.assertEquals(1, result.size());
		     Assertions.assertEquals("Kevin", result.get(0).getCustomerName());
		     Assertions.assertEquals(40, result.get(0).getTotalPoints());
		 }
		 
		 // get All Customer Rewards By RetailerId Invalid Test RetailerNotFound
		 @Test
		 void getAllCustomerRewardsByRetailerId_RetailerNotFound() {
		     long retailerId = 999L;
		     String startDate = "2025-04-01";
		     String endDate = "2025-06-30";

		     Mockito.when(retailerRepository.findById(retailerId)).thenReturn(Optional.empty());

		     RewardPointsException exception = Assertions.assertThrows(
		             RewardPointsException.class,
		             () -> transactionService.getAllCustomerRewardsByRetailerId(retailerId, startDate, endDate)
		     );

		     Assertions.assertEquals("Service.RETAILER_NOT_FOUND", exception.getMessage());
		 }
		 
		 // get All Customer Rewards By RetailerId Invalid Test NoCustomers
		 @Test
		 void getAllCustomerRewardsByRetailerId_NoCustomers() {
		     long retailerId = 100L;
		     String startDate = "2025-04-01";
		     String endDate = "2025-06-30";

		     Retailer retailer = new Retailer();
		     retailer.setRetailerId(retailerId);

		     Mockito.when(retailerRepository.findById(retailerId)).thenReturn(Optional.of(retailer));
		     Mockito.when(customerRepository.findByRetailerRetailerId(retailerId)).thenReturn(Collections.emptyList());

		     RewardPointsException exception = Assertions.assertThrows(
		             RewardPointsException.class,
		             () -> transactionService.getAllCustomerRewardsByRetailerId(retailerId, startDate, endDate)
		     );

		     Assertions.assertEquals("Service.CUSTOMER_NOT_FOUND", exception.getMessage());
		 }

}
