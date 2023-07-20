package com.konnect.pet.dto;

import com.konnect.pet.entity.UserWalkingFootprint;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserWalkingFootprintDto {
	private Long id;

	private Long userId;

	private double longitude;

	private double latitude;

	private int stock;

	public UserWalkingFootprintDto(UserWalkingFootprint footprint) {
		this.id = footprint.getId();
		this.userId = footprint.getUser().getId();
		this.longitude = footprint.getLongitude();
		this.latitude = footprint.getLatitude();
		this.stock = footprint.getStock();
	}


}
