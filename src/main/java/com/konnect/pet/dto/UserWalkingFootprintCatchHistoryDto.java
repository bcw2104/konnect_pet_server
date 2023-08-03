package com.konnect.pet.dto;

import java.time.LocalDateTime;

import com.konnect.pet.entity.UserWalkingFootprintCatchHistory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserWalkingFootprintCatchHistoryDto {
	private Long footprintId;
	private LocalDateTime createdDate;

	public UserWalkingFootprintCatchHistoryDto(UserWalkingFootprintCatchHistory history) {
		this.footprintId = history.getUserWalkingFootprint().getId();
		this.createdDate = history.getCreatedDate();
	}

}
