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
public class UserWalkingHistoryListDto {
	private Long id;

	private int meters;

	private int seconds;

	private LocalDateTime startDate;

	private LocalDateTime endDate;

	public UserWalkingHistoryListDto(UserWalkingHistory history) {
		this.id = history.getId();
		this.meters = history.getMeters();
		this.seconds = history.getSeconds();
		this.startDate = history.getStartDate();
		this.endDate = history.getEndDate();
	}

}
