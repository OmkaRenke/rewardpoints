package com.infy.rewardpoints.entity;

import java.math.BigDecimal;
import java.security.Timestamp;

import com.infy.rewardpoints.dto.CustomerDTO;
import com.infy.rewardpoints.dto.RetailerDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long transactionId;
	
	private String transactionNumber;
	private BigDecimal amount;
	private Timestamp transactionDate;
	private int pointsEarned;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "retailer_id")
	private Retailer retailer;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "customer_id")
	private Customer customer;
}
