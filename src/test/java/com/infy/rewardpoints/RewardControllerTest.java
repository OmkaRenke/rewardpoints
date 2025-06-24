package com.infy.rewardpoints;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infy.rewardpoints.controller.RewardController;
import com.infy.rewardpoints.models.CustomerDTO;
import com.infy.rewardpoints.models.CustomerRewardSummaryMapper;
import com.infy.rewardpoints.models.MonthlyPointsMapper;
import com.infy.rewardpoints.models.TransactionDTO;
import com.infy.rewardpoints.models.TransactionResponseMapper;
import com.infy.rewardpoints.repository.CustomerRepository;
import com.infy.rewardpoints.repository.TransactionRepository;
import com.infy.rewardpoints.service.CustomerService;
import com.infy.rewardpoints.service.CustomerServiceImpl;
import com.infy.rewardpoints.service.TransactionService;
import com.infy.rewardpoints.service.TransactionServiceImpl;

/**
 * Unit tests for RewardController using MockMvc and Mockito. Covers customer
 * registration, transaction creation, and reward summaries.
 */
@WebMvcTest(RewardController.class)
public class RewardControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Mock
	private CustomerRepository customerRepository;
	@Mock
	private TransactionRepository transactionRepository;
	@InjectMocks
	private TransactionService transactionService = new TransactionServiceImpl();
	@InjectMocks
	private CustomerService customerService = new CustomerServiceImpl();
	@Autowired
	private ObjectMapper objectMapper;
	/**
	 * Tests successful customer registration. Expects a 201 CREATED response with a
	 * customer ID in the body.
	 */
	@Test
	void registerCustomer_success() throws Exception {
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setName("Test User");
		customerDTO.setEmail("test@example.com");
		customerDTO.setContact("9876543210");
		Mockito.when(customerService.saveCustomer(any())).thenReturn(101L);
		mockMvc.perform(post("/rewardpoint/register/customer").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(customerDTO))).andExpect(status().isCreated())
				.andExpect(content().string(org.hamcrest.Matchers.containsString("101")));
	}

	/**
	 * Tests successful transaction creation and point calculation. Expects a 201
	 * CREATED response with a transaction ID in the response body.
	 */
	@Test
	void saveTransaction_success() throws Exception {
		TransactionDTO dto = new TransactionDTO();
		dto.setTransactionId(1L);
		dto.setAmount(new java.math.BigDecimal("120"));
		CustomerDTO cust = new CustomerDTO();
		cust.setCustomerId(101L);
		dto.setCustomerDTO(cust);
		Mockito.when(transactionService.saveTransaction(any())).thenReturn(555L);
		mockMvc.perform(post("/rewardpoint/save/transaction").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isCreated())
				.andExpect(content().string(org.hamcrest.Matchers.containsString("555")));
	}

	/**
	 * Tests successful retrieval of all transactions by a given customer ID.
	 * Expects a 200 OK with transaction data including points and transaction
	 * number.
	 */
	@Test
	void transactionsByCustomer_success() throws Exception {
		long customerId = 123L;
		TransactionResponseMapper txn = new TransactionResponseMapper();
		txn.setTransactionId(1L);
		txn.setTransactionNumber("TXN-001");
		txn.setTransactionMode("CARD");
		txn.setAmount(BigDecimal.valueOf(120));
		txn.setTransactionDate(Timestamp.valueOf("2025-06-01 10:00:00"));
		txn.setPointsEarned(90);
		Mockito.when(transactionService.getTransactionsByCustomerId(customerId)).thenReturn(List.of(txn));
		mockMvc.perform(get("/rewardpoint/customer/{customerId}", customerId)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].transactionNumber", is("TXN-001")))
				.andExpect(jsonPath("$[0].pointsEarned", is(90)));
	}

	/**
	 * Tests scenario where transactions are not found for the customer. Expects a
	 * 400 BAD REQUEST response with appropriate error handling.
	 */
	@Test
	void transactionsByCustomer_notFound() throws Exception {
		long customerId = 999L;
		Mockito.when(transactionService.getTransactionsByCustomerId(customerId))
				.thenThrow(new com.infy.rewardpoints.exception.RewardPointsException("Service.CUSTOMER_NOT_FOUND"));
		mockMvc.perform(get("/rewardpoint/customer/{customerId}", customerId)).andExpect(status().isBadRequest());
	}

	/**
	 * Tests reward summary API with valid customer and date range. Expects a 200 OK
	 * response with monthly breakdown and total points.
	 */
	@Test
	void getCustomerRewardSummary_success() throws Exception {
		long customerId = 200L;
		String startDate = "2025-04-01";
		String endDate = "2025-06-30";
		TransactionResponseMapper tx = new TransactionResponseMapper();
		tx.setTransactionId(1L);
		tx.setTransactionNumber("TXN1001");
		tx.setPointsEarned(90);
		MonthlyPointsMapper monthlyPoints = new MonthlyPointsMapper();
		monthlyPoints.setMonth("2025 - April");
		monthlyPoints.setPoints(90);
		monthlyPoints.setTransactioList(List.of(tx));

		CustomerRewardSummaryMapper summary = new CustomerRewardSummaryMapper();
		summary.setCustomerId(customerId);
		summary.setCustomerName("Test Customer");
		summary.setMonthlyRewards(List.of(monthlyPoints));
		summary.setTotalPoints(90);

		Mockito.when(transactionService.getCustomerRewardsLast3Months(customerId, startDate, endDate))
				.thenReturn(summary);
		mockMvc.perform(get("/rewardpoint/customer/summary").param("customerId", String.valueOf(customerId))
				.param("startDate", startDate).param("endDate", endDate).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.customerId").value(200))
				.andExpect(jsonPath("$.totalPoints").value(90))
				.andExpect(jsonPath("$.monthlyRewards[0].month").value("2025 - April"))
				.andExpect(jsonPath("$.monthlyRewards[0].points").value(90))
				.andExpect(jsonPath("$.monthlyRewards[0].transactioList[0].transactionNumber").value("TXN1001"));
	}

	/**
	 * Tests reward summary API with an invalid start date. Expects a 400 BAD
	 * REQUEST response due to bad date format.
	 */
	@Test
	void getCustomerRewardSummary_invalidDate() throws Exception {
		long customerId = 200L;
		Mockito.when(transactionService.getCustomerRewardsLast3Months(anyLong(), anyString(), anyString()))
				.thenThrow(new com.infy.rewardpoints.exception.RewardPointsException("Service.INVALID_DATE_FORMAT"));
		mockMvc.perform(get("/rewardpoint/customer/summary").param("customerId", String.valueOf(customerId))
				.param("startDate", "invalid-date").param("endDate", "2025-06-30")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

}
