package com.infy.rewardpoints.utility;

import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.infy.rewardpoints.exception.RewardPointsException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

/**
 * Global exception handler for REST controllers. Converts various exceptions
 * into structured error responses using {@link ErrorInfo}.
 */
@RestControllerAdvice
public class ExceptionControllerAdvice {
	private static final Log LOGGER = LogFactory.getLog(ExceptionControllerAdvice.class);
	@Autowired
	private Environment environment;
	/**
	 * Handles custom {@link RewardPointsException} and maps to a user-friendly
	 * error message.
	 *
	 * @param exception the custom exception thrown by business logic
	 * @return ResponseEntity with error message and BAD_REQUEST status
	 */
	@ExceptionHandler(RewardPointsException.class)
	public ResponseEntity<ErrorInfo> handleRewardPointsException(RewardPointsException exception) {
		LOGGER.error(exception.getMessage(), exception);
		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());
		errorInfo.setErrorMessage(environment.getProperty(exception.getMessage(), exception.getMessage()));
		return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handles bean validation exceptions from request body and path/query params.
	 *
	 * @param exception the validation-related exception
	 * @return ResponseEntity with joined error messages and BAD_REQUEST status
	 */
	@ExceptionHandler({ MethodArgumentNotValidException.class, ConstraintViolationException.class })
	public ResponseEntity<ErrorInfo> handleValidationExceptions(Exception exception) {
		LOGGER.error(exception.getMessage(), exception);
		String errorMsg;
		if (exception instanceof MethodArgumentNotValidException manvException) {
			errorMsg = manvException.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage)
					.map(errorCode -> environment.getProperty(errorCode, errorCode)).collect(Collectors.joining(", "));
		} else {
			ConstraintViolationException cvException = (ConstraintViolationException) exception;
			errorMsg = cvException.getConstraintViolations().stream().map(ConstraintViolation::getMessage)
					.map(errorCode -> environment.getProperty(errorCode, errorCode)).collect(Collectors.joining(", "));
		}

		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());
		errorInfo.setErrorMessage(errorMsg);
		return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handles constraint violations like duplicate keys or null values in DB
	 * fields.
	 *
	 * @param ex the DataIntegrityViolationException thrown by persistence layer
	 * @return ResponseEntity with conflict-level error message
	 */
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorInfo> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
		LOGGER.error("Data integrity violation", ex);
		String rootMessage = ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();
		String errorMessage;
		if (rootMessage != null && rootMessage.contains("Duplicate entry")
				&& rootMessage.contains("transaction.transaction_number")) {
			errorMessage = environment.getProperty("Transaction.DUPLICATE_TRANSACTION_NUMBER",
					"Duplicate transaction number");
		} else {
			errorMessage = environment.getProperty("Transaction.DATA_INTEGRITY", "Database integrity violation");
		}
		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setErrorCode(HttpStatus.CONFLICT.value());
		errorInfo.setErrorMessage(errorMessage);
		return new ResponseEntity<>(errorInfo, HttpStatus.CONFLICT);
	}
}
