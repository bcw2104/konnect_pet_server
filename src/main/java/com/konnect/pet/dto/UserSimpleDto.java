package com.konnect.pet.dto;

import com.konnect.pet.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserSimpleDto {
	private Long userId;
	private String email;
	private String tel;
	private String platform;
}
