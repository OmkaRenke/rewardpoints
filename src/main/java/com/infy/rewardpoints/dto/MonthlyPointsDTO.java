package com.infy.rewardpoints.dto;

import java.util.List;

import lombok.Data;

@Data
public class MonthlyPointsDTO {
	private String month; 
    private int points;
    private List<TransactionDTO> transactioList;
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public List<TransactionDTO> getTransactioList() {
		return transactioList;
	}
	public void setTransactioList(List<TransactionDTO> transactioList) {
		this.transactioList = transactioList;
	}
    
}
