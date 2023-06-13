package com.konnect.pet.service;

import java.util.Map;

import org.springframework.jdbc.support.CustomSQLExceptionTranslatorRegistrar;
import org.springframework.stereotype.Service;

import com.konnect.pet.dto.SocialUserInfoDto;
import com.konnect.pet.enums.PlatformType;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.response.ResponseDto;

import lombok.RequiredArgsConstructor;
import com.konnect.pet.ex.CustomResponseException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final ExternalApiService apiService;

	public ResponseDto getSocialUserInfo(String token, PlatformType type) {
		Map userInfo;
		try {
			if (PlatformType.GOOGLE.equals(type)) {
				userInfo = apiService.callGoogleUserInfoApi(token);

				String gid = userInfo.get("id").toString();
				String email = userInfo.get("email").toString();

				SocialUserInfoDto socialUserInfoDto = new SocialUserInfoDto(gid,email);
				return new ResponseDto(ResponseType.SUCCESS, socialUserInfoDto);
			} else if (PlatformType.FACEBOOK.equals(type)) {

				return new ResponseDto(ResponseType.SUCCESS, null);
			} else {
				throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
			}
		} catch (Exception e) {
			throw new CustomResponseException();
		}
	}
}
