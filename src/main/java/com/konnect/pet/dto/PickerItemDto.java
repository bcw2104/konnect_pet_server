package com.konnect.pet.dto;

import com.konnect.pet.entity.CommonCode;
import com.konnect.pet.enums.code.EnumCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * Picker Item For React Native Pricker
 */
@Getter
@Setter
@NoArgsConstructor
public class PickerItemDto {
	private String label;
	private String value;

	
	public PickerItemDto(CommonCode commonCode) {
		this.label = commonCode.getCodeName();
		this.value = commonCode.getCodePair().getCode();
	}
	
	public PickerItemDto(EnumCode enumCode) {
		this.label = enumCode.getCodeForApp() == null ? enumCode.getCodeName() : enumCode.getCodeForApp();
		this.value = enumCode.getCode();
	}

	public PickerItemDto(String label, String value) {
		this.label = label;
		this.value = value;
	}
}
