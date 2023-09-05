package com.konnect.pet.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
public @interface ExecutionTimer {

	/**
	 * @return 작업 명
	 */
	String task() default "";
}
