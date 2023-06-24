package com.konnect.pet.dto;

import com.konnect.pet.entity.CommonCode;

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
@AllArgsConstructor
public class PickerItemDto {
	private String label;
	private String value;

	public PickerItemDto(CommonCode commonCode) {
		this.label = commonCode.getCodeName();
		this.value = commonCode.getCodePair().getCode();
	}
}
