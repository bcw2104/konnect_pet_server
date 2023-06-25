package com.konnect.pet.dto;

import com.konnect.pet.entity.User;

import lombok.Getter;

import lombok.Setter;

@Setter
@Getter
public class UserSimpleDto {
	private String email;
	private String platform;
	private Long userId;

	public UserSimpleDto(User user) {
		this.userId = user.getId();
		this.email = user.getEmail();
		this.platform = user.getPlatform().name();
	}
}
