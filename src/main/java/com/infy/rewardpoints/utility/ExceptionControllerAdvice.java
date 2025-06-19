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
	
}