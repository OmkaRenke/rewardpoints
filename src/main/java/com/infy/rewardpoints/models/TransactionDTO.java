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
	private CustomerDTO customerDTO;
}
