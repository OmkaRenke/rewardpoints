package com.infy.rewardpoints.utility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.infy.rewardpoints.exception.RewardPointsException;

@Component
@Aspect
public class LoggingAspect {
	private static final Log LOGGER = LogFactory.getLog(LoggingAspect.class);
	
	@AfterThrowing(pointcut = "execution (* com.infy.rewardpoints.service.*Impl.*(..))",throwing = "exception")
	public void logServiceException(RewardPointsException exception) {
		LOGGER.error(exception.getMessage(),exception);
	}
}
