package com.konnect.pet.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.konnect.pet.constant.ServiceConst;
import com.konnect.pet.dto.BannerDto;
import com.konnect.pet.dto.JwtTokenDto;
import com.konnect.pet.dto.UserAppSettingDto;
import com.konnect.pet.dto.UserFriendDto;
import com.konnect.pet.dto.UserPetDto;
import com.konnect.pet.dto.UserPointDto;
import com.konnect.pet.dto.UserProfileDto;
import com.konnect.pet.dto.UserSimpleDto;
import com.konnect.pet.dto.VerifyFormat;
import com.konnect.pet.entity.TermsGroup;
import com.konnect.pet.entity.User;
import com.konnect.pet.entity.UserAppSetting;
import com.konnect.pet.entity.UserFriend;
import com.konnect.pet.entity.UserPoint;
import com.konnect.pet.entity.UserProfile;
import com.konnect.pet.entity.UserRemoved;
import com.konnect.pet.entity.UserTermsAgreement;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.enums.VerifyType;
import com.konnect.pet.enums.code.LocationCode;
import com.konnect.pet.enums.code.PointTypeCode;
import com.konnect.pet.enums.code.ProcessStatusCode;
import com.konnect.pet.enums.code.UserStatusCode;
import com.konnect.pet.ex.CustomResponseException;
import com.konnect.pet.repository.BannerRepository;
import com.konnect.pet.repository.TermsGroupRepository;
import com.konnect.pet.repository.UserAppSettingRepository;
import com.konnect.pet.repository.UserFriendRepository;
import com.konnect.pet.repository.UserNotificationLogRepository;
import com.konnect.pet.repository.UserPetRepository;
import com.konnect.pet.repository.UserPointRepository;
import com.konnect.pet.repository.UserProfileRepository;
import com.konnect.pet.repository.UserRemovedRepository;
import com.konnect.pet.repository.UserRepository;
import com.konnect.pet.repository.UserTermsAgreementRepository;
import com.konnect.pet.repository.UserWalkingFootprintRepository;
import com.konnect.pet.repository.redis.RefreshTokenRepository;
import com.konnect.pet.response.ResponseDto;
import com.konnect.pet.utils.Aes256Utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserAppSettingRepository userAppSettingRepository;
	private final UserRepository userRepository;
	private final UserPetRepository userPetRepository;
	private final UserProfileRepository userProfileRepository;
	private final UserRemovedRepository userRemovedRepository;
	private final UserTermsAgreementRepository userTermsAgreementRepository;
	private final UserWalkingFootprintRepository userWalkingFootprintRepository;
	private final UserPointRepository userPointRepository;
	private final UserFriendRepository userFriendRepository;
	private final UserNotificationLogRepository userNotificationLogRepository;
	private final VerifyService verifyService;
	private final RefreshTokenRepository refreshTokenRepository;
	private final BannerRepository bannerRepository;
	private final TermsGroupRepository termsGroupRepository;
	private final PasswordEncoder passwordEncoder;

	@Value("${application.aes.privacy.key}")
	private String PRIVACY_AES_KEY;
	@Value("${application.aes.privacy.iv}")
	private String PRIVACY_AES_IV;

	public ResponseDto logout(Long userId) {
		refreshTokenRepository.deleteById(userId);

		return new ResponseDto(ResponseType.SUCCESS);
	}

	@Transactional
	public ResponseDto getUserSimplenfo(User user) {

		UserAppSetting setting = userAppSettingRepository.findByUserId(user.getId()).orElse(null);
		if (setting == null) {
			setting = new UserAppSetting();
			setting.setUser(user);
			setting.initSetting();
			userAppSettingRepository.save(setting);
		}

		UserProfile profile = userProfileRepository.findByUserId(user.getId()).orElse(null);
		UserProfileDto profileDto = null;
		if (profile != null) {
			profileDto = new UserProfileDto(profile);
		}
		List<UserPetDto> pets = userPetRepository.findByUserId(user.getId()).stream().map(UserPetDto::new).toList();
		UserSimpleDto simpleDto = UserSimpleDto.builder().userId(user.getId()).email(user.getEmail())
				.platform(user.getPlatform().name()).tel(user.getTelMask()).createdDate(user.getCreatedDate())
				.profile(profileDto).pets(pets).residenceCity(user.getResidenceCity())
				.residenceAddress(user.getResidenceAddress()).residenceCoords(user.getResidenceCoords())
				.marketingYn(user.isMarketingYn()).appSettings(new UserAppSettingDto(setting)).build();

		return new ResponseDto(ResponseType.SUCCESS, simpleDto);
	}

	@Transactional
	public ResponseDto changePassword(User user, String password, String newPassword) {
		if (!passwordEncoder.matches(password, user.getPassword())) {
			return new ResponseDto(ResponseType.INVALID_PASSWORD);
		}

		user = userRepository.findById(user.getId())
				.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));

		user.setPassword(passwordEncoder.encode(newPassword));

		return new ResponseDto(ResponseType.SUCCESS);
	}

	@Transactional
	public ResponseDto sendVerifyCodeBySms(User user, LocationCode locationCode) {
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
	public ResponseDto changeMarketingYn(Long userId, boolean marketingYn) {
		log.info("change user marketing agreement - userId: {}, marketingYn: {}", userId, marketingYn);

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));

		TermsGroup termsGroup = termsGroupRepository.findById(ServiceConst.MARKETING_TERMS_GROUP_ID)
				.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));

		UserTermsAgreement marketingAggreement = userTermsAgreementRepository
				.findByUserIdAndTermsGroupId(userId, termsGroup.getId()).orElse(null);

		user.setMarketingYn(marketingYn);

		if (marketingAggreement == null) {
			UserTermsAgreement newAgreement = new UserTermsAgreement();
			newAgreement.setUser(user);
			newAgreement.setTermsGroup(termsGroup);
			newAgreement.setAgreedYn(marketingYn);
			userTermsAgreementRepository.save(newAgreement);
		} else {
			marketingAggreement.setAgreedYn(marketingYn);
		}

		return new ResponseDto(ResponseType.SUCCESS);
	}

	@Transactional
	public ResponseDto removeUser(Long userId, String smsVerifyKey) {
		VerifyFormat smsVerifyFormat = verifyService.validateVerfiyKey(smsVerifyKey);

		log.info("remove user - userId: {}", userId);

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

		int deleted = userFriendRepository.deleteAllByUserId(user.getId());

		log.info("remove user friends - count: {}", deleted);

		UserRemoved userRemoved = new UserRemoved(user);

		String dummy = "r_" + user.getId();

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
		user.setStatus(UserStatusCode.REMOVED.getCode());

		userRemovedRepository.save(userRemoved);

		return new ResponseDto(ResponseType.SUCCESS);
	}

	@Transactional
	public ResponseDto saveProfile(User user, Map<String, Object> body) {
		try {
			String nickname = body.get("nickname").toString();
			String gender = body.get("gender").toString();
			String birthDate = body.get("birthDate").toString();
			String imgPath = body.get("imgPath") == null ? null : body.get("imgPath").toString();
			String comment = body.get("comment") == null ? "" : body.get("comment").toString();

			UserProfile profile = userProfileRepository.findByUserId(user.getId()).orElse(null);

			if (profile != null) {
				profile.setBirthDate(birthDate);
				profile.setNickname(nickname);
				profile.setImgPath(imgPath);
				profile.setComment(comment);
				profile.setGender(gender);
			} else {
				profile = new UserProfile();
				profile.setUser(user);
				profile.setBirthDate(birthDate);
				profile.setNickname(nickname);
				profile.setImgPath(imgPath);
				profile.setComment(comment);
				profile.setGender(gender);

				userProfileRepository.save(profile);
			}
			return new ResponseDto(ResponseType.SUCCESS, new UserProfileDto(profile));

		} catch (Exception e) {
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
		}

	}

	@Transactional(readOnly = true)
	public ResponseDto getMypageData(User user) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		Map<String, UserPointDto> pointMap = generateFullUserPointMap(user);
		int newNotiCount = userNotificationLogRepository.countByUserIdAndVisitedYnIsFalse(user.getId());

		List<BannerDto> banners = bannerRepository.findActive().stream().map(BannerDto::new).toList();

		int friendCount = userFriendRepository.countByFromUserIdAndStatus(user.getId(),
				ProcessStatusCode.PERMITTED.getCode());

		resultMap.put("point", pointMap.get(PointTypeCode.POINT.getCode()));
		resultMap.put("banners", banners);
		resultMap.put("newNotiCount", newNotiCount);
		resultMap.put("friendCount", friendCount);
		return new ResponseDto(ResponseType.SUCCESS, resultMap);
	}

	private Map<String, UserPointDto> generateFullUserPointMap(User user) {
		Map<String, UserPointDto> pointMap = new HashMap<String, UserPointDto>();
		Map<String, PointTypeCode> pointTypeMap = PointTypeCode.enumMap;
		for (String code : pointTypeMap.keySet()) {
			pointMap.put(code, new UserPointDto(pointTypeMap.get(code)));
		}

		List<UserPoint> userPoints = userPointRepository.findByUserId(user.getId());

		for (UserPoint userPoint : userPoints) {
			pointMap.put(userPoint.getPointType(), new UserPointDto(userPoint));
		}

		return pointMap;
	}

	@Transactional
	public ResponseDto changeAppSetting(User user, UserAppSettingDto settingDto) {
		UserAppSetting appSetting = userAppSettingRepository.findByUserIdForUpdate(user.getId()).orElse(null);

		if (appSetting == null) {
			appSetting = new UserAppSetting();
			appSetting.setCommunityYn(settingDto.isCommunityYn());
			appSetting.setMessageYn(settingDto.isMessageYn());
			appSetting.setFriendYn(settingDto.isFriendYn());
			appSetting.setServiceYn(settingDto.isServiceYn());
			appSetting.setWalkingYn(settingDto.isWalkingYn());
			userAppSettingRepository.save(appSetting);
		} else {
			appSetting.setCommunityYn(settingDto.isCommunityYn());
			appSetting.setMessageYn(settingDto.isMessageYn());
			appSetting.setFriendYn(settingDto.isFriendYn());
			appSetting.setServiceYn(settingDto.isServiceYn());
			appSetting.setWalkingYn(settingDto.isWalkingYn());
		}

		return new ResponseDto(ResponseType.SUCCESS);

	}

}
