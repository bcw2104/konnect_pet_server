package com.konnect.pet.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

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

	@Column(columnDefinition = "TEXT")
	private String answer;

	@Column(length = 3, nullable = false)
	private String category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_QNA-USER_ID"), nullable = false)
	private User user;
	
	private Long answeredBy;

	private LocalDateTime answeredDate;

}
