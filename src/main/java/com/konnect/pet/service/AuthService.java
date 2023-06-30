package com.konnect.pet.service;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.support.CustomSQLExceptionTranslatorRegistrar;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.konnect.pet.dto.SocialUserInfoDto;
import com.konnect.pet.dto.VerifyFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.konnect.pet.constant.CommonCodeConst;
import com.konnect.pet.dto.JwtTokenDto;
import com.konnect.pet.dto.MailRequestDto;
import com.konnect.pet.dto.AuthRequestDto;
import com.konnect.pet.entity.CommonCode;
import com.konnect.pet.entity.MailVerifyLog;
import com.konnect.pet.entity.SmsVerifyLog;
import com.konnect.pet.entity.TermsGroup;
import com.konnect.pet.entity.User;
import com.konnect.pet.entity.UserTermsAgreement;
import com.konnect.pet.enums.PlatformType;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.enums.Roles;
import com.konnect.pet.enums.VerifyType;
import com.konnect.pet.enums.code.LocationCode;
import com.konnect.pet.response.ResponseDto;
import com.konnect.pet.security.JwtTokenProvider;
import com.konnect.pet.utils.Aes256Utils;
import com.konnect.pet.utils.Sha256Utils;
import com.twilio.rest.api.v2010.account.Message;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import com.konnect.pet.ex.CustomResponseException;
import com.konnect.pet.repository.MailVerifyLogRepository;
import com.konnect.pet.repository.SmsVerifyLogRepository;
import com.konnect.pet.repository.TermsGroupRepository;
import com.konnect.pet.repository.UserRepository;
import com.konnect.pet.repository.UserTermsAggrementRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final MailVerifyLogRepository mailVerifyLogRepository;
	private final SmsVerifyLogRepository smsVerifyLogRepository;
	private final UserTermsAggrementRepository userTermsAggrementRepository;
	private final TermsGroupRepository termsGroupRepository;
	private final JwtTokenProvider tokenProvider;
	private final PasswordEncoder passwordEncoder;
	private final ExternalApiService apiService;
	private final MailService mailService;
	private final SmsService smsService;

	private final ObjectMapper objectMapper;

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
		String accessToken = tokenProvider.resolveToken(request, "EXPRIED");
		String refreshToken = tokenProvider.resolveToken(request, "REFRESH");

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

		} else {
			Optional<User> userOpt = userRepository.findByEmail(email);

			if (userOpt.isEmpty()) {
				return new ResponseDto(ResponseType.LOGIN_FAIL, new SocialUserInfoDto(email, type));
			}

			User user = userOpt.get();
			JwtTokenDto token = tokenProvider.generateToken(user.getId());
			user.setLastLoginDate(LocalDateTime.now());

			return new ResponseDto(ResponseType.SUCCESS, token);
		}

		return new ResponseDto(ResponseType.LOGIN_FAIL);
	}

	@Transactional
	public ResponseDto join(AuthRequestDto requestDto) {
		validateJoinData(requestDto);

		VerifyFormat smsVerifyFormat = validateVerfiyKey(requestDto.getSmsVerifyKey());
		VerifyFormat emailVerifyFormat = validateVerfiyKey(requestDto.getEmailVerifyKey());

		try {
			User user = new User();

			if (PlatformType.EMAIL.equals(requestDto.getPlatform())) {
				user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
			}

			String tel = smsVerifyFormat.getTarget();
			String email = emailVerifyFormat.getTarget();

			StringBuffer maskingTel = new StringBuffer();
			maskingTel.append(tel.substring(0, 2));
			maskingTel.append("****");
			maskingTel.append(tel.substring(6));

			user.setEmail(email);
			user.setTelEnc(Aes256Utils.encrypt(tel, PRIVACY_AES_KEY, PRIVACY_AES_IV));
			user.setTelMask(maskingTel.toString());
			user.setTelHash(Sha256Utils.encrypt(tel));
			user.setRole(Roles.ROLE_LVL1);
			user.setPlatform(requestDto.getPlatform());
			user.setLastLoginDate(LocalDateTime.now());
			user.setDeviceName("GALAXY S21");
			user.setDeviceToken("AAAA");
			user.setMarketingYn(Boolean
					.parseBoolean(((Map) requestDto.getTermsAgreed().get(CommonCodeConst.MARKETING_TERMS_GROUP_ID))
							.get("checkedYn").toString()));

			userRepository.save(user);

			List<UserTermsAgreement> agreements = new ArrayList<UserTermsAgreement>();

			requestDto.getTermsAgreed().forEach((key, value) -> {
				Map ele = (Map) value;
				boolean agreement = Boolean.parseBoolean(ele.get("checkedYn").toString());

				if (agreement) {
					Optional<TermsGroup> termsGroupOpt = termsGroupRepository.findById(key);

					if (termsGroupOpt.isPresent()) {
						agreements.add(UserTermsAgreement.builder().user(user).termsGroup(termsGroupOpt.get()).build());
					}
				}
			});

			userTermsAggrementRepository.saveAll(agreements);

			JwtTokenDto token = tokenProvider.generateToken(user.getId());

			return new ResponseDto(ResponseType.SUCCESS, token);
		} catch (Exception e) {
			log.error("{}", e.getMessage(), e);
			throw new CustomResponseException(ResponseType.SERVER_ERROR);
		}
	}

	private void validateJoinData(AuthRequestDto requestDto) {
		if (StringUtils.isEmpty(requestDto.getSmsVerifyKey()) || (requestDto.getPlatform().equals(PlatformType.EMAIL)
						&& (StringUtils.isEmpty(requestDto.getPassword()) || StringUtils.isEmpty(requestDto.getEmailVerifyKey())))) {
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
		}
	}

	@Transactional
	public ResponseDto resetPassword(AuthRequestDto requestDto) {
		validateResetPasswordData(requestDto);

		VerifyFormat emailVerifyFormat = validateVerfiyKey(requestDto.getEmailVerifyKey());

		User user = userRepository.findByEmail(emailVerifyFormat.getTarget()).orElseThrow(()->new CustomResponseException(ResponseType.INVALID_PARAMETER));

		user.setPassword(passwordEncoder.encode(requestDto.getPassword()));

		return new ResponseDto(ResponseType.SUCCESS);
	}

	private void validateResetPasswordData(AuthRequestDto requestDto) {
		if (StringUtils.isEmpty(requestDto.getPassword()) || StringUtils.isEmpty(requestDto.getEmailVerifyKey())) {
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
		}
	}


	public ResponseDto socialLogin(String token, PlatformType type) {
		try {
			if (PlatformType.GOOGLE.equals(type)) {
				Map userInfo = apiService.callGoogleUserInfoApi(token);
				String email = userInfo.get("email").toString();

				return login(email, null, PlatformType.GOOGLE);
			} else if (PlatformType.FACEBOOK.equals(type)) {
				Map userInfo = apiService.callFacebookUserInfoApi(token);
				String email = userInfo.get("email").toString();

				return login(email, null, PlatformType.FACEBOOK);
			} else {
				throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
			}
		} catch (Exception e) {
			throw new CustomResponseException(ResponseType.SERVER_ERROR);
		}
	}

	@Transactional
	public ResponseDto sendVerifyCodeBySms(String tel, LocationCode locationCode) {
		String verifiyCode = generateRandomString(6, true, false);
		StringBuffer smsContent = new StringBuffer();
		smsContent.append("Your ").append(APP_NAME).append(" verification code is: ").append(verifiyCode);

		Message message = smsService.sendSimpleSms(tel, smsContent.toString());

		if (message != null) {

			SmsVerifyLog verifyLog = SmsVerifyLog.builder().verifiyCode(verifiyCode).consumedYn(false).successYn(false)
					.locationCode(locationCode.getCode()).build();

			smsVerifyLogRepository.save(verifyLog);

			Map<String, Object> result = new HashMap<>();
			result.put("reqId", verifyLog.getId());
			result.put("tel", message.getTo());
			result.put("timestamp",
					verifyLog.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

			return new ResponseDto(ResponseType.SUCCESS, result);
		} else {
			throw new CustomResponseException(ResponseType.SERVER_ERROR);
		}

	}

	@Transactional(readOnly = true)
	public void checkTelDuplication(String tel, boolean join) {
		try {
			boolean isExist = userRepository.existsByTelEnc(Aes256Utils.encrypt(tel, PRIVACY_AES_KEY, PRIVACY_AES_IV));

			if (isExist && join) {
				throw new CustomResponseException(ResponseType.DUPLICATED_EMAIL);
			}
			if (!isExist && !join) {
				throw new CustomResponseException(ResponseType.NOT_EXIST_EMAIL);
			}
		} catch (Exception e) {
			throw new CustomResponseException(ResponseType.SERVER_ERROR);
		}
	}

	@Transactional
	public ResponseDto sendVerifyCodeByEmail(String email, LocationCode locationCode) {
		Map<String, Object> mailData = new HashMap<>();

		String verifiyCode = generateRandomString(8, true, true);

		mailData.put("verifyCode", verifiyCode);

		MailRequestDto mail = MailRequestDto.builder().subject("Konnect - Email authentication code for sign up")
				.receiver(email).template("email_verify_template").data(mailData).build();

		mailService.sendTemplateMail(mail);

		MailVerifyLog verifyLog = MailVerifyLog.builder().verifiyCode(verifiyCode).consumedYn(false)
				.locationCode(locationCode.getCode()).build();

		mailVerifyLogRepository.save(verifyLog);

		Map<String, Object> result = new HashMap<>();
		result.put("reqId", verifyLog.getId());
		result.put("timestamp", verifyLog.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

		return new ResponseDto(ResponseType.SUCCESS, result);
	}

	@Transactional(readOnly = true)
	public void checkEmailDuplication(String email, boolean join) {
		boolean isExist = userRepository.existsByEmail(email);
		if (isExist && join) {
			throw new CustomResponseException(ResponseType.DUPLICATED_EMAIL);
		}
		if (!isExist && !join) {
			throw new CustomResponseException(ResponseType.NOT_EXIST_EMAIL);
		}
	}

	@Transactional
	public ResponseDto validateVerfiyCode(Long logId, LocalDateTime timestamp, String verifyCode, String target,
			VerifyType type) {
		LocalDateTime now = LocalDateTime.now();

		if (VerifyType.SMS.equals(type)) {
			SmsVerifyLog verifyLog = smsVerifyLogRepository.findById(logId)
					.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));

			if (verifyLog.isConsumedYn()) {
				new CustomResponseException(ResponseType.VERIFY_FAIL);
			}
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

			if (verifyLog.isConsumedYn()) {
				new CustomResponseException(ResponseType.VERIFY_FAIL);
			}
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

		VerifyFormat format = VerifyFormat.builder().verifyId(logId).verifyTime(timestamp).type(type).target(target)
				.build();

		try {
			String data = objectMapper.writeValueAsString(format);

			Map<String, Object> result = new HashMap<>();
			result.put("key", Aes256Utils.encrypt(PRIVACY_AES_KEY, PRIVACY_AES_IV, data));

			return new ResponseDto(ResponseType.SUCCESS, result);
		} catch (Exception e) {
			log.error("validate verfiy error - {}", e.getMessage(), e);
			throw new CustomResponseException(ResponseType.SERVER_ERROR);
		}
	}

	@Transactional
	public VerifyFormat validateVerfiyKey(String key) {
		try {
			String decKey = Aes256Utils.decrypt(PRIVACY_AES_KEY, PRIVACY_AES_IV, key);

			VerifyFormat format = objectMapper.readValue(decKey, VerifyFormat.class);

			if (VerifyType.SMS.equals(format.getType())) {
				SmsVerifyLog verifyLog = smsVerifyLogRepository.findById(format.getVerifyId())
						.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));

				if (!verifyLog.isConsumedYn() || !format.getVerifyTime().isEqual(verifyLog.getCreatedDate())) {
					new CustomResponseException(ResponseType.INVALID_PARAMETER);
				}
			} else if (VerifyType.EMAIL.equals(format.getType())) {
				MailVerifyLog verifyLog = mailVerifyLogRepository.findById(format.getVerifyId())
						.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));

				if (!verifyLog.isConsumedYn() || !format.getVerifyTime().isEqual(verifyLog.getCreatedDate())) {
					new CustomResponseException(ResponseType.INVALID_PARAMETER);
				}
			} else {
				throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
			}

			return format;
		} catch (Exception e) {
			log.error("validate key error - {}", e.getMessage(), e);
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
		}
	}

}
