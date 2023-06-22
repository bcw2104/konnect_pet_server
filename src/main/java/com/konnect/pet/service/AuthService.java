package com.konnect.pet.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.CustomSQLExceptionTranslatorRegistrar;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.konnect.pet.dto.SocialUserInfoDto;
import com.konnect.pet.dto.TokenInfoDto;
import com.konnect.pet.entity.User;
import com.konnect.pet.enums.PlatformType;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.response.ResponseDto;
import com.konnect.pet.security.JwtTokenProvider;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.konnect.pet.ex.CustomResponseException;
import com.konnect.pet.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final JwtTokenProvider tokenProvider;
	private final PasswordEncoder passwordEncoder;
	private final ExternalApiService apiService;

	public ResponseDto refreshToken(HttpServletRequest request) {
		String accessToken = tokenProvider.resolveToken(request,"Expired");
		String refreshToken = tokenProvider.resolveToken(request,"Refresh");

		if (accessToken == null || refreshToken == null) {
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
		}

		TokenInfoDto newToken = tokenProvider.generateTokenByRefreshToken(accessToken, refreshToken);

		return new ResponseDto(ResponseType.SUCCESS, newToken);
	}

	@Transactional
	public ResponseDto login(String email, String password, PlatformType type) {

		if (PlatformType.EMAIL.equals(type)) {
			Optional<User> userOpt = userRepository.findByEmail(email);

			if (userOpt.isEmpty()) {
				return new ResponseDto(ResponseType.LOGIN_FAIL);
			}

			User user = userOpt.get();

			if (passwordEncoder.matches(password, user.getPassword())) {
				TokenInfoDto token = tokenProvider.generateToken(user.getId());
				user.setLastLoginDate(LocalDateTime.now());

				return new ResponseDto(ResponseType.SUCCESS, token);
			}

		} else if (PlatformType.GOOGLE.equals(type)) {

		} else if (PlatformType.FACEBOOK.equals(type)) {

		} else if (PlatformType.APPLE.equals(type)) {

		} else {
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
		}

		return new ResponseDto(ResponseType.LOGIN_FAIL);
	}

	public ResponseDto getSocialUserInfo(String token, PlatformType type) {
		Map userInfo;
		try {
			if (PlatformType.GOOGLE.equals(type)) {
				userInfo = apiService.callGoogleUserInfoApi(token);

				String gid = userInfo.get("id").toString();
				String email = userInfo.get("email").toString();

				SocialUserInfoDto socialUserInfoDto = new SocialUserInfoDto(email, gid, PlatformType.GOOGLE);
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
