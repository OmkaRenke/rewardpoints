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

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public List<MonthlyPointsMapper> getMonthlyRewards() {
		return monthlyRewards;
	}

	public void setMonthlyRewards(List<MonthlyPointsMapper> monthlyRewards) {
		this.monthlyRewards = monthlyRewards;
	}

	public int getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}

}
