package com.konnect.pet.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.CustomSQLExceptionTranslatorRegistrar;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.konnect.pet.dto.SocialUserInfoDto;
import com.konnect.pet.dto.JwtTokenDto;
import com.konnect.pet.dto.MailDto;
import com.konnect.pet.entity.MailVerifyLog;
import com.konnect.pet.entity.SmsVerifyLog;
import com.konnect.pet.entity.User;
import com.konnect.pet.enums.PlatformType;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.enums.VerifyType;
import com.konnect.pet.enums.code.VerifyLocationCode;
import com.konnect.pet.response.ResponseDto;
import com.konnect.pet.security.JwtTokenProvider;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import com.konnect.pet.ex.CustomResponseException;
import com.konnect.pet.repository.MailVerifyLogRepository;
import com.konnect.pet.repository.SmsVerifyLogRepository;
import com.konnect.pet.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final MailVerifyLogRepository mailVerifyLogRepository;
	private final SmsVerifyLogRepository smsVerifyLogRepository;
	private final JwtTokenProvider tokenProvider;
	private final PasswordEncoder passwordEncoder;
	private final ExternalApiService apiService;
	private final MailService mailService;

	public ResponseDto refreshToken(HttpServletRequest request) {
		String accessToken = tokenProvider.resolveToken(request, "Expired");
		String refreshToken = tokenProvider.resolveToken(request, "Refresh");

		if (accessToken == null || refreshToken == null) {
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
		}

		JwtTokenDto newToken = tokenProvider.generateTokenByRefreshToken(accessToken, refreshToken);

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
				JwtTokenDto token = tokenProvider.generateToken(user.getId());
				user.setLastLoginDate(LocalDateTime.now());

				return new ResponseDto(ResponseType.SUCCESS, token);
			}

		} else if (PlatformType.GOOGLE.equals(type)) {

		} else if (PlatformType.FACEBOOK.equals(type)) {

		} else if (PlatformType.APPLE.equals(type)) {

		} else {
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
		}

		return new ResponseDto(ResponseType.SUCCESS);
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

	@Transactional
	public ResponseDto sendVerifyCodeBySms(String tel, VerifyLocationCode locationCode) {
		Map<String, Object> mailData = new HashMap<>();

		String verifiyCode = generateRandomString(8, true, true);

		SmsVerifyLog verifyLog = SmsVerifyLog.builder().verifiyCode(verifiyCode).consumedYn(false)
				.locationCode(locationCode.getCode()).build();

		smsVerifyLogRepository.save(verifyLog);

		Map<String, Object> result = new HashMap<>();
		result.put("req_id", verifyLog.getId());
		result.put("timestamp", Timestamp.valueOf(verifyLog.getCreatedDate()));

		return new ResponseDto(ResponseType.SUCCESS, result);
	}

	@Transactional
	public ResponseDto sendVerifyCodeByEmail(String email, VerifyLocationCode locationCode) {
		Map<String, Object> mailData = new HashMap<>();

		String verifiyCode = generateRandomString(8, true, true);

		mailData.put("verifyCode", verifiyCode);

		MailDto mail = MailDto.builder().subject("Konnect - Email authentication code for Sign up").receiver(email)
				.template("email_verify_template").data(mailData).build();

		mailService.sendTemplateMail(mail);

		MailVerifyLog verifyLog = MailVerifyLog.builder().verifiyCode(verifiyCode).consumedYn(false)
				.locationCode(locationCode.getCode()).build();

		mailVerifyLogRepository.save(verifyLog);

		Map<String, Object> result = new HashMap<>();
		result.put("req_id", verifyLog.getId());
		result.put("timestamp", Timestamp.valueOf(verifyLog.getCreatedDate()));

		return new ResponseDto(ResponseType.SUCCESS, result);
	}

	private String generateRandomString(int length, boolean useNumber, boolean useLetters) {
		return RandomStringUtils.random(length, useLetters, useNumber);
	}

	@Transactional
	public ResponseDto validateVerfiyCode(Long logId, Long timestamp, String verifyCode, VerifyType type) {
		Map<String, Object> result = new HashMap<>();
		
		if (VerifyType.SMS.equals(type)) {
			SmsVerifyLog log = smsVerifyLogRepository.findById(logId)
					.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));
			Long createdTimestamp = Timestamp.valueOf(log.getCreatedDate()).getTime();
			//TODO 여기다 시간 체크
			if (!timestamp.equals(createdTimestamp)) {
				new CustomResponseException(ResponseType.INVALID_PARAMETER);
			}
			if (!log.getVerifiyCode().equals(verifyCode)) {
				throw new CustomResponseException(ResponseType.VERIFY_FAIL);
			}
			log.setConsumedYn(true);
		} else if (VerifyType.EMAIL.equals(type)) {
			MailVerifyLog log = mailVerifyLogRepository.findById(logId)
					.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));
			if (!Timestamp.valueOf(log.getCreatedDate()).equals(timestamp)) {
				new CustomResponseException(ResponseType.INVALID_PARAMETER);
			}
			if (!log.getVerifiyCode().equals(verifyCode)) {
				throw new CustomResponseException(ResponseType.VERIFY_FAIL);
			}
			log.setConsumedYn(true);
		} else {
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
		}


		return new ResponseDto(ResponseType.SUCCESS, result);
	}

}
