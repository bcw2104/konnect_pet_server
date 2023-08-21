package com.konnect.pet.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class AppVersion extends BaseAutoSetAdminEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "version_id")
	private Long id;

	@Column(length = 5, nullable = false)
	private String version;

	private boolean forcedYn;

	private LocalDateTime releasedDate;

}
