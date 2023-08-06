package com.konnect.pet.dto;

import java.util.List;

import com.konnect.pet.entity.UserPet;
import com.konnect.pet.entity.UserProfile;
import com.konnect.pet.entity.UserWalkingFootprint;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserWalkingFootprintDetailDto {
	private Long id;

	private Long userId;

	private UserProfileDto profile;

	private List<UserPetDto> pets;

	private String residenceCity;

	public UserWalkingFootprintDetailDto(UserWalkingFootprint footprint, UserProfile profile) {
		this.id = footprint.getId();
		this.userId = footprint.getUser().getId();
		this.profile = new UserProfileDto(profile);
		this.pets = footprint.getUser().getUserPets().stream().map(UserPetDto::new).toList();
		this.residenceCity = footprint.getUser().getResidenceCity();
	}
}
