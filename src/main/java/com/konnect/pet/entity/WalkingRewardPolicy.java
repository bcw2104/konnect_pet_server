package com.konnect.pet.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class WalkingRewardPolicy extends BaseAutoSetAdminEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="policy_id")
	private Long id;

	@Column(length = 200)
	private String policyName;

	@Column(length = 3, nullable = false)
	private String rewardProvideType;

	private LocalDateTime rewardProvideDate;

	@Column(length = 3, nullable = false)
	private String rewardType;

	@Column(length = 3, nullable = false)
	private String pointType;

	private int maxRewardAmountPerWalking;

	private int maxRewardAmountPerPeriod;

	@Column(length = 3, nullable = false)
	private String peroidType;

	private int pointPerUnit;

	private int provideUnit;

	private LocalDateTime startDate;

	private LocalDateTime endDate;

	private boolean activeYn;

	@ColumnDefault("0")
	private int sortOrder;

}
