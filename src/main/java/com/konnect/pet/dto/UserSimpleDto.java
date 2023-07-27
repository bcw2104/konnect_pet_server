package com.konnect.pet.dto;

import java.util.List;

import com.konnect.pet.entity.User;

import jakarta.persistence.Column;
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

	private String profileImgUrl;

	private List<UserPetDto> pets;

	private String residenceAddress;
	private String residenceCoords;
}