package com.infy.rewardpoints.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infy.rewardpoints.dto.CustomerDTO;
import com.infy.rewardpoints.dto.CustomerRewardSummaryDTO;
import com.infy.rewardpoints.dto.MonthlyPointsDTO;
import com.infy.rewardpoints.dto.TransactionDTO;
import com.infy.rewardpoints.entity.Customer;
import com.infy.rewardpoints.entity.Retailer;
import com.infy.rewardpoints.exception.RewardPointsException;
import com.infy.rewardpoints.repository.CustomerRepository;
import com.infy.rewardpoints.repository.RetailerRepository;
import com.infy.rewardpoints.repository.TransactionRepository;

import jakarta.transaction.Transactional;

import com.infy.rewardpoints.entity.Transaction;

@Service(value="transactionService")
@Transactional
public class TransactionServiceImpl implements TransactionService {
	
private ModelMapper modelMapper = new ModelMapper();
	
	@Autowired
	private RetailerRepository retailerRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;


	@Override
	public List<TransactionDTO> getTransactionsByCustomerId(long customerId) throws RewardPointsException {
		Optional<Customer> optCustomer = customerRepository.findById(customerId);
		Customer customer = optCustomer.orElseThrow(()-> new RewardPointsException("Service.CUSTOMER_NOT_FOUND"));
		
		List<Transaction> transactionList = transactionRepository.findByCustomerCustomerId(customerId);
		if(transactionList.isEmpty()) {
			throw new RewardPointsException("Service.TRANSACTIONS_NOT_FOUND");
		}
		
		List<TransactionDTO> transactionDTOList = new ArrayList<>();
		
		transactionList.stream().forEach(transaction->{
			TransactionDTO transactionDTO = modelMapper.map(transaction, TransactionDTO.class);
			CustomerDTO customerDTO = modelMapper.map(transaction.getCustomer(), CustomerDTO.class);
			
			
			transactionDTO.setCustomerDTO(customerDTO);
			transactionDTOList.add(transactionDTO);
			
		});
		return transactionDTOList;
	}


	@Override
	public long saveTransaction(TransactionDTO transactionDTO) throws RewardPointsException {
		Optional<Customer> optCustomer = customerRepository.findById(transactionDTO.getCustomerDTO().getCustomerId());
		Customer customer = optCustomer.orElseThrow(()-> new RewardPointsException("Service.CUSTOMER_NOT_FOUND"));
		
		
		Transaction transaction = modelMapper.map(transactionDTO, Transaction.class);
		transaction.setCustomer(customer);
		
		// calculate points based on $ Amount 
		double amount  = transactionDTO.getAmount().doubleValue();
		// bigdecimal to double
		if(amount > 100) {
			transaction.setPointsEarned((int) ((amount - 100)*2 + 50));
		}else if(amount >50) {
			transaction.setPointsEarned((int) (amount -50));
		}else {
			transaction.setPointsEarned(0);
		}
		
		transactionRepository.save(transaction);
		return transaction.getTransactionId();
	}


	@Override
	public CustomerRewardSummaryDTO getCustomerRewardsLast3Months(long customerId, String startDate,
			String endDate) throws RewardPointsException {
		Optional<Customer> optCustomer = customerRepository.findById(customerId);
		Customer customer = optCustomer.orElseThrow(()-> new RewardPointsException("Service.CUSTOMER_NOT_FOUND"));
		LocalDateTime startDateTime = LocalDate.parse(startDate).atStartOfDay();
		LocalDateTime endDateTime = LocalDate.parse(endDate).atTime(LocalTime.MAX);
		
		List<Transaction> transactionList = transactionRepository.findByCustomerCustomerIdAndTransactionDateBetween(customerId, Timestamp.valueOf(startDateTime), Timestamp.valueOf(endDateTime));
		if(transactionList.isEmpty()) {
			
			throw new RewardPointsException("Service.TRANSACTIONS_NOT_FOUND");
		}
		 Map<YearMonth, List<Transaction>> groupedBymonths = transactionList.stream()
		            .collect(Collectors.groupingBy(t -> YearMonth.from(t.getTransactionDate().toLocalDateTime())));

		    List<MonthlyPointsDTO> monthlyRewards = new ArrayList<>();
		    int totalPoints = 0;

		    for (Map.Entry<YearMonth, List<Transaction>> entry : groupedBymonths.entrySet()) {
		        YearMonth ym = entry.getKey();
		        List<Transaction> monthTransactions = entry.getValue();

		        int monthPoints = monthTransactions.stream().mapToInt(t->t.getPointsEarned()).sum();

		        List<TransactionDTO> dtoList = monthTransactions.stream()
		                .map(tx -> modelMapper.map(tx, TransactionDTO.class))
		                .toList();

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


	@Override
	public List<CustomerRewardSummaryDTO> getAllCustomerRewardsByRetailerId(long retailerId, String startDate,
			String endDate) throws RewardPointsException {
		Optional<Retailer> optRetailer = retailerRepository.findById(retailerId);
		Retailer retailer = optRetailer.orElseThrow(()-> new RewardPointsException("Service.RETAILER_NOT_FOUND"));
		
		List<Customer> customers = customerRepository.findByRetailerRetailerId(retailerId);

		
		if(customers.isEmpty()) {
			throw new RewardPointsException("Service.CUSTOMER_NOT_FOUND");
		}
		LocalDateTime startDateTime = LocalDate.parse(startDate).atStartOfDay();
		LocalDateTime endDateTime = LocalDate.parse(endDate).atTime(LocalTime.MAX);
		
		List<CustomerRewardSummaryDTO>  summaryDTOs = new ArrayList<>();
		
		customers.stream().forEach(customer->{
			List<Transaction> transactionList = transactionRepository.findByCustomerCustomerIdAndTransactionDateBetween(customer.getCustomerId(), Timestamp.valueOf(startDateTime), Timestamp.valueOf(endDateTime));
			if(!transactionList.isEmpty()) {
				
			 Map<YearMonth, List<Transaction>> groupedBymonths = transactionList.stream()
			            .collect(Collectors.groupingBy(t -> YearMonth.from(t.getTransactionDate().toLocalDateTime())));

			    List<MonthlyPointsDTO> monthlyRewards = new ArrayList<>();
			    int totalPoints = 0;

			    for (Map.Entry<YearMonth, List<Transaction>> entry : groupedBymonths.entrySet()) {
			        YearMonth ym = entry.getKey();
			        List<Transaction> monthTransactions = entry.getValue();

			        int monthPoints = monthTransactions.stream().mapToInt(t->t.getPointsEarned()).sum();

			        List<TransactionDTO> dtoList = monthTransactions.stream()
			                .map(tx -> modelMapper.map(tx, TransactionDTO.class))
			                .toList();

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
			    summaryDTOs.add(response);
		}
		
		});
		
		if(summaryDTOs.isEmpty()) {
			throw new RewardPointsException("SService.TRANSACTIONS_NOT_FOUND");
		}
		return summaryDTOs;
	}





	

	


	


	

}
