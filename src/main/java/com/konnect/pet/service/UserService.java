package com.konnect.pet.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.konnect.pet.dto.VerifyFormat;
import com.konnect.pet.entity.User;
import com.konnect.pet.entity.UserRemoved;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.enums.VerifyType;
import com.konnect.pet.enums.code.LocationCode;
import com.konnect.pet.ex.CustomResponseException;
import com.konnect.pet.repository.UserRemovedRepository;
import com.konnect.pet.repository.UserRepository;
import com.konnect.pet.response.ResponseDto;
import com.konnect.pet.utils.Aes256Utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final UserRemovedRepository userRemovedRepository;
	private final VerifyService verifyService;

	@Value("${application.aes.privacy.key}")
	private String PRIVACY_AES_KEY;
	@Value("${application.aes.privacy.iv}")
	private String PRIVACY_AES_IV;

	@Transactional
	public ResponseDto sendVerifyCodeBySms(Long userId, LocationCode locationCode) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));
		try {
			String tel = Aes256Utils.decrypt(user.getTelEnc(), PRIVACY_AES_KEY, PRIVACY_AES_IV);

			return verifyService.sendVerifyCodeBySms(tel, locationCode);
		} catch (Exception e) {
			log.error("{}", e.getMessage(), e);
			throw new CustomResponseException(ResponseType.SERVER_ERROR);
		}
	}

	@Transactional
	public ResponseDto validateVerfiyCode(Long logId, LocalDateTime timestamp, String verifyCode, String target,
			VerifyType type) {

		return verifyService.validateVerfiyCode(logId, timestamp, verifyCode, target, type);
	}

	@Transactional
	public ResponseDto updateDeviceInfo(Long userId, Map<String, Object> data) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));

		String deviceModel = data.get("deviceModel") != null ? data.get("deviceModel").toString() : null;
		String deviceOs = data.get("deviceOs") != null ? data.get("deviceOs").toString() : null;
		String deviceOsVersion = data.get("deviceOsVersion") != null ? data.get("deviceOsVersion").toString() : null;
		String deviceToken = data.get("deviceToken") != null ? data.get("deviceToken").toString() : null;

		if (!StringUtils.isEmpty(deviceModel))
			user.setDeviceModel(deviceModel);
		if (!StringUtils.isEmpty(deviceOs))
			user.setDeviceOs(deviceOs);
		if (!StringUtils.isEmpty(deviceOsVersion))
			user.setDeviceOsVersion(deviceOsVersion);
		if (!StringUtils.isEmpty(deviceToken))
			user.setDeviceToken(deviceToken);

		return new ResponseDto(ResponseType.SUCCESS);
	}

	@Transactional
	public ResponseDto removeUser(Long userId, String smsVerifyKey) {
		VerifyFormat smsVerifyFormat = verifyService.validateVerfiyKey(smsVerifyKey);

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));

		String tel = null;

		try {
			tel = Aes256Utils.encrypt(smsVerifyFormat.getTarget(), PRIVACY_AES_KEY, PRIVACY_AES_IV);
		} catch (Exception e) {
			log.error("{}", e.getMessage(), e);
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
		}

		if (!user.getTelEnc().equals(tel)) {
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
		}

		UserRemoved userRemoved = new UserRemoved(user);

		String dummy = "r_" + user.getId();

		user.setAuthTokenId(null);
		
		user.setEmail(dummy);
		user.setNationCode(dummy);

		user.setPassword(null);
		user.setEmail(dummy);

		user.setTelEnc(dummy);
		user.setTelHash(dummy);
		user.setTelMask(dummy);
		user.setDeviceModel(null);
		user.setDeviceOs(null);
		user.setDeviceOsVersion(null);
		user.setDeviceToken(null);

		userRemovedRepository.save(userRemoved);

		return new ResponseDto(ResponseType.SUCCESS);
	}

}
