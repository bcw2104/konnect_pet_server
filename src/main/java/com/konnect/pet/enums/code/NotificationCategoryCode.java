package com.konnect.pet.enums.code;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Getter;

/*
 * sms 또는 email 인증이 발생하는 구역
 */
@Getter
public enum NotificationCategoryCode implements EnumCode {
	ACCOUNT("001", "Account", "Account"),
	WALKING("002", "Walking", "Walking"),
	SHOPING("003", "Shoping","Shoping"),
	COMMUNITY("004", "Community","Community"),
	NOTICE("998", "Notice","Notice"),
	MARKETING("999", "Marketing","Marketing");

	private String code;
	private String codeName;
	private String codeForApp;

	private static final Map<String, NotificationCategoryCode> enumMap = Collections.unmodifiableMap(
			Stream.of(values()).collect(Collectors.toMap(NotificationCategoryCode::getCode, Function.identity())));

	private NotificationCategoryCode(String code, String codeName, String codeForApp) {
		this.code = code;
		this.codeName = codeName;
		this.codeForApp = codeForApp;
	}

	public static NotificationCategoryCode findByCode(String code) {
		return enumMap.get(code);
	}

}
