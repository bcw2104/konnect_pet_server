package com.konnect.pet.enums;

public enum ResponseType {


	SUCCESS("1000", "success","성공"),

	// 내부 이슈로 실패 90XX
	FAIL("9000", "failed","잠시 후 다시 시도해주세요."),
	SERVER_ERROR("9001", "internal server error","잠시 후 다시 시도해주세요."),

	// 요청 이슈로 실패 91XX
	INVALID_REQUEST("9101", "invalid request","잠시 후 다시 시도해주세요."),
	INVALID_PARAMETER("9102", "invalid parameter", "잠시 후 다시 시도해주세요."),

	//인증 실패 92XX
	AUTH_FAIL("9200", "auth failed", "인증에 실패했습니다."),
	UNAUTHORIZED("9201", "unauthorized", "접근할 수 없는 서비스입니다."),
	INVALID_ACCESS_TOKEN("9202", "invalid access token", "유효하지 않은 토큰입니다."),
	INVALID_REFRESH_TOKEN("9203", "invalid refresh token", "유효하지 않은 토큰입니다."),
	EXPIRED_ACCESS_TOKEN("9202", "expired access token", "만료된 토큰입니다."),

	LOGIN_FAIL("9210", "login failed", "아이디 또는 비밀번호가 일치하지 않습니다."),
	VERIFY_FAIL("9211", "invalid verify code", "인증번호가 일치하지 않습니다."),

	DOMMY("10000", "dommy","dommy");

	private String code;
	private String message;
	private String displayMessage;

	private ResponseType(String code, String message,String displayMessage) {
		this.code = code;
		this.message = message;
		this.displayMessage = displayMessage;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public String getDisplayMessage() {
		return displayMessage;
	}



}
