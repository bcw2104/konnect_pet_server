package com.konnect.pet.utils;

import java.time.LocalDateTime;

import org.apache.commons.lang3.RandomStringUtils;

import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.ex.CustomResponseException;

public class ValidationUtils {


	public static String generateRandomString(int length, boolean useNumber, boolean useLetters) {
		return RandomStringUtils.random(length, useLetters, useNumber);
	}
	/**
	 * timestamp 검증 : Replay Attack방지를 위해서 생성된 시간부터 일정 시간이 지난 호출의 경우 요청 무효 처리(필수)
	 *
	 * @param timestamp
	 */
	public static void validationCheckTimestamp(LocalDateTime timestamp, int minute) {
		// minute 시간 이내의 요청만 허용
		if (timestamp.plusMinutes(minute).isBefore(LocalDateTime.now())) {
			throw new CustomResponseException(ResponseType.INVALID_REQUEST);
		}
	}
}
