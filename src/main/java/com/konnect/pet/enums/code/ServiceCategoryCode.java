package com.konnect.pet.enums.code;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Getter;

/*
 * sms 또는 email 인증이 발생하는 구역
 */
@Getter
public enum ServiceCategoryCode implements EnumCode {
	ACCOUNT("001", "Account", "Account"), WALKING("002", "Walking", "Walking"), SHOPING("003", "Shoping", "Shoping"),
	COMMUNITY("004", "Community", "Community"), ETC("999", "ETC", "Etc");

	private String code;
	private String codeName;
	private String codeForApp;

	private static final Map<String, ServiceCategoryCode> enumMap = Collections.unmodifiableMap(
			Stream.of(values()).collect(Collectors.toMap(ServiceCategoryCode::getCode, Function.identity())));

	public static final List<ServiceCategoryCode> enumList = Collections
			.unmodifiableList(Stream.of(values()).collect(Collectors.toList()));

	private ServiceCategoryCode(String code, String codeName, String codeForApp) {
		this.code = code;
		this.codeName = codeName;
		this.codeForApp = codeForApp;
	}

	public static ServiceCategoryCode findByCode(String code) {
		return enumMap.get(code);
	}

}
