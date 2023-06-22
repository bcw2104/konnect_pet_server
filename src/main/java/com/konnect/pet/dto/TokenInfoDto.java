package com.konnect.pet.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class TokenInfoDto {

	private String grantType;
	private String accessToken;
	private long accessTokenExpireAt;
	private String refreshToken;
	private long refreshTokenExpireAt;
}
