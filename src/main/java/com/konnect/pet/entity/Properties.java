package com.konnect.pet.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Properties {

	@Id
	@Column(length = 50)
	private String pKey;

	@Column(length = 50)
	private String pKeyGroup;

	@Column(length = 200, nullable = false)
	private String pValue;

	@Column(length = 100)
	private String description;
}
