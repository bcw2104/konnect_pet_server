package com.konnect.pet.dto;

import com.konnect.pet.entity.UserAppSetting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAppSettingDto {
	private boolean walkingYn;

	private boolean friendYn;

	private boolean messageYn;

	private boolean communityYn;

	private boolean serviceYn;

	public UserAppSettingDto(UserAppSetting setting) {
		this.walkingYn = setting.isWalkingYn();
		this.friendYn = setting.isFriendYn();
		this.messageYn = setting.isMessageYn();
		this.communityYn = setting.isCommunityYn();
		this.serviceYn = setting.isServiceYn();
	}

}
