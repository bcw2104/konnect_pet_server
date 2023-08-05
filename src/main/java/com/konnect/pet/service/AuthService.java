package com.konnect.pet.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.extras.springsecurity6.auth.AuthUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.konnect.pet.constant.CommonCodeConst;
import com.konnect.pet.dto.AuthRequestDto;
import com.konnect.pet.dto.JwtTokenDto;
import com.konnect.pet.dto.VerifyFormat;
import com.konnect.pet.entity.EventRewardPolicy;
import com.konnect.pet.entity.TermsGroup;
import com.konnect.pet.entity.User;
import com.konnect.pet.entity.UserTermsAgreement;
import com.konnect.pet.entity.redis.RefreshToken;
import com.konnect.pet.enums.PlatformType;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.enums.Roles;
import com.konnect.pet.enums.VerifyType;
import com.konnect.pet.enums.code.PointHistoryTypeCode;
import com.konnect.pet.ex.CustomResponseException;
import com.konnect.pet.repository.EventRewardPolicyRepository;
import com.konnect.pet.repository.TermsGroupRepository;
import com.konnect.pet.repository.UserRepository;
import com.konnect.pet.repository.UserTermsAggrementRepository;
import com.konnect.pet.repository.redis.RefreshTokenRepository;
import com.konnect.pet.response.ResponseDto;
import com.konnect.pet.security.JwtTokenProvider;
import com.konnect.pet.utils.Aes256Utils;
import com.konnect.pet.utils.Sha256Utils;
import com.konnect.pet.utils.ValidationUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final UserTermsAggrementRepository userTermsAggrementRepository;
	private final TermsGroupRepository termsGroupRepository;
	private final JwtTokenProvider tokenProvider;
	private final PasswordEncoder passwordEncoder;
	private final ExternalApiService apiService;
	private final VerifyService verifyService;
	private final EventService eventService;

	private final RefreshTokenRepository refreshTokenRepository;

	private final ObjectMapper objectMapper;

	@Value("${application.aes.privacy.key}")
	private String PRIVACY_AES_KEY;
	@Value("${application.aes.privacy.iv}")
	private String PRIVACY_AES_IV;

	public ResponseDto refreshToken(HttpServletRequest request) {
		String refreshToken = tokenProvider.resolveToken(request);

		if(refreshToken == null) {
			new CustomResponseException(HttpStatus.FORBIDDEN, ResponseType.INVALID_REFRESH_TOKEN);
		}

		Long userId = Long.parseLong(tokenProvider.parseClaims(refreshToken).get("id").toString());

		RefreshToken redisSavedToken = refreshTokenRepository.findById(userId).orElseThrow(
				() -> new CustomResponseException(HttpStatus.FORBIDDEN, ResponseType.INVALID_REFRESH_TOKEN));

		if (!redisSavedToken.getRefreshToken().equals(refreshToken)) {
			// 위변조 탐지
			refreshTokenRepository.deleteById(userId);
			new CustomResponseException(HttpStatus.FORBIDDEN, ResponseType.INVALID_REFRESH_TOKEN);
		}

		return new ResponseDto(ResponseType.SUCCESS, tokenProvider.generateToken(userId));
	}

	@Transactional
	public ResponseDto socialLogin(String token, PlatformType type) {
		String email = null;

		try {
			if (PlatformType.GOOGLE.equals(type)) {
				Map userInfo = apiService.callGoogleUserInfoApi(token);
				email = userInfo.get("email").toString();
			} else if (PlatformType.FACEBOOK.equals(type)) {
				Map userInfo = apiService.callFacebookUserInfoApi(token);
				email = userInfo.get("email").toString();
			} else {

			}
		} catch (Exception e) {
			log.info(e.getMessage(),e);
			throw new CustomResponseException(ResponseType.SERVER_ERROR);
		}

		if (email == null) {
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
		}

		return login(email, null, type);
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

			return new ResponseDto(ResponseType.LOGIN_FAIL);
		} else {
			Optional<User> userOpt = userRepository.findByEmail(email);

			if (userOpt.isEmpty()) {
				VerifyFormat format = VerifyFormat.builder().target(email).type(VerifyType.SOCIAL).build();

				try {
					String data = objectMapper.writeValueAsString(format);

					Map<String, Object> result = new HashMap<>();
					result.put("key", Aes256Utils.encrypt(data, PRIVACY_AES_KEY, PRIVACY_AES_IV));

					return new ResponseDto(ResponseType.NOT_EXIST_EMAIL, result);
				} catch (Exception e) {
					log.error("validate verfiy error - {}", e.getMessage(), e);
					throw new CustomResponseException(ResponseType.SERVER_ERROR);
				}

			}

			User user = userOpt.get();

			if (!type.equals(user.getPlatform())) {
				if (user.getPlatform().equals(PlatformType.EMAIL)) {
					throw new CustomResponseException(ResponseType.JOIN_WITH_EMAIL);
				} else if (user.getPlatform().equals(PlatformType.GOOGLE)) {
					throw new CustomResponseException(ResponseType.JOIN_WITH_GOOGLE);
				} else if (user.getPlatform().equals(PlatformType.APPLE)) {
					throw new CustomResponseException(ResponseType.JOIN_WITH_APPLE);
				} else if (user.getPlatform().equals(PlatformType.FACEBOOK)) {
					throw new CustomResponseException(ResponseType.JOIN_WITH_FACEBOOK);
				}
				throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
			}

			JwtTokenDto token = tokenProvider.generateToken(user.getId());
			user.setLastLoginDate(LocalDateTime.now());

			return new ResponseDto(ResponseType.SUCCESS, token);
		}

	}

	@Transactional
	public ResponseDto join(AuthRequestDto requestDto) {
		validateJoinData(requestDto);

		VerifyFormat smsVerifyFormat = verifyService.validateVerfiyKey(requestDto.getSmsVerifyKey());
		VerifyFormat emailVerifyFormat = verifyService.validateVerfiyKey(requestDto.getEmailVerifyKey());

		try {
			User user = new User();

			if (PlatformType.EMAIL.equals(requestDto.getPlatform())) {
				user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
			}

			String tel = smsVerifyFormat.getTarget();
			String email = emailVerifyFormat.getTarget();

			StringBuffer maskingTel = new StringBuffer();
			maskingTel.append(tel.substring(0, 3));
			maskingTel.append("****");
			maskingTel.append(tel.substring(7));

			user.setEmail(email);
			user.setTelEnc(Aes256Utils.encrypt(tel, PRIVACY_AES_KEY, PRIVACY_AES_IV));
			user.setTelMask(maskingTel.toString());
			user.setTelHash(Sha256Utils.encrypt(tel));
			user.setRole(Roles.ROLE_LVL1);
			user.setPlatform(requestDto.getPlatform());
			user.setNationCode(requestDto.getNationCode());
			user.setResidenceAddress(requestDto.getAddress());
			user.setResidenceCoords(requestDto.getCoords());
			user.setLastLoginDate(LocalDateTime.now());
			user.setRecommendCode(ValidationUtils.generateRandomString(6, true, true)+ValidationUtils.generateRandomString(2, false, true));
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

			eventService.provideEventReward(user, PointHistoryTypeCode.WELCOME);

			JwtTokenDto token = tokenProvider.generateToken(user.getId());

			return new ResponseDto(ResponseType.JOIN_SUCCESS, token);
		} catch (Exception e) {
			log.error("{}", e.getMessage(), e);
			throw new CustomResponseException(ResponseType.SERVER_ERROR);
		}
	}

	private void validateJoinData(AuthRequestDto requestDto) {
		if (requestDto.getPlatform().equals(PlatformType.EMAIL) && (StringUtils.isEmpty(requestDto.getPassword()))) {
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
		}
	}

	@Transactional
	public ResponseDto resetPassword(AuthRequestDto requestDto) {
		validateResetPasswordData(requestDto);

		VerifyFormat emailVerifyFormat = verifyService.validateVerfiyKey(requestDto.getEmailVerifyKey());

		User user = userRepository.findByEmail(emailVerifyFormat.getTarget())
				.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));

		user.setPassword(passwordEncoder.encode(requestDto.getPassword()));

		return new ResponseDto(ResponseType.RESET_PASSWORD_SUCCESS);
	}

	private void validateResetPasswordData(AuthRequestDto requestDto) {
		if (StringUtils.isEmpty(requestDto.getPassword())) {
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
		}
	}

	@Transactional(readOnly = true)
	public void checkTelBeforeVerify(String tel, boolean join) {
		String encTel = null;
		try {
			encTel = Aes256Utils.encrypt(tel, PRIVACY_AES_KEY, PRIVACY_AES_IV);
		} catch (Exception e) {
			throw new CustomResponseException(ResponseType.SERVER_ERROR);
		}

		if (encTel == null) {
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
		}

		boolean isExist = userRepository.existsByTelEnc(encTel);

		if (isExist && join) {
			throw new CustomResponseException(ResponseType.DUPLICATED_TEL);
		}
		if (!isExist && !join) {
			throw new CustomResponseException(ResponseType.NOT_EXIST_TEL);
		}
	}

	@Transactional(readOnly = true)
	public void checkEmailBeforeVerify(String email, boolean join) {
		Optional<User> userOpt = userRepository.findByEmail(email);
		if (userOpt.isPresent() && join) {
			throw new CustomResponseException(ResponseType.DUPLICATED_EMAIL);
		} else if (userOpt.isPresent() && !join) {
			User user = userOpt.get();
			if (user.getPlatform().equals(PlatformType.GOOGLE)) {
				throw new CustomResponseException(ResponseType.JOIN_WITH_GOOGLE);
			} else if (user.getPlatform().equals(PlatformType.FACEBOOK)) {
				throw new CustomResponseException(ResponseType.JOIN_WITH_FACEBOOK);
			} else if (user.getPlatform().equals(PlatformType.APPLE)) {
				throw new CustomResponseException(ResponseType.JOIN_WITH_APPLE);
			}
		} else if (!userOpt.isPresent() && !join) {
			throw new CustomResponseException(ResponseType.NOT_EXIST_EMAIL);
		}
	}

}
