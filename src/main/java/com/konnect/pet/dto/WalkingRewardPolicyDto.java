package com.konnect.pet.dto;

import java.time.LocalDateTime;

import com.konnect.pet.entity.WalkingRewardPolicy;
import com.konnect.pet.enums.code.PointTypeCode;
import com.konnect.pet.enums.code.WalkingRewardProvideTypeCode;
import com.konnect.pet.enums.code.WalkingRewardTypeCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalkingRewardPolicyDto {
	private Long id;

	private String policyName;

	private String rewardProvideType;

	private String rewardType;

	private String pointType;

	private Integer maxRewardAmountPerWalking;

	private Integer pointPerUnit;

	private Integer provideUnit;

	private LocalDateTime startDate;

	private LocalDateTime endDate;

	private boolean activeYn;

	public WalkingRewardPolicyDto(WalkingRewardPolicy rewardPolicy) {
		this.id = rewardPolicy.getId();
		this.policyName = rewardPolicy.getPolicyName();
		this.rewardProvideType = WalkingRewardProvideTypeCode.findByCode(rewardPolicy.getRewardProvideType()).getCodeForApp();
		this.rewardType = WalkingRewardTypeCode.findByCode(rewardPolicy.getRewardType()).getCodeForApp();
		this.pointType = PointTypeCode.findByCode(rewardPolicy.getPointType()).getCodeForApp();
		this.maxRewardAmountPerWalking = rewardPolicy.getMaxRewardAmountPerWalking();
		this.pointPerUnit = rewardPolicy.getPointPerUnit();
		this.provideUnit = rewardPolicy.getProvideUnit();
		this.startDate = rewardPolicy.getStartDate();
		this.endDate = rewardPolicy.getEndDate();
		this.activeYn = rewardPolicy.isActiveYn();
	}

}
