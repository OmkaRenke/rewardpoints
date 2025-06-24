package com.infy.rewardpoints.models;

import java.math.BigDecimal;
import java.sql.Timestamp;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Data Transfer Object for Customer.
 */
@Data
public class TransactionDTO {
	private long transactionId;
	@NotBlank(message = "transaction.number.required")
	private String transactionNumber;

	@NotBlank(message = "transaction.mode.required")
	private String transactionMode;

	@NotNull(message = "transaction.amount.required")
	@DecimalMin(value = "0.01", message = "transaction.amount.min")
	private BigDecimal amount;

	@NotNull(message = "transaction.date.required")
	private Timestamp transactionDate;

	@Min(value = 0, message = "transaction.points.invalid")
	private int pointsEarned;

	@NotNull(message = "transaction.customer.required")
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

	public CustomerDTO getCustomerDTO() {
		return customerDTO;
	}

	public void setCustomerDTO(CustomerDTO customerDTO) {
		this.customerDTO = customerDTO;
	}

	public String getTransactionMode() {
		return transactionMode;
	}

	public void setTransactionMode(String transactionMode) {
		this.transactionMode = transactionMode;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

}
