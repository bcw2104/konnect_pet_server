package com.konnect.pet.dto;

import com.konnect.pet.entity.Properties;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PropertiesDto {
	private String pKey;

	private String pValue;

	public PropertiesDto(Properties properties) {
		this.pKey = properties.getPKey();
		this.pValue = properties.getPValue();
	}


}
