package com.konnect.pet.enums.code;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Getter;

@Getter
public enum ProcessStatusCode {
	NONE("000", "NONE"),
	PENDING("001", "Pending"),
	PERMITTED("002", "Permitted"),
	DENIED("003", "Denied"),
	CANCELED("004", "Canceled"),
	FORBIDDEN("005", "Forbidden"),

	UNSET("999", "Unset");
	private String code;
	private String codeName;

	private static final Map<String, ProcessStatusCode> enumMap = Collections.unmodifiableMap(
			Stream.of(values()).collect(Collectors.toMap(ProcessStatusCode::getCode, Function.identity())));

	private ProcessStatusCode(String code, String codeName) {
		this.code = code;
		this.codeName = codeName;
	}

	public static ProcessStatusCode findByCode(String code) {
		return enumMap.get(code);
	}
}
