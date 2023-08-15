package com.konnect.pet.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFriendDto {

	private Long friendId;

	private Long userId;

	private String nickname;
	private String birthDate;
	private String gender;

	private String profileImgUrl;

	private String status;

	private LocalDateTime createdDate;

	public UserFriendDto(Long friendId, Long userId, String nickname, String birthDate, String gender,
			String profileImgUrl, String status, LocalDateTime createdDate) {
		this.friendId = friendId;
		this.userId = userId;
		this.nickname = nickname;
		this.birthDate = birthDate;
		this.gender = gender;
		this.profileImgUrl = profileImgUrl;
		this.status = status;
		this.createdDate = createdDate;
	}

}
