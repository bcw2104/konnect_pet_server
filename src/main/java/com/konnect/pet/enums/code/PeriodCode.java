package com.konnect.pet.enums.code;

import lombok.Getter;

/*
 * sms 또는 email 인증이 발생하는 구역
 */
@Getter
public enum PeriodCode implements EnumCode{
	DAY("001","Day"),
	WEEK("002","Week"),
	MONTH("003","Month"),


	INFINITY("999","Infinity");

	private String code;
	private String codeName;

	private PeriodCode(String code, String codeName) {
		this.code = code;
		this.codeName = codeName;
	}

	@Override
	public String getCodeForApp() {
		// TODO Auto-generated method stub
		return null;
	}
}
