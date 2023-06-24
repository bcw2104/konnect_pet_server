package com.konnect.pet.entity;

import com.konnect.pet.entity.embedded.CommonCodePair;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class CommonCode extends BaseAutoSetEntity{

	@Id @Embedded
	private CommonCodePair codePair;
	@Column(length = 20, nullable = false)
	private String codeName;
	@Column(length = 30)
	private String codeDescription;

	private int sortOrder;
}

