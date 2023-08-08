package com.konnect.pet.enums.code;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Getter;

@Getter
public enum UserStatusCode {
	ACTIVE("001", "Active"),
	SLEEP("002", "Sleep"),

	REMOVED("999", "Removed");

	private String code;
	private String codeName;

	private static final Map<String, UserStatusCode> enumMap = Collections.unmodifiableMap(
			Stream.of(values()).collect(Collectors.toMap(UserStatusCode::getCode, Function.identity())));

	private UserStatusCode(String code, String codeName) {
		this.code = code;
		this.codeName = codeName;
	}

	public static UserStatusCode findByCode(String code) {
		return enumMap.get(code);
	}
}
