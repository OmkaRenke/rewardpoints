package com.infy.rewardpoints.exception;

import lombok.Data;

/**
 * DTO representing error details to be returned in API responses. Useful for
 * returning structured error messages and codes to the client.
 */
@Data
public class ErrorInfo {
	/**
	 * Human-readable error message for the client.
	 */
	private String errorMessage;
	/**
	 * Application-specific error code (optional).
	 */
	private Integer errorCode;

}
