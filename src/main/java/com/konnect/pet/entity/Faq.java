package com.konnect.pet.entity;

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
public class Faq extends BaseAutoSetAdminEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "faq_id")
	private Long id;

	@Column(length = 255, nullable = false)
	private String question;

	@Column(length = 1000, nullable = false)
	private String answer;

	@Column(length = 3, nullable = false)
	private String category;

	@ColumnDefault("true")
	private boolean activeYn;

	@ColumnDefault("0")
	private int sortOrder;
}
