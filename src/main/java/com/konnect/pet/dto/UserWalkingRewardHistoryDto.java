package com.konnect.pet.dto;

import com.konnect.pet.entity.UserWalkingRewardHistory;
import com.konnect.pet.enums.code.PointTypeCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserWalkingRewardHistoryDto {
	private int amount;

	private boolean paymentYn;

	private String pointType;
	private String pointTypeName;
	private String pointTypeSymbol;

	private String policyName;

	public UserWalkingRewardHistoryDto(UserWalkingRewardHistory history) {
		PointTypeCode pointTypeCode = PointTypeCode.findByCode(history.getPointType());
		this.amount = history.getAmount();
		this.paymentYn = history.isPaymentYn();
		this.pointType = pointTypeCode.getCode();
		this.pointTypeName = pointTypeCode.getCodeName();
		this.pointTypeSymbol = pointTypeCode.getCodeForApp();
		this.policyName = history.getWalkingRewardPolicy().getPolicyName();
	}

}