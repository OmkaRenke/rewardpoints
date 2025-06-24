package com.infy.rewardpoints.utility;

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

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

}
