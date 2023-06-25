package com.konnect.pet.dto;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TermsDto {

	private Long termsId;
	private Long termsGroupId;

	private String termsGroupName;
	private String termsName;
	private String termsContent;

	private boolean requiredYn;

	private LocalDateTime createdDate;

	public TermsDto(Long termsId, Long termsGroupId, String termsGroupName, String termsName, String termsContent,
			boolean requiredYn, LocalDateTime createdDate) {
		this.termsId = termsId;
		this.termsGroupId = termsGroupId;
		this.termsGroupName = termsGroupName;
		this.termsName = termsName;
		this.termsContent = termsContent;
		this.requiredYn = requiredYn;
		this.createdDate = createdDate;
	}
}
