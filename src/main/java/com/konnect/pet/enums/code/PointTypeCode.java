package com.konnect.pet.enums.code;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Getter;

/*
 * 포인트 타
 */
@Getter
public enum PointTypeCode implements EnumCode {
	POINT("001", "Point", "P");

	private String code;
	private String codeName;
	private String codeForApp;

	public static final Map<String, PointTypeCode> enumMap = Collections.unmodifiableMap(
			Stream.of(values()).collect(Collectors.toMap(PointTypeCode::getCode, Function.identity())));

	public static final List<PointTypeCode> enumList = Collections.unmodifiableList(Stream.of(values()).toList());

	private PointTypeCode(String code, String codeName, String codeForApp) {
		this.code = code;
		this.codeName = codeName;
		this.codeForApp = codeForApp;
	}

	public static PointTypeCode findByCode(String code) {
		return enumMap.get(code);
	}
}
