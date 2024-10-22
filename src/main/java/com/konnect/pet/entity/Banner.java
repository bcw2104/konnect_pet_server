package com.konnect.pet.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

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
public class Banner extends BaseAutoSetAdminEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "banner_id")
	private Long id;

	@Column(length = 255)
	private String imgPath;

	@Column(length = 255)
	private String landingUrl;

	private LocalDateTime startDate;

	private LocalDateTime endDate;

	private boolean activeYn;

	@ColumnDefault("0")
	private boolean sortOrder;

	@Column(length = 255)
	private String description;
}
