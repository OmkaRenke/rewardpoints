package com.infy.rewardpoints.models;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.Data;

/**
 * DTO used to return transaction details along with reward points earned. This
 * is used in customer reward summaries responses.
 */
@Data
public class TransactionResponseMapper {
	private long transactionId;
	private String transactionNumber;
	private String transactionMode;
	private BigDecimal amount;
	private Timestamp transactionDate;
	private int pointsEarned;
}
