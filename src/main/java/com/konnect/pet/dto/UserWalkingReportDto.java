package com.konnect.pet.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.konnect.pet.entity.UserWalkingHistory;
import com.konnect.pet.entity.UserWalkingRewardHistory;
import com.konnect.pet.enums.code.PointTypeCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserWalkingReportDto {
	private Long id;

	private int meters;

	private int seconds;

	private String routes;

	private LocalDateTime startDate;

	private LocalDateTime endDate;

	private List<UserWalkingRewardDto> rewardHistories;

	public UserWalkingReportDto(UserWalkingHistory history) {
		this.id = history.getId();
		this.meters = history.getMeters();
		this.seconds = history.getSeconds();
		this.routes = history.getRoutes();
		this.startDate = history.getStartDate();
		this.endDate = history.getEndDate();
		this.rewardHistories = history.getRewardHistories().stream().map(UserWalkingRewardDto::new).toList();
	}

}

@Getter
@Setter
class UserWalkingRewardDto {
	private int amount;

	private boolean paymentYn;

	private String pointType;
	private String pointTypeName;
	private String policyName;

	public UserWalkingRewardDto(UserWalkingRewardHistory history) {
		PointTypeCode pointTypeCode = PointTypeCode.findByCode(history.getPointType());
		this.amount = history.getAmount();
		this.paymentYn = history.isPaymentYn();
		this.pointType = pointTypeCode.getCodeForApp();
		this.pointTypeName = pointTypeCode.getCodeName();
		this.policyName = history.getPolicyName();
	}

}
