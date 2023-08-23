package com.konnect.pet.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TermsGroupDto {
	private Long termsGroupId;

	private String name;

	private String content;

	private boolean requiredYn;

	public TermsGroupDto(Long termsGroupId, String name, String content, boolean requiredYn) {
		this.termsGroupId = termsGroupId;
		this.name = name;
		this.content = content;
		this.requiredYn = requiredYn;
	}
}
