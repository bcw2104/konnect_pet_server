package com.konnect.pet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class JwtTokenDto {

	private String grantType;
	private String accessToken;
	private long accessTokenExpireAt;
	private String refreshToken;
	private long refreshTokenExpireAt;
}
