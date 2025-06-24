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
