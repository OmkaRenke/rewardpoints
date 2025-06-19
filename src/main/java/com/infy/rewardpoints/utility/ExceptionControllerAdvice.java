package com.infy.rewardpoints.utility;

import org.apache.commons.logging.LogFactory;

import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.infy.rewardpoints.exception.RewardPointsException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class ExceptionControllerAdvice{
	
	private static final Log LOGGER = LogFactory.getLog(ExceptionControllerAdvice.class);
	
	@Autowired
	private Environment environment;
	
	@ExceptionHandler(RewardPointsException.class)
	public ResponseEntity<ErrorInfo> RewardPointsExceptionHandler(RewardPointsException exception){
		LOGGER.error(exception.getMessage(),exception);
		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());
		errorInfo.setErrorMessage(environment.getProperty(exception.getMessage()));
		
		return new ResponseEntity<>(errorInfo,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({MethodArgumentNotValidException.class,ConstraintViolationException.class})
	public ResponseEntity<ErrorInfo> validatorExeptionHandler(Exception exception){
		LOGGER.error(exception.getMessage(),exception);
		String errorMsg;
		if(exception instanceof MethodArgumentNotValidException manvException) {
			manvException = (MethodArgumentNotValidException) exception;
			errorMsg = manvException.getBindingResult().getAllErrors()
						.stream().map(ObjectError::getDefaultMessage)
						.collect(Collectors.joining(", "));
		}
		else {
			ConstraintViolationException cvException =(ConstraintViolationException) exception;
			errorMsg = cvException.getConstraintViolations().stream()
						.map(ConstraintViolation::getMessage)
						.collect(Collectors.joining(", "));
		}
		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());
		errorInfo.setErrorMessage(errorMsg);
		
		return new ResponseEntity<>(errorInfo,HttpStatus.BAD_REQUEST);
		
	}
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorInfo> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
	    LOGGER.error("Data integrity violation", ex);

	    String rootMessage = ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();

	    String errorMessage;
	    if (rootMessage.contains("Duplicate entry") && rootMessage.contains("transaction.transaction_number")) {
	    	errorMessage = environment.getProperty("Transaction.DUPLICATE_TRANSACTION_NUMBER");
	    	 
	    } else {
	    	errorMessage = environment.getProperty("Transaction.DATA_INTEGRITY");
	    }

	    ErrorInfo errorInfo = new ErrorInfo();
	    errorInfo.setErrorCode(HttpStatus.CONFLICT.value()); 
	    errorInfo.setErrorMessage(errorMessage);

	    return new ResponseEntity<>(errorInfo, HttpStatus.CONFLICT);
	}
	
}