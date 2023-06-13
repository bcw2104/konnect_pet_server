package com.konnect.pet.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class CommonCode extends BaseAutoSetEntity{

	@Id
	@Column(length = 5)
	private String code;
	@Column(length = 5)
	private String upperCode;
	@Column(length = 20, nullable = false)
	private String codeName;
	@Column(length = 30)
	private String codeDescription;
}
