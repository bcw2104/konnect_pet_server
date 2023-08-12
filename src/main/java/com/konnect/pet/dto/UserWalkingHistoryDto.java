package com.konnect.pet.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.konnect.pet.entity.UserWalkingFootprintCatchHistory;
import com.konnect.pet.entity.UserWalkingHistory;
import com.konnect.pet.entity.UserWalkingRewardHistory;
import com.konnect.pet.enums.code.PointTypeCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserWalkingHistoryDto {
	private Long id;

	private int meters;

	private int seconds;

	private String routes;

	private LocalDateTime startDate;

	private LocalDateTime endDate;

	private Integer maxFootprintAmount;

	private List<EnumCodeDto> pointTypes;

	private List<UserWalkingRewardHistoryDto> rewardHistories;

	private List<UserWalkingFootprintCatchHistoryDto> footprintCatchHistories;

	public UserWalkingHistoryDto(UserWalkingHistory history, Integer maxFootprintAmount) {
		this.id = history.getId();
		this.meters = history.getMeters();
		this.seconds = history.getSeconds();
		this.routes = history.getRoutes();
		this.startDate = history.getStartDate();
		this.endDate = history.getEndDate();
		this.maxFootprintAmount = maxFootprintAmount;
		this.pointTypes = PointTypeCode.enumList.stream().map(EnumCodeDto::new).toList();
		this.rewardHistories = history.getRewardHistories().stream().map(UserWalkingRewardHistoryDto::new).toList();
		this.footprintCatchHistories = history.getFootprintCatchHistories().stream()
				.map(UserWalkingFootprintCatchHistoryDto::new).toList();
	}

}
