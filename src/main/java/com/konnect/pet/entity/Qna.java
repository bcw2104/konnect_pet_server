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
public class Qna extends BaseAutoSetEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "qna_id")
	private Long id;

	@Column(length = 255, nullable = false)
	private String title;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String question;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String answer;

	@Column(length = 3, nullable = false)
	private String category;

	private Long answeredBy;

	@Column(nullable = false)
	private LocalDateTime answeredDate;

}
