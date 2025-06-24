package com.infy.rewardpoints.exception;

/**
 * Custom exception class for handling reward points application errors. This
 * exception is thrown for business-level validation or service issues, such as
 * duplicate customers, invalid transactions, or missing data.
 */
public class RewardPointsException extends Exception {
	private static final long serialVersionUID = 1L;
	public RewardPointsException(String message) {
		super(message);
	}
}
