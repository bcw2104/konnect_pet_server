package com.konnect.pet.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
public @interface DistributedLock {
	/**
	 * 잠금 키를 지정한다. Spring EL 표현식을 사용하여 동적인 값을 지정한다. 예: "#paramName"
	 *
	 * @return 잠금 키 문자열
	 */
	String key() default "";

	/**
	 * 잠금을 획득하기 위해 대기할 최대 시간(초)을 지정.
	 *
	 * @return 대기 시간 (단위: 초)
	 */
	long waitTime() default 5;

	/**
	 * 잠금을 보유할 최대 시간(초)을 지정.
	 *
	 * @return 보유 시간 (단위: 초)
	 */
	long leaseTime() default 1;
}
