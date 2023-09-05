package com.konnect.pet.aop;

import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.konnect.pet.annotation.DistributedLock;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.ex.CustomResponseException;
import com.konnect.pet.utils.CustomSpringELParser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAop {
	private static final String REDISSON_LOCK_PREFIX = "DLOCK:";
	private final RedissonClient redissonClient;
	private final AopForTransaction aopForTransaction;
	
	@Around("@annotation(distributedLock)")
	public Object lock(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		String key = REDISSON_LOCK_PREFIX + distributedLock.prefix() + CustomSpringELParser
				.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), distributedLock.key());
		log.info(key);
		RLock lock = redissonClient.getLock(key);
		try {
			// 잠금 시도: 지정된 대기 시간 동안 잠금을 획득하려고 시도
			if (!lock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), TimeUnit.SECONDS)) {
				log.info("Failed to acquire lock.");
				return null;
			}

			log.info("Hold lock.");
			// 잠금 획득 성공 시, 대상 메소드 실행
			return aopForTransaction.proceed(joinPoint);
		} finally {
			// 현재 스레드가 잠금을 보유하고 있는지 확인 후 잠금 해제
			if (lock.isLocked() && lock.isHeldByCurrentThread()) {
				log.info("Release lock.");
				lock.unlock();
			}
		}

	}
}
