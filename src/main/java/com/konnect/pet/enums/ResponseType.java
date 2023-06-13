package com.konnect.pet.enums;

public enum ResponseType implements EnumModel {


	SUCCESS("1000", "success"),

	// 내부 이슈로 실패 90XX
	FAIL("9000", "failed"),

	// 요청 이슈로 실패 91XX
	INVALID_REQUEST("9101", "invalid request"),
	INVALID_PARAMETER("9102", "invalid parameter"),

	//인증 실패 92XX
	AUTH_FAIL("9200", "auth failed"),
	UNAUTHORIZED("9201", "unauthorized"),
	INVALID_ACCESS_TOKEN("9202", "invalid access token"),
	INVALID_REFRESH_TOKEN("9203", "invalid refresh token"),

	DOMMY("10000", "dommy");

	private String code;
	private String message;

	private ResponseType(String code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public String getKey() {
		return this.code;
	}

	@Override
	public String getValue() {
		return this.message;
	}

}
