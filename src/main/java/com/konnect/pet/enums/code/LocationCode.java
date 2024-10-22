package com.konnect.pet.enums.code;

import lombok.Getter;

/*
 * sms 또는 email 인증이 발생하는 구역
 */
@Getter
public enum LocationCode implements EnumCode{
	SIGNUP("001","회원가입"),
	PASSWORD_RESET("002","비밀번호 초기화"),
	LEAVE("003","회원 탈퇴");

	private String code;
	private String codeName;

	private LocationCode(String code, String codeName) {
		this.code = code;
		this.codeName = codeName;
	}

	@Override
	public String getCodeForApp() {
		// TODO Auto-generated method stub
		return null;
	}
}
