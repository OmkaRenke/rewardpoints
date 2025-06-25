package com.infy.rewardpoints.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infy.rewardpoints.exception.RewardPointsException;
import com.infy.rewardpoints.models.Customer;
import com.infy.rewardpoints.models.CustomerRewardSummaryMapper;
import com.infy.rewardpoints.models.MonthlyPointsMapper;
import com.infy.rewardpoints.models.Transaction;
import com.infy.rewardpoints.models.TransactionDTO;
import com.infy.rewardpoints.models.TransactionResponseMapper;
import com.infy.rewardpoints.repository.CustomerRepository;
import com.infy.rewardpoints.repository.TransactionRepository;

import jakarta.transaction.Transactional;

/**
 * Service implementation for handling transaction operations and reward point
 * calculations.
 */
@Service(value = "transactionService")
@Transactional
public class TransactionServiceImpl implements TransactionService {
	private ModelMapper modelMapper = new ModelMapper();
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private TransactionRepository transactionRepository;

	/**
	 * Retrieves all transactions made by a specific customer.
	 */
	@Override
	public List<TransactionResponseMapper> getTransactionsByCustomerId(long customerId) throws RewardPointsException {
		Optional<Customer> optCustomer = customerRepository.findById(customerId);
		Customer customer = optCustomer.orElseThrow(() -> new RewardPointsException("Service.CUSTOMER_NOT_FOUND"));
		List<Transaction> transactionList = transactionRepository.findByCustomerCustomerId(customerId);
		if (transactionList.isEmpty()) {
			throw new RewardPointsException("Service.TRANSACTIONS_NOT_FOUND");
		}
		List<TransactionResponseMapper> transactionDTOList = new ArrayList<>();
		transactionList.stream().forEach(transaction -> {
			TransactionResponseMapper transactionDTO = modelMapper.map(transaction, TransactionResponseMapper.class);
			transactionDTOList.add(transactionDTO);
		});
		return transactionDTOList;
	}

	/**
	 * Adds a new transaction and calculates reward points based on the transaction
	 * amount.
	 */
	@Override
	public long addTransaction(TransactionDTO transactionDTO, long customerId) throws RewardPointsException {
		Optional<Customer> optCustomer = customerRepository.findById(customerId);
		Customer customer = optCustomer.orElseThrow(() -> new RewardPointsException("Service.CUSTOMER_NOT_FOUND"));
		Transaction transaction = modelMapper.map(transactionDTO, Transaction.class);
		transaction.setCustomer(customer);
		// calculate points based on $ Amount
		double amount = transactionDTO.getAmount().doubleValue();
		// bigdecimal to double
		if (amount > 100) {
			transaction.setPointsEarned((int) ((amount - 100) * 2 + 50));
		} else if (amount > 50) {
			transaction.setPointsEarned((int) (amount - 50));
		} else {
			transaction.setPointsEarned(0);
		}
		Transaction savedTransaction = transactionRepository.save(transaction);
		return savedTransaction.getTransactionId();
	}

	/**
	 * Calculates reward summary for the customer over the last 3 months (or given
	 * date range).
	 */
	@Override
	public CustomerRewardSummaryMapper getCustomerRewardsLast3Months(long customerId, String startDate, String endDate)
			throws RewardPointsException {
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
		LocalDate today = LocalDate.now();
		if (start.isAfter(today) || end.isAfter(today)) {
			throw new RewardPointsException("Service.DATE_IN_FUTURE_NOT_ALLOWED");
		}
		Optional<Customer> optCustomer = customerRepository.findById(customerId);
		Customer customer = optCustomer.orElseThrow(() -> new RewardPointsException("Service.CUSTOMER_NOT_FOUND"));
		Timestamp startTimestamp = Timestamp.valueOf(start.atStartOfDay());
		Timestamp endTimestamp = Timestamp.valueOf(end.atTime(LocalTime.MAX));
		List<Transaction> transactionList = transactionRepository
				.findByCustomerCustomerIdAndTransactionDateBetween(customerId, startTimestamp, endTimestamp);
		if (transactionList.isEmpty()) {
			throw new RewardPointsException("Service.TRANSACTIONS_NOT_FOUND");
		}
		Map<YearMonth, List<Transaction>> groupedBymonths = transactionList.stream()
				.collect(Collectors.groupingBy(t -> YearMonth.from(t.getTransactionDate().toLocalDateTime())));
		List<MonthlyPointsMapper> monthlyRewards = new ArrayList<>();
		int totalPoints = 0;

		for (Map.Entry<YearMonth, List<Transaction>> entry : groupedBymonths.entrySet()) {
			YearMonth ym = entry.getKey();
			List<Transaction> monthTransactions = entry.getValue();
			int monthPoints = monthTransactions.stream().mapToInt(t -> t.getPointsEarned()).sum();
			List<TransactionResponseMapper> dtoList = monthTransactions.stream()
					.map(tx -> modelMapper.map(tx, TransactionResponseMapper.class)).toList();
			MonthlyPointsMapper mpDTO = new MonthlyPointsMapper();
			mpDTO.setMonth(ym.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
			mpDTO.setYear(String.valueOf(ym.getYear()));
			mpDTO.setPoints(monthPoints);
			mpDTO.setTransactioList(dtoList);
			monthlyRewards.add(mpDTO);
			totalPoints += monthPoints;
		}

		CustomerRewardSummaryMapper response = new CustomerRewardSummaryMapper();
		response.setCustomerId(customer.getCustomerId());
		response.setCustomerName(customer.getName());
		response.setMonthlyRewards(monthlyRewards);
		response.setTotalPoints(totalPoints);
		return response;
	}
}
