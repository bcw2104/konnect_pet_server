package com.konnect.pet.dto;

import com.konnect.pet.enums.code.CommonCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnumCodeDto {
	private String code;
	private String codeName;
	private String codeForApp;

	public EnumCodeDto(CommonCode code) {
		this.code = code.getCode();
		this.codeName = code.getCodeName();
		this.codeForApp = code.getCodeForApp();
	}

}