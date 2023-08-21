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
public enum PetTypeCode implements EnumCode{
	DAY("001","Dog"),
	WEEK("002","Cat"),


	INFINITY("999","Infinity");

	private String code;
	private String codeName;

	private static final Map<String, PetTypeCode> enumMap = Collections.unmodifiableMap(
			Stream.of(values()).collect(Collectors.toMap(PetTypeCode::getCode, Function.identity())));


	private PetTypeCode(String code, String codeName) {
		this.code = code;
		this.codeName = codeName;
	}

	public static PetTypeCode findByCode(String code) {
		return enumMap.get(code);
	}

	@Override
	public String getCodeForApp() {
		// TODO Auto-generated method stub
		return null;
	}
}
