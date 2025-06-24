package com.infy.rewardpoints.models;

import java.math.BigDecimal;
import java.sql.Timestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

/**
 * Entity representing purchase transaction made by customer. Points are
 * calculated based on the transaction amount and recorded with the transaction.
 */
@Data
@Entity
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long transactionId;
	private String transactionMode;
	private String transactionNumber;
	private BigDecimal amount;
	private Timestamp transactionDate;
	private int pointsEarned;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "customer_id")
	private Customer customer;
}
