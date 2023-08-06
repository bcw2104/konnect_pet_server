package com.konnect.pet.dto;

import com.konnect.pet.entity.UserProfile;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserProfileDto {
	private Long id;

	private String nickname;

	private String birthDate;

	private String gender;

	private String profileImgUrl;

	private String comment;

	public UserProfileDto(UserProfile profile) {
		this.id = profile.getId();
		this.nickname = profile.getNickname();
		this.birthDate = profile.getBirthDate();
		this.gender=profile.getGender();
		this.profileImgUrl = profile.getProfileImgUrl();
		this.comment = profile.getComment();
	}

}
