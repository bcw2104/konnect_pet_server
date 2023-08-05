package com.konnect.pet.enums.code;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Getter;

@Getter
public enum PointHistoryTypeCode {
	WELCOME("001", "Welcome Bonus", "Welcome Bonus"),
	WALKING("002", "Walking Reward", "Walking Reward"),
	ATTENDANCE("003", "Attendance Bonus", "Attendance Bonus"),
	SHOPPING_USE("004", "Used in shopping ", "Used in shopping"),


	DOMMY("999", "Dommy", "Dommy");

	private String code;
	private String codeName;
	private String codeForApp;

	public static final Map<String, PointHistoryTypeCode> enumMap = Collections.unmodifiableMap(
			Stream.of(values()).collect(Collectors.toMap(PointHistoryTypeCode::getCode, Function.identity())));

	private PointHistoryTypeCode(String code, String codeName, String codeForApp) {
		this.code = code;
		this.codeName = codeName;
		this.codeForApp = codeForApp;
	}

	public static PointHistoryTypeCode findByCode(String code) {
		return enumMap.get(code);
	}
}
