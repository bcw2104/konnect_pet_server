package com.konnect.pet.dto;

import com.konnect.pet.enums.PlatformType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SocialUserInfoDto {
	private String email;
	private PlatformType platformType;
}
