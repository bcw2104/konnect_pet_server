package com.konnect.pet.dto;

import com.konnect.pet.entity.UserProfile;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserProfileDto {
	private Long profileId;

	private String nickname;

	private String birthDate;

	private String gender;

	private String imgPath;

	private String comment;

	public UserProfileDto(UserProfile profile) {
		this.profileId = profile.getId();
		this.nickname = profile.getNickname();
		this.birthDate = profile.getBirthDate();
		this.gender=profile.getGender();
		this.imgPath = profile.getImgPath();
		this.comment = profile.getComment();
	}

}
