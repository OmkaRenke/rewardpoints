package com.infy.rewardpoints.dto;

import java.math.BigDecimal;
import java.security.Timestamp;

import lombok.Data;

@Data
public class TransactionDTO {
	private String transactionNumber;
	private BigDecimal amount;
	 private Timestamp transactionDate;
	 private int pointsEarned;
	private RetailerDTO retailerDTO;
	private CustomerDTO customerDTO;
	public String getTransactionNumber() {
		return transactionNumber;
	}
	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Timestamp getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Timestamp transactionDate) {
		this.transactionDate = transactionDate;
	}
	public int getPointsEarned() {
		return pointsEarned;
	}
	public void setPointsEarned(int pointsEarned) {
		this.pointsEarned = pointsEarned;
	}
	public RetailerDTO getRetailerDTO() {
		return retailerDTO;
	}
	public void setRetailerDTO(RetailerDTO retailerDTO) {
		this.retailerDTO = retailerDTO;
	}
	public CustomerDTO getCustomerDTO() {
		return customerDTO;
	}
	public void setCustomerDTO(CustomerDTO customerDTO) {
		this.customerDTO = customerDTO;
	}
	
}
