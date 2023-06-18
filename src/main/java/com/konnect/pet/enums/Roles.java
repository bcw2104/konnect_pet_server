package com.konnect.pet.enums;

import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Roles implements GrantedAuthority{
	ROLE_LVL1("ROLE_LVL1","LVL1")		//일반 사용자
;
	private final String type;
	private final String level;

	@Override
	public String getAuthority() {
		return type;
	}
}
