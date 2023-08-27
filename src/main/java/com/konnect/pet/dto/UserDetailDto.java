package com.konnect.pet.dto;

import java.util.List;

import com.konnect.pet.entity.User;
import com.konnect.pet.entity.UserFriend;
import com.konnect.pet.entity.UserProfile;
import com.konnect.pet.enums.code.ProcessStatusCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailDto {
	private Long userId;

	private UserProfileDto profile;

	private List<UserPetDto> pets;

	private String residenceCity;

	private String friendStatus;

	public UserDetailDto(User user, UserProfile profile, UserFriend friend) {
		this.userId = user.getId();
		this.profile = new UserProfileDto(profile);
		this.pets = user.getUserPets().stream().map(UserPetDto::new).toList();
		this.residenceCity = user.getResidenceCity();
		if (friend == null) {
			this.friendStatus = ProcessStatusCode.NONE.getCode();
		} else {
			this.friendStatus = friend.getStatus();
		}
	}
}
