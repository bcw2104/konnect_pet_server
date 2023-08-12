package com.konnect.pet.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class UserSimpleDto {
	private Long userId;
	private String email;
	private String tel;
	private String platform;
	private LocalDateTime createdDate;

	private UserProfileDto profile;

	private List<UserPetDto> pets;

	private String residenceCity;
	private String residenceAddress;
	private String residenceCoords;
}