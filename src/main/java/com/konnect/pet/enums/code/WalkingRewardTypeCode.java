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
public enum WalkingRewardTypeCode implements CommonCode {
	DISTANCE("001", "Distance", "DISTANCE"),
	TIME("002", "Time", "TIME"),
	VISIT("003", "Visit", "VISIT");

	private String code;
	private String codeName;
	private String codeForApp;

	private static final Map<String, WalkingRewardTypeCode> enumMap = Collections.unmodifiableMap(
			Stream.of(values()).collect(Collectors.toMap(WalkingRewardTypeCode::getCode, Function.identity())));

	private WalkingRewardTypeCode(String code, String codeName, String codeForApp) {
		this.code = code;
		this.codeName = codeName;
		this.codeForApp = codeForApp;
	}

	public static WalkingRewardTypeCode findByCode(String code) {
		return enumMap.get(code);
	}
}
