package com.konnect.pet.enums.code;

import lombok.Getter;

/*
 * sms 또는 email 인증이 발생하는 구역
 */
@Getter
public enum NotificationTypeCode implements CommonCode{
	WELCOME("001","welcome"),
	WAKLING_FINISHED("002","walking finished"),

	//커뮤니티 관련 코드
	REQUEST_FRIEND("100","friend request"),
	DUMMY("999","DUMMY");

	private String code;
	private String codeName;

	private NotificationTypeCode(String code, String codeName) {
		this.code = code;
		this.codeName = codeName;
	}
}
