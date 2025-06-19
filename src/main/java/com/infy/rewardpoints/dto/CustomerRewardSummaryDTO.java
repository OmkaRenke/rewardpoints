package com.infy.rewardpoints.dto;

import java.util.List;

import lombok.Data;

@Data
public class CustomerRewardSummaryDTO {
	    private Long customerId;
	    private String customerName;
	    private List<MonthlyPointsDTO> monthlyRewards;
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
		public List<MonthlyPointsDTO> getMonthlyRewards() {
			return monthlyRewards;
		}
		public void setMonthlyRewards(List<MonthlyPointsDTO> monthlyRewards) {
			this.monthlyRewards = monthlyRewards;
		}
		public int getTotalPoints() {
			return totalPoints;
		}
		public void setTotalPoints(int totalPoints) {
			this.totalPoints = totalPoints;
		}
	    
}
