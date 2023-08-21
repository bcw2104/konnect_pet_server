package com.konnect.pet.enums.code;

import lombok.Getter;

/*
 * sms 또는 email 인증이 발생하는 구역
 */
@Getter
public enum LandingTypeCode implements EnumCode {
	INAPP("001", "in app"), OUTLINK("002", "out link");

	private String code;
	private String codeName;

	private LandingTypeCode(String code, String codeName) {
		this.code = code;
		this.codeName = codeName;
	}

	@Override
	public String getCodeForApp() {
		// TODO Auto-generated method stub
		return null;
	}
}
