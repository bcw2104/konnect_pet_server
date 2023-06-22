package com.konnect.pet.entity.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class CommonCodePair {

	@Column(length = 5)
	private String code;
	@Column(length = 10)
	private String codeGroup;
}
