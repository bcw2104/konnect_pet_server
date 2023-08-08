package com.konnect.pet.enums.code;

import lombok.Getter;

/*
 * sms 또는 email 인증이 발생하는 구역
 */
@Getter
public enum NotificationCategoryCode implements CommonCode{
	ACCOUNT("001","account"),
	WALKING("002","walking"),
	SHOPING("003","shoping"),
	COMMUNITY("004","community"),
	MARKETING("999","marketing");

	private String code;
	private String codeName;

	private NotificationCategoryCode(String code, String codeName) {
		this.code = code;
		this.codeName = codeName;
	}
}
