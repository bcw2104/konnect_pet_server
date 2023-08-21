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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Terms extends BaseAutoSetEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "terms_id")
	private Long id;

	@Column(length = 30, nullable = false)
	private String termsName;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String termsContent;

	private boolean visibleYn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "terms_group_id", foreignKey = @ForeignKey(name = "FK_terms_terms_group"), nullable = false)
	private TermsGroup termsGroup;

	@Column(updatable = false, nullable = false)
	private Long createdBy;

	@Column(nullable = false)
	private Long lastModifiedBy;
}
