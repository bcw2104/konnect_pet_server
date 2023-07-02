package com.konnect.pet.dto;

import java.util.Map;

import com.konnect.pet.enums.PlatformType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequestDto {

	private PlatformType platform;
	private String smsVerifyKey;
	private String emailVerifyKey;
	
	private String nationCode;

	private String password;

	private Map<Long, Object> termsAgreed;
}


