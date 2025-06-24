package com.infy.rewardpoints.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infy.rewardpoints.dto.CustomerRewardSummaryDTO;
import com.infy.rewardpoints.dto.MonthlyPointsDTO;
import com.infy.rewardpoints.dto.TransactionDTO;
import com.infy.rewardpoints.dto.TransactionResponseDTO;
import com.infy.rewardpoints.entity.Customer;
import com.infy.rewardpoints.entity.Transaction;
import com.infy.rewardpoints.exception.RewardPointsException;
import com.infy.rewardpoints.repository.CustomerRepository;
import com.infy.rewardpoints.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@Service(value = "transactionService")
@Transactional
public class TransactionServiceImpl implements TransactionService {

	private ModelMapper modelMapper = new ModelMapper();
	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Override
	public List<TransactionResponseDTO> getTransactionsByCustomerId(long customerId) throws RewardPointsException {
		Optional<Customer> optCustomer = customerRepository.findById(customerId);
		Customer customer = optCustomer.orElseThrow(() -> new RewardPointsException("Service.CUSTOMER_NOT_FOUND"));

		List<Transaction> transactionList = transactionRepository.findByCustomerCustomerId(customerId);
		if (transactionList.isEmpty()) {
			throw new RewardPointsException("Service.TRANSACTIONS_NOT_FOUND");
		}

		List<TransactionResponseDTO> transactionDTOList = new ArrayList<>();

		transactionList.stream().forEach(transaction -> {
			TransactionResponseDTO transactionDTO = modelMapper.map(transaction, TransactionResponseDTO.class);
			transactionDTOList.add(transactionDTO);

		});
		return transactionDTOList;
	}

	@Override
	public long saveTransaction(TransactionDTO transactionDTO) throws RewardPointsException {
		Optional<Customer> optCustomer = customerRepository.findById(transactionDTO.getCustomerDTO().getCustomerId());
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

		transactionRepository.save(transaction);
		return transaction.getTransactionId();
	}

	@Override
	public CustomerRewardSummaryDTO getCustomerRewardsLast3Months(long customerId, String startDate, String endDate)
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

		Optional<Customer> optCustomer = customerRepository.findById(customerId);
		Customer customer = optCustomer.orElseThrow(() -> new RewardPointsException("Service.CUSTOMER_NOT_FOUND"));
		LocalDateTime startDateTime = LocalDate.parse(startDate).atStartOfDay();
		LocalDateTime endDateTime = LocalDate.parse(endDate).atTime(LocalTime.MAX);

		List<Transaction> transactionList = transactionRepository.findByCustomerCustomerIdAndTransactionDateBetween(
				customerId, Timestamp.valueOf(startDateTime), Timestamp.valueOf(endDateTime));
		if (transactionList.isEmpty()) {

			throw new RewardPointsException("Service.TRANSACTIONS_NOT_FOUND");
		}
		Map<YearMonth, List<Transaction>> groupedBymonths = transactionList.stream()
				.collect(Collectors.groupingBy(t -> YearMonth.from(t.getTransactionDate().toLocalDateTime())));

		List<MonthlyPointsDTO> monthlyRewards = new ArrayList<>();
		int totalPoints = 0;

		for (Map.Entry<YearMonth, List<Transaction>> entry : groupedBymonths.entrySet()) {
			YearMonth ym = entry.getKey();
			List<Transaction> monthTransactions = entry.getValue();

			int monthPoints = monthTransactions.stream().mapToInt(t -> t.getPointsEarned()).sum();

			List<TransactionDTO> dtoList = monthTransactions.stream()
					.map(tx -> modelMapper.map(tx, TransactionDTO.class)).toList();

			MonthlyPointsDTO mpDTO = new MonthlyPointsDTO();
			mpDTO.setMonth(ym.toString());
			mpDTO.setPoints(monthPoints);
			mpDTO.setTransactioList(dtoList);

			monthlyRewards.add(mpDTO);
			totalPoints += monthPoints;
		}

		CustomerRewardSummaryDTO response = new CustomerRewardSummaryDTO();
		response.setCustomerId(customer.getCustomerId());
		response.setCustomerName(customer.getName());
		response.setMonthlyRewards(monthlyRewards);
		response.setTotalPoints(totalPoints);

		return response;
	}
}
