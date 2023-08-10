package com.konnect.pet.dto;

import java.time.LocalDateTime;

import com.konnect.pet.entity.User;
import com.konnect.pet.entity.UserNotification;
import com.konnect.pet.enums.code.NotificationCategoryCode;

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
	private Long id;

	private String category;
	private String categoryName;

	private String title;

	private String content;

	private String notiType;

	private String landingType;

	private String landingUrl;

	private boolean visitedYn;

	private LocalDateTime createdDate;

	public UserNotificationDto(Long notiId, String category, String title, String content, String notiType,
			String landingType, String landingUrl, boolean visitedYn, LocalDateTime createdDate) {
		this.id = notiId;
		this.category = category;
		this.categoryName = NotificationCategoryCode.findByCode(category).getCodeForApp();
		this.title = title;
		this.content = content;
		this.notiType = notiType;
		this.landingType = landingType;
		this.landingUrl = landingUrl;
		this.visitedYn = visitedYn;
		this.createdDate = createdDate;
	}

}
