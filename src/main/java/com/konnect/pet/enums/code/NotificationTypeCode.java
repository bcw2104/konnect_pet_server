package com.konnect.pet.enums.code;

import lombok.Getter;

/*
 * sms 또는 email 인증이 발생하는 구역
 */
@Getter
public enum NotificationTypeCode implements CommonCode{
	WELCOME("001","welcome"),
	WAKLING_FINISHED("002","walking finished");

	private String code;
	private String codeName;

	private NotificationTypeCode(String code, String codeName) {
		this.code = code;
		this.codeName = codeName;
	}
}
