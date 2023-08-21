package com.konnect.pet.enums.code;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum WalkingRewardProvideTypeCode implements EnumCode {
	REALTIME("001", "RealTime", "REALTIME"),
	BATCH("002", "Batch", "BATCH");

	private String code;
	private String codeName;
	private String codeForApp;

	private static final Map<String, WalkingRewardProvideTypeCode> enumMap = Collections.unmodifiableMap(
			Stream.of(values()).collect(Collectors.toMap(WalkingRewardProvideTypeCode::getCode, Function.identity())));

	private WalkingRewardProvideTypeCode(String code, String codeName,String codeForApp) {
		this.code = code;
		this.codeName = codeName;
		this.codeForApp = codeForApp;
	}

	public static WalkingRewardProvideTypeCode findByCode(String code) {
		return enumMap.get(code);
	}
}
