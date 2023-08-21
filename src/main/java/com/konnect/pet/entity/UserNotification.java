package com.konnect.pet.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class UserNotification extends BaseAutoSetAdminEntity{

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="noti_id")
	private Long id;

	@Column(length = 3,nullable = false)
	private String category;

	@Column(length = 255,nullable = false)
	private String title;

	@Column(length = 255,nullable = false)
	private String content;

	private LocalDateTime startDate;

	private LocalDateTime endDate;

	private boolean activeYn;

	@Column(length = 3,nullable = false)
	private String notiType;

	@Column(length = 3)
	private String landingType;

	@Column(length = 255)
	private String landingUrl;
}
