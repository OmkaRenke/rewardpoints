package com.infy.rewardpoints.utility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.infy.rewardpoints.exception.RewardPointsException;

/**
 * Aspect for logging exceptions thrown from service implementation classes.
 * Helps trace errors consistently across the service layer.
 */
@Component
@Aspect
public class LoggingAspect {
	private static final Log LOGGER = LogFactory.getLog(LoggingAspect.class);

	/**
	 * Logs any RewardPointsException thrown by service implementation methods.
	 * 
	 * @param exception the exception thrown
	 */
	@AfterThrowing(pointcut = "execution (* com.infy.rewardpoints.service.*Impl.*(..))", throwing = "exception")
	public void logServiceException(RewardPointsException exception) {
		LOGGER.error(exception.getMessage(), exception);
	}
}
