package com.infy.rewardpoints.models;

import java.util.List;

import lombok.Data;

/**
 * DTO representing the reward points earned by a customer in a specific month.
 * Includes the month's name, total points for that month, and a list of related
 * transactions.
 */
@Data
public class MonthlyPointsMapper {
	private String month;
	private int points;
	/**
	 * List of transactions made by the customer in this month.
	 */
	private List<TransactionResponseMapper> transactioList;
}
