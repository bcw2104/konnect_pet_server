package com.konnect.pet.enums.code;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Getter;

/*
 *
 */
@Getter
public enum RewardTypeCode implements EnumCode {
	WALKING("001", "WalkingReward", "Walking reward"),
	ATTENDANCE("002", "AttendanceReward","Attendance reward");

	private String code;
	private String codeName;
	private String codeForApp;

	private static final Map<String, RewardTypeCode> enumMap = Collections.unmodifiableMap(
			Stream.of(values()).collect(Collectors.toMap(RewardTypeCode::getCode, Function.identity())));

	private RewardTypeCode(String code, String codeName, String codeForApp) {
		this.code = code;
		this.codeName = codeName;
		this.codeForApp = codeForApp;
	}

	public static RewardTypeCode findByCode(String code) {
		return enumMap.get(code);
	}
}
