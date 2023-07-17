package com.konnect.pet.dto;

import com.konnect.pet.entity.Properties;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PropertiesDto {
	private String name;

	private String value;

	public PropertiesDto(Properties properties) {
		this.name = properties.getPKey();
		this.value = properties.getPValue();
	}


}
