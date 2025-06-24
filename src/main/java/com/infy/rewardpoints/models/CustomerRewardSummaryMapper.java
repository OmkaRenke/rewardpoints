package com.infy.rewardpoints.models;

import java.util.List;

import lombok.Data;

/**
 * DTO representing the reward summary of a customer. Includes customer details,
 * monthly reward points, and total points.
 */
@Data
public class CustomerRewardSummaryMapper {
	private Long customerId;
	private String customerName;
	/**
	 * List of monthly reward points earned by the customer. Each entry represents
	 * points earned in a specific month.
	 */
	private List<MonthlyPointsMapper> monthlyRewards;
	/**
	 * Total reward points earned across all months.
	 */
	private int totalPoints;
}
