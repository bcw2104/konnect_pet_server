package com.konnect.pet.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class EventRewardPolicy extends BaseAutoSetAdminEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "event_id")
	private Long id;

	@Column(length = 300, nullable = false)
	private String title;

	@Column(length = 2000, nullable = false)
	private String content;

	private LocalDateTime startDate;

	private LocalDateTime endDate;

	private boolean activeYn;

	private int balance;

	@Column(length = 3, nullable = false)
	private String pointType;

	@Column(length = 3, nullable = false)
	private String historyType;

}
