package com.konnect.pet.dto;

import com.konnect.pet.entity.User;
import com.konnect.pet.entity.UserNotification;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserNotificationDto {
	private Long notiId;

	private String category;

	private String title;

	private String content;

	private String notiType;

	private String landingType;

	private String landingUrl;

	private boolean visitedYn;

	public UserNotificationDto(Long notiId, String category, String title, String content,
			String notiType, String landingType, String landingUrl, boolean visitedYn) {
		this.notiId = notiId;
		this.category = category;
		this.title = title;
		this.content = content;
		this.notiType = notiType;
		this.landingType = landingType;
		this.landingUrl = landingUrl;
		this.visitedYn = visitedYn;
	}

}
