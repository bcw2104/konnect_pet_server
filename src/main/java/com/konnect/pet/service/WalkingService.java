package com.konnect.pet.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.konnect.pet.dto.PropertiesDto;
import com.konnect.pet.dto.UserWalkingFootprintDetailDto;
import com.konnect.pet.dto.UserWalkingFootprintDto;
import com.konnect.pet.dto.UserWalkingHistoryDto;
import com.konnect.pet.dto.WalkingRewardPolicyDto;
import com.konnect.pet.entity.User;
import com.konnect.pet.entity.UserFriend;
import com.konnect.pet.entity.UserProfile;
import com.konnect.pet.entity.UserWalkingFootprint;
import com.konnect.pet.entity.UserWalkingFootprintCatchHistory;
import com.konnect.pet.entity.UserWalkingHistory;
import com.konnect.pet.entity.UserWalkingRewardHistory;
import com.konnect.pet.entity.WalkingRewardPolicy;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.enums.code.NotificationTypeCode;
import com.konnect.pet.enums.code.PointHistoryTypeCode;
import com.konnect.pet.enums.code.PointTypeCode;
import com.konnect.pet.enums.code.UserStatusCode;
import com.konnect.pet.enums.code.WalkingRewardProvideTypeCode;
import com.konnect.pet.ex.CustomResponseException;
import com.konnect.pet.repository.PropertiesRepository;
import com.konnect.pet.repository.UserFriendRepository;
import com.konnect.pet.repository.UserProfileRepository;
import com.konnect.pet.repository.UserRepository;
import com.konnect.pet.repository.UserWalkingFootprintCatchHistoryRepository;
import com.konnect.pet.repository.UserWalkingFootprintRepository;
import com.konnect.pet.repository.UserWalkingHistoryRepository;
import com.konnect.pet.repository.UserWalkingRewardHistoryRepository;
import com.konnect.pet.repository.WalkingRewardPolicyRepository;
import com.konnect.pet.repository.query.PropertiesQueryRepository;
import com.konnect.pet.repository.query.UserWalkingQueryRepository;
import com.konnect.pet.response.ResponseDto;
import com.konnect.pet.utils.Aes256Utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalkingService {

	private final NotificationService notificationService;

	private final UserRepository userRepository;
	private final UserFriendRepository userFriendRepository;
	private final UserProfileRepository userProfileRepository;
	private final UserWalkingHistoryRepository userWalkingHistoryRepository;
	private final WalkingRewardPolicyRepository walkingRewardPolicyRepository;
	private final UserWalkingRewardHistoryRepository userWalkingRewardHistoryRepository;
	private final UserWalkingQueryRepository userWalkingQueryRepository;
	private final UserWalkingFootprintRepository userWalkingFootprintRepository;
	private final UserWalkingFootprintCatchHistoryRepository userWalkingFootprintCatchHistoryRepository;
	private final PropertiesRepository propertiesRepository;
	private final PropertiesQueryRepository PropertiesQueryRepository;
	private final PointService pointService;

	private final ObjectMapper objectMapper;

	@Value("${application.aes.service.key}")
	private String SERVICE_AES_KEY;
	@Value("${application.aes.service.iv}")
	private String SERVICE_AES_IV;

	private final int DEFAULT_DISPLAY_FOOTPRINTS_AMOUNT = 50;
	private final int DEFAULT_DISPLAY_FOOTPRINTS_DISTANCE = 4;
	private final int DEFAULT_DISPLAY_FOOTPRINTS_PERIOD = 5;

	@Transactional
	public ResponseDto generateStartWalkingData(User user) {
		LocalDateTime now = LocalDateTime.now();

		UserWalkingHistory walkingHistory = new UserWalkingHistory(user, now);

		userWalkingHistoryRepository.save(walkingHistory);

		Map<String, Object> result = new HashMap<>();

		List<WalkingRewardPolicyDto> rewardPolicies = walkingRewardPolicyRepository.findActiveAll().stream()
				.map(WalkingRewardPolicyDto::new).collect(Collectors.toList());

		List<PropertiesDto> properties = propertiesRepository.findByKeyGroup("walking_policy").stream()
				.map(PropertiesDto::new).collect(Collectors.toList());

		result.put("rewardPolicies", rewardPolicies);
		result.put("walkingPolicies", properties);

		String keyText = walkingHistory.getId() + "_" + now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		try {
			String encKey = Aes256Utils.encrypt(keyText, SERVICE_AES_KEY, SERVICE_AES_IV);
			result.put("key", encKey);
			result.put("id", walkingHistory.getId());

			return new ResponseDto(ResponseType.SUCCESS, result);
		} catch (Exception e) {
			throw new CustomResponseException(ResponseType.SERVER_ERROR, e.getMessage());
		}
	}

	@Transactional
	public ResponseDto saveWalking(User user, Map<String, Object> body) {
		try {
			Long id = Long.parseLong(body.get("id").toString());
			String key = body.get("key").toString();

			String decKey = Aes256Utils.decrypt(key, SERVICE_AES_KEY, SERVICE_AES_IV);
			Long cid = Long.parseLong(decKey.split("_")[0]);
			if (!cid.equals(id)) {
				throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
			}
			int meters = Integer.parseInt(body.get("meters").toString());
			int seconds = Integer.parseInt(body.get("seconds").toString());

			Map<Long, Object> rewards = objectMapper.readValue(body.get("rewards").toString(),
					new TypeReference<Map<Long, Object>>() {
					});

			List<Object> footprintCoords = objectMapper.readValue(body.get("footprintCoords").toString(),
					new TypeReference<List<Object>>() {
					});

			List<Long> catchedFootprints = objectMapper.readValue(body.get("catchedFootprints").toString(),
					new TypeReference<List<Long>>() {
					});
			String savedCoords = body.get("savedCoords").toString();
			LocalDateTime endDate = LocalDateTime.now();
			try {
				endDate = LocalDateTime.parse(body.get("endDate").toString());
			} catch (Exception e) {
			}

			log.info("Save walking data - walkingId: {}", id);

			UserWalkingHistory walkingHistory = userWalkingHistoryRepository.findByIdForUpdate(id).orElseThrow(
					() -> new CustomResponseException(ResponseType.INVALID_PARAMETER, "walking history not found"));

			if (walkingHistory.getEndDate() != null) {
				new CustomResponseException(ResponseType.INVALID_PARAMETER, "already finished");
			}

			walkingHistory.setMeters(meters);
			walkingHistory.setSeconds(seconds);
			walkingHistory.setRoutes(savedCoords);
			walkingHistory.setUser(user);
			walkingHistory.setEndDate(endDate);

			if (!footprintCoords.isEmpty()) {
				createFootprint(user, footprintCoords, walkingHistory);
			}

			if (!rewards.isEmpty()) {
				provideWalkingReward(user, rewards, walkingHistory);
			}

			if (!catchedFootprints.isEmpty()) {
				saveCatchedFootprints(user, catchedFootprints, walkingHistory);
			}

			notificationService.createMacroUserNotificationLog(user, NotificationTypeCode.WAKLING_FINISHED);

			return new ResponseDto(ResponseType.SUCCESS);
		} catch (Exception e) {
			log.error("Save walking data failed - user: {}, body: {}", user.getId(), body.toString(), e);
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER, e.getMessage());
		}
	}

	@Transactional
	private void saveCatchedFootprints(User user, List<Long> catchedFootprints, UserWalkingHistory walkingHistory) {
		List<UserWalkingFootprintCatchHistory> footprintCatchHistories = new ArrayList<UserWalkingFootprintCatchHistory>();
		List<UserWalkingFootprint> footprints = userWalkingFootprintRepository.findByIds(catchedFootprints);
		for (UserWalkingFootprint footprint : footprints) {
			UserWalkingFootprintCatchHistory history = new UserWalkingFootprintCatchHistory();
			history.setUser(user);
			history.setUserWalkingHistory(walkingHistory);
			history.setUserWalkingFootprint(footprint);
			footprintCatchHistories.add(history);
		}
		userWalkingFootprintCatchHistoryRepository.saveAll(footprintCatchHistories);
	}

	@Transactional
	private void provideWalkingReward(User user, Map<Long, Object> rewards, UserWalkingHistory walkingHistory)
			throws JsonProcessingException, JsonMappingException {
		List<WalkingRewardPolicy> rewardPolicies = walkingRewardPolicyRepository.findActiveAll();
		Map<Long, WalkingRewardPolicy> rewardPolicyMap = new HashMap<Long, WalkingRewardPolicy>();
		rewardPolicies.forEach(ele -> rewardPolicyMap.put(ele.getId(), ele));

		Map<Long, Integer> rewardTotalAmounts = userWalkingQueryRepository
				.findUserCurrentRewardTotalAmount(user.getId());

		List<UserWalkingRewardHistory> rewardHistories = new ArrayList<UserWalkingRewardHistory>();

		Map<String, Integer> totalRewardAmountMap = new HashMap<String, Integer>();
		for (Long k : rewardPolicyMap.keySet()) {
			if (rewards.get(k) == null)
				continue;

			Map<String, Object> reward = (Map<String, Object>) rewards.get(k);

			int rewardAmount = Integer.parseInt(reward.get("rewardAmount").toString());

			int currentTotalAmount = rewardTotalAmounts.getOrDefault(k, 0);
			WalkingRewardPolicy policy = rewardPolicyMap.get(k);

			int maxAmountPerWalking = policy.getMaxRewardAmountPerWalking();
			int maxAmountPerPeriod = policy.getMaxRewardAmountPerPeriod();

			rewardAmount = Math.min(Math.min(maxAmountPerWalking, rewardAmount),
					maxAmountPerPeriod - currentTotalAmount);

			if (rewardAmount > 0) {
				UserWalkingRewardHistory rewardHistory = new UserWalkingRewardHistory();
				rewardHistory.setAmount(rewardAmount);
				rewardHistory.setPointType(policy.getPointType());
				rewardHistory.setPolicyName(policy.getPolicyName());
				rewardHistory.setRewardProvideType(policy.getRewardProvideType());
				rewardHistory.setRewardType(policy.getRewardType());
				rewardHistory.setUser(user);
				rewardHistory.setUserWalkingHistory(walkingHistory);
				rewardHistory.setWalkingRewardPolicy(policy);

				if (policy.getRewardProvideType().equals(WalkingRewardProvideTypeCode.REALTIME.getCode())) {
					rewardHistory.setPaymentYn(true);

					PointTypeCode pointTypeCode = PointTypeCode.findByCode(policy.getPointType());
					pointService.increaseUserPoint(user, pointTypeCode, rewardAmount);

					String pointType = policy.getPointType();
					if (totalRewardAmountMap.get(pointType) == null) {
						totalRewardAmountMap.put(pointType, rewardAmount);
					} else {
						int total = totalRewardAmountMap.get(pointType);
						totalRewardAmountMap.put(pointType, total + rewardAmount);
					}
				} else {
					rewardHistory.setPaymentYn(false);
				}

				rewardHistories.add(rewardHistory);
			}

		}

		for (String pointType : totalRewardAmountMap.keySet()) {
			pointService.createPointHistory(user, PointTypeCode.findByCode(pointType), PointHistoryTypeCode.WALKING,
					totalRewardAmountMap.get(pointType));
		}

		userWalkingRewardHistoryRepository.saveAll(rewardHistories);
	}

	@Transactional
	private void createFootprint(User user, List<Object> footprintCoords, UserWalkingHistory walkingHistory) {
		int maxFootprintAmount = Integer
				.parseInt(propertiesRepository.findValueByKey("walking_footprint_max_amount").orElse("5"));
		int footprintStock = Integer
				.parseInt(propertiesRepository.findValueByKey("walking_footprint_stock").orElse("3"));

		int footPrintLength = footprintCoords.size() > maxFootprintAmount ? maxFootprintAmount : footprintCoords.size();

		List<UserWalkingFootprint> footprints = new ArrayList<UserWalkingFootprint>();
		for (int i = 0; i < footPrintLength; i++) {
			try {

				List<String> footprintCoord = objectMapper.readValue(footprintCoords.get(i).toString(),
						new TypeReference<List<String>>() {
						});

				UserWalkingFootprint footprint = new UserWalkingFootprint();
				footprint.setStock(footprintStock);
				footprint.setLatitude(Double.parseDouble(footprintCoord.get(0)));
				footprint.setLongitude(Double.parseDouble(footprintCoord.get(1)));
				footprint.setUser(user);
				footprint.setUserWalkingHistory(walkingHistory);
				footprints.add(footprint);
			} catch (Exception e) {
				log.error("{}", e.getMessage(), e);
			}
		}

		if (footprints.size() > 0) {
			userWalkingFootprintRepository.saveAll(footprints);
		}
	}

	@Transactional(readOnly = true)
	public ResponseDto getWalkingHistory(User user, Long walkingId) {
		UserWalkingHistory userWalkingHistory = userWalkingHistoryRepository.findWithRewardHistById(walkingId)
				.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));

		if (!userWalkingHistory.getUser().getId().equals(user.getId())) {
			new CustomResponseException(ResponseType.INVALID_PARAMETER);
		}

		return new ResponseDto(ResponseType.SUCCESS, new UserWalkingHistoryDto(userWalkingHistory));
	}

	@Transactional(readOnly = true)
	public ResponseDto getAroundFootprint(User user, double lat, double lng) {
		Map<String, String> propertyMap = PropertiesQueryRepository.getPropertyMapByKeys(
				"walking_footprint_display_amount", "walking_footprint_display_distance",
				"walking_footprint_display_period");

		int displayDistance = DEFAULT_DISPLAY_FOOTPRINTS_DISTANCE;
		int displayAmount = DEFAULT_DISPLAY_FOOTPRINTS_AMOUNT;
		int displayPeriod = DEFAULT_DISPLAY_FOOTPRINTS_PERIOD;
		try {
			displayAmount = Integer.parseInt(propertyMap.get("walking_footprint_display_amount"));
		} catch (Exception e) {
			log.error("walking_footprint_display_amount props is not number");
		}
		try {
			displayDistance = Integer.parseInt(propertyMap.get("walking_footprint_display_distance"));
		} catch (Exception e) {
			log.error("walking_footprint_display_distance props is not number");
		}
		try {
			displayPeriod = Integer.parseInt(propertyMap.get("walking_footprint_display_period"));
		} catch (Exception e) {
			log.error("walking_footprint_display_distance props is not number");
		}
		LocalDateTime displayDate = LocalDateTime.now().minusDays(displayPeriod).with(LocalTime.MIDNIGHT);

		Map<String, Double> coordRadius = getCoordRadius(lat, lng, displayDistance);

		double minLat = coordRadius.get("minLat");
		double maxLat = coordRadius.get("maxLat");
		double minLng = coordRadius.get("minLng");
		double maxLng = coordRadius.get("maxLng");

		List<UserWalkingFootprintDto> radiusFootprints = userWalkingFootprintRepository
				.findAroundByLatLongLimit(displayDate, maxLat, maxLng, minLat, minLng, UserStatusCode.REMOVED.getCode(),
						PageRequest.of(0, displayAmount))
				.stream().map(UserWalkingFootprintDto::new).toList();
		List<Long> catchedFootprints = userWalkingFootprintCatchHistoryRepository
				.findFootprintIdByCreatedDateAndUserId(displayDate, user.getId());

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("radiusFootprints", radiusFootprints);
		resultMap.put("catchedFootprints", catchedFootprints);

		return new ResponseDto(ResponseType.SUCCESS, resultMap);
	}

	private Map<String, Double> getCoordRadius(double lat, double lng, double km) {

		double kmInLongitudeDegree = 111.320D * Math.cos(lat / 180.0 * Math.PI);

		double deltaLat = km / 111.1;
		double deltaLng = km / kmInLongitudeDegree;

		double minLat = lat - deltaLat;
		double maxLat = lat + deltaLat;
		double minLng = lng - deltaLng;
		double maxLng = lng + deltaLng;

		Map<String, Double> resultMap = new HashMap<String, Double>();

		resultMap.put("minLat", minLat);
		resultMap.put("maxLat", maxLat);
		resultMap.put("minLng", minLng);
		resultMap.put("maxLng", maxLng);

		return resultMap;
	}

	@Transactional(readOnly = true)
	public ResponseDto getFootprintInfo(User user,Long footprintId) {
		UserWalkingFootprint footprint = userWalkingFootprintRepository.findWithUserAndPetById(footprintId)
				.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));

		UserFriend friend = userFriendRepository.findByFromUserIdAndToUserId(user.getId(), footprint.getUser().getId())
				.orElse(null);

		UserProfile profile = userProfileRepository.findByUserId(footprint.getUser().getId()).orElse(new UserProfile());

		return new ResponseDto(ResponseType.SUCCESS, new UserWalkingFootprintDetailDto(footprint, profile,friend));
	}

}
