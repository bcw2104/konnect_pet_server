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

	private String termsGroupName;

	private String termsGroupContent;

	private boolean requiredYn;

	public TermsGroupDto(Long termsGroupId, String termsGroupName, String termsGroupContent, boolean requiredYn) {
		this.termsGroupId = termsGroupId;
		this.termsGroupName = termsGroupName;
		this.termsGroupContent = termsGroupContent;
		this.requiredYn = requiredYn;
	}
}
