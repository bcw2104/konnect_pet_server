package com.konnect.pet.dto;

import java.util.List;

import com.konnect.pet.entity.UserFriend;
import com.konnect.pet.entity.UserPet;
import com.konnect.pet.entity.UserProfile;
import com.konnect.pet.entity.UserWalkingFootprint;
import com.konnect.pet.enums.code.ProcessStatusCode;

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

	private String friendStatus;

	public UserWalkingFootprintDetailDto(UserWalkingFootprint footprint, UserProfile profile, UserFriend friend) {
		this.id = footprint.getId();
		this.userId = footprint.getUser().getId();
		this.profile = new UserProfileDto(profile);
		this.pets = footprint.getUser().getUserPets().stream().map(UserPetDto::new).toList();
		this.residenceCity = footprint.getUser().getResidenceCity();
		if (friend == null) {
			this.friendStatus = ProcessStatusCode.NONE.getCode();
		} else {
			this.friendStatus = friend.getStatus();
		}
	}
}
