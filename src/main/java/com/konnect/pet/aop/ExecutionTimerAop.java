package com.konnect.pet.aop;

import java.time.LocalDateTime;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.konnect.pet.annotation.ExecutionTimer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Aspect
public class ExecutionTimerAop {

	@Around("@annotation(executionTimer)")
	public Object timer(ProceedingJoinPoint joinPoint, ExecutionTimer executionTimer) throws Throwable {
		long start = System.currentTimeMillis();
		log.info("{} is started at {}", executionTimer.task(), LocalDateTime.now());

		try {
			return joinPoint.proceed();
		} finally {
			long end = System.currentTimeMillis();
			log.info("{} is finished at {} - time: {}ms", executionTimer.task(), LocalDateTime.now(), end - start);
		}
	}
}
