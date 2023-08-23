package com.konnect.pet.entity;


import java.util.ArrayList;
import java.util.List;

import com.konnect.pet.enums.code.LocationCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class TermsGroup extends BaseAutoSetEntity{

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "terms_group_id")
	private Long id;

	@Column(length = 30, nullable = false)
	private String name;

	@Column(length = 100)
	private String content;

	private boolean requiredYn;

	private boolean visibleYn;

	@Column(length = 3, nullable = false)
	private String locationCode;

	private int sortOrder;

	@Builder.Default
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "termsGroup")
	private List<Terms> terms = new ArrayList<Terms>();

	@Column(updatable = false, nullable = false)
	private Long createdBy;

	@Column(nullable = false)
	private Long lastModifiedBy;
}
