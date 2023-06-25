package com.konnect.pet.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.support.CustomSQLExceptionTranslatorRegistrar;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.konnect.pet.dto.SocialUserInfoDto;
import com.konnect.pet.constant.CommonCodeConst;
import com.konnect.pet.dto.JwtTokenDto;
import com.konnect.pet.dto.MailDto;
import com.konnect.pet.dto.SignupRequestDto;
import com.konnect.pet.entity.CommonCode;
import com.konnect.pet.entity.MailVerifyLog;
import com.konnect.pet.entity.SmsVerifyLog;
import com.konnect.pet.entity.User;
import com.konnect.pet.enums.PlatformType;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.enums.Roles;
import com.konnect.pet.enums.VerifyType;
import com.konnect.pet.enums.code.LocationCode;
import com.konnect.pet.response.ResponseDto;
import com.konnect.pet.security.JwtTokenProvider;
import com.konnect.pet.utils.Aes256Utils;
import com.konnect.pet.utils.Sha256Utils;

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
	private final SmsService smsService;

	@Value("${application.name}")
	private String APP_NAME;

	@Value("${application.aes.privacy.key}")
	private String PRIVACY_AES_KEY;
	@Value("${application.aes.privacy.iv}")
	private String PRIVACY_AES_IV;

	// seconds
	private final int SMS_VERIFY_TIMEOUT = 180;
	private final int EMAIL_VERIFY_TIMEOUT = 600;

	private String generateRandomString(int length, boolean useNumber, boolean useLetters) {
		return RandomStringUtils.random(length, useLetters, useNumber);
	}

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

		return new ResponseDto(ResponseType.LOGIN_FAIL);
	}

	@Transactional
	public ResponseDto join(SignupRequestDto requestDto) {
		validateJoinData(requestDto);
		try {
			User user = new User();

			if (PlatformType.EMAIL.equals(requestDto.getPlatform())) {
				user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
			}

			user.setEmail(requestDto.getEmail());
			user.setTelEnc(Aes256Utils.encrypt(requestDto.getTel(), PRIVACY_AES_KEY, PRIVACY_AES_IV));
			user.setTelMask(requestDto.getMaskingTel());
			user.setTelHash(Sha256Utils.encrypt(requestDto.getTel()));
			user.setRole(Roles.ROLE_LVL1);
			user.setPlatform(requestDto.getPlatform());
			user.setLastLoginDate(LocalDateTime.now());
			user.setDeviceName("GALAXY S21");
			user.setDeviceToken("AAAA");
			user.setMarketingYn(Boolean
					.parseBoolean(((Map) requestDto.getTermsAgreed().get(CommonCodeConst.MARKETING_TERMS_GROUP_ID))
							.get("checkedYn").toString()));



			userRepository.save(user);

			JwtTokenDto token = tokenProvider.generateToken(user.getId());

			return new ResponseDto(ResponseType.SUCCESS, token);
		} catch (Exception e) {
			log.error("{}",e.getMessage(),e);
			throw new CustomResponseException(ResponseType.SERVER_ERROR);
		}
	}

	private void validateJoinData(SignupRequestDto requestDto) {
		if (StringUtils.isEmpty(requestDto.getEmail()) || StringUtils.isEmpty(requestDto.getTel())
				|| (requestDto.getPlatform().equals(PlatformType.EMAIL)
						&& (StringUtils.isEmpty(requestDto.getPassword())) || requestDto.getEmailReqId() == null)
				|| (!requestDto.getPlatform().equals(PlatformType.EMAIL)
						&& StringUtils.isEmpty(requestDto.getPlatformId()))
				|| requestDto.getSmsReqId() == null) {
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
		}

		SmsVerifyLog smsVerifyLog = smsVerifyLogRepository.findById(requestDto.getSmsReqId())
				.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));

		if (!smsVerifyLog.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
				.equals(requestDto.getSmsTimestamp())) {
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
		}
		if (requestDto.getPlatform().equals(PlatformType.EMAIL)) {
			MailVerifyLog emailVerifyLog = mailVerifyLogRepository.findById(requestDto.getEmailReqId())
					.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));

			if (!emailVerifyLog.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
					.equals(requestDto.getEmailTimestamp())) {
				throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
			}
		}

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
	public ResponseDto sendVerifyCodeBySms(String tel, LocationCode locationCode) {
		String verifiyCode = generateRandomString(6, true, false);
		StringBuffer smsContent = new StringBuffer();
		smsContent.append("Your ").append(APP_NAME).append(" verification code is: ").append(verifiyCode);

		boolean isSuccess = smsService.sendSimpleSms(tel, smsContent.toString());

		if (isSuccess) {
			SmsVerifyLog verifyLog = SmsVerifyLog.builder().verifiyCode(verifiyCode).consumedYn(false).successYn(false)
					.locationCode(locationCode.getCode()).build();

			smsVerifyLogRepository.save(verifyLog);

			Map<String, Object> result = new HashMap<>();
			result.put("reqId", verifyLog.getId());
			result.put("timestamp",
					verifyLog.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

			return new ResponseDto(ResponseType.SUCCESS, result);
		} else {
			throw new CustomResponseException(ResponseType.SERVER_ERROR);
		}

	}

	@Transactional
	public ResponseDto sendVerifyCodeByEmail(String email, LocationCode locationCode) {
		Map<String, Object> mailData = new HashMap<>();

		String verifiyCode = generateRandomString(8, true, true);

		mailData.put("verifyCode", verifiyCode);

		MailDto mail = MailDto.builder().subject("Konnect - Email authentication code for sign up").receiver(email)
				.template("email_verify_template").data(mailData).build();

		mailService.sendTemplateMail(mail);

		MailVerifyLog verifyLog = MailVerifyLog.builder().verifiyCode(verifiyCode).consumedYn(false)
				.locationCode(locationCode.getCode()).build();

		mailVerifyLogRepository.save(verifyLog);

		Map<String, Object> result = new HashMap<>();
		result.put("reqId", verifyLog.getId());
		result.put("timestamp", verifyLog.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

		return new ResponseDto(ResponseType.SUCCESS, result);
	}

	@Transactional
	public ResponseDto validateVerfiyCode(Long logId, LocalDateTime timestamp, String verifyCode, VerifyType type) {
		Map<String, Object> result = new HashMap<>();
		LocalDateTime now = LocalDateTime.now();

		if (VerifyType.SMS.equals(type)) {
			SmsVerifyLog verifyLog = smsVerifyLogRepository.findById(logId)
					.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));

			if (!timestamp.isEqual(verifyLog.getCreatedDate())) {
				new CustomResponseException(ResponseType.INVALID_PARAMETER);
			}
			if (verifyLog.getCreatedDate().plusSeconds(SMS_VERIFY_TIMEOUT).isBefore(now)) {
				throw new CustomResponseException(ResponseType.VERIFY_TIMEOUT);
			}
			if (!verifyLog.getVerifiyCode().equals(verifyCode)) {
				throw new CustomResponseException(ResponseType.VERIFY_FAIL);
			}
			verifyLog.setConsumedYn(true);
		} else if (VerifyType.EMAIL.equals(type)) {
			MailVerifyLog verifyLog = mailVerifyLogRepository.findById(logId)
					.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));

			if (!timestamp.isEqual(verifyLog.getCreatedDate())) {
				new CustomResponseException(ResponseType.INVALID_PARAMETER);
			}
			if (verifyLog.getCreatedDate().plusSeconds(EMAIL_VERIFY_TIMEOUT).isBefore(now)) {
				throw new CustomResponseException(ResponseType.VERIFY_TIMEOUT);
			}
			if (!verifyLog.getVerifiyCode().equals(verifyCode)) {
				throw new CustomResponseException(ResponseType.VERIFY_FAIL);
			}
			verifyLog.setConsumedYn(true);
		} else {
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
		}

		return new ResponseDto(ResponseType.SUCCESS, result);
	}

}
