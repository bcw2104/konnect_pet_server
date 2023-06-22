package com.konnect.pet.enums.code;

import lombok.Getter;

/*
 * sms 또는 email 인증이 발생하는 구역
 */
@Getter
public enum VerifyLocationCode implements CommonCode{
	SIGNUP("001","회원가입");
	
	private String code;
	private String codeName;
	
	private VerifyLocationCode(String code, String codeName) {
		this.code = code;
		this.codeName = codeName;
	}
}
