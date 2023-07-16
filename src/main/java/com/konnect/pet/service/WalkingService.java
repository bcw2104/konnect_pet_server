package com.konnect.pet.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.konnect.pet.dto.PropertiesDto;
import com.konnect.pet.dto.WalkingRewardPolicyDto;
import com.konnect.pet.entity.Properties;
import com.konnect.pet.entity.User;
import com.konnect.pet.entity.UserWalkingFootprint;
import com.konnect.pet.entity.UserWalkingHistory;
import com.konnect.pet.entity.WalkingRewardPolicy;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.ex.CustomResponseException;
import com.konnect.pet.repository.PropertiesRepository;
import com.konnect.pet.repository.UserRepository;
import com.konnect.pet.repository.UserWalkingFootprintRepository;
import com.konnect.pet.repository.UserWalkingHistoryRepository;
import com.konnect.pet.repository.WalkingRewardPolicyRepository;
import com.konnect.pet.response.ResponseDto;
import com.konnect.pet.utils.Aes256Utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalkingService {

	private final UserRepository userRepository;
	private final UserWalkingHistoryRepository userWalkingHistoryRepository;
	private final WalkingRewardPolicyRepository walkingRewardPolicyRepository;
	private final UserWalkingFootprintRepository userWalkingFootprintRepository;
	private final PropertiesRepository propertiesRepository;

	private ObjectMapper objectMapper;

	@Value("${application.aes.service.key}")
	private String SERVICE_AES_KEY;
	@Value("${application.aes.service.iv}")
	private String SERVICE_AES_IV;

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
			Map<Long, Map<String, Object>> rewards = objectMapper.readValue(body.get("rewards").toString(),
					new TypeReference<Map<Long, Map<String, Object>>>() {
					});
			List<List<String>> footprintCoords = objectMapper.readValue(body.get("footprintCoords").toString(),
					new TypeReference<List<List<String>>>() {
					});
			String savedCoords = body.get("savedCoords").toString();

			log.info("Save walking data - walkingId: {}", id);

			LocalDateTime now = LocalDateTime.now();

			UserWalkingHistory walkingHistory = userWalkingHistoryRepository.findById(id).orElseThrow(
					() -> new CustomResponseException(ResponseType.INVALID_PARAMETER, "walking history not found"));
			walkingHistory.setMeters(meters);
			walkingHistory.setSeconds(seconds);
			walkingHistory.setRoutes(savedCoords);
			walkingHistory.setUser(user);
			walkingHistory.setEndDate(now);

			createFootprint(user, footprintCoords, walkingHistory);

			List<WalkingRewardPolicy> rewardPolicies = walkingRewardPolicyRepository.findActiveAll();
			Map<Long, WalkingRewardPolicy> rewardPolicyMap = new HashMap<Long, WalkingRewardPolicy>();
			rewardPolicies.forEach(ele -> rewardPolicyMap.put(ele.getId(), ele));


			return null;
		} catch (Exception e) {
			log.error("Save walking data failed - user: {}, body: {}", user.getId(), body.toString());
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER, e.getMessage());
		}
	}

	@Transactional
	private void createFootprint(User user, List<List<String>> footprintCoords, UserWalkingHistory walkingHistory) {
		int maxFootprintAmount = Integer
				.parseInt(propertiesRepository.findValueByKey("walking_footprint_max_amount").orElse("5"));
		int footprintStock = Integer
				.parseInt(propertiesRepository.findValueByKey("walking_footprint_stock").orElse("3"));

		int footPrintLength = footprintCoords.size() > maxFootprintAmount ? maxFootprintAmount : footprintCoords.size();

		List<UserWalkingFootprint> footprints = new ArrayList<UserWalkingFootprint>();
		for (int i = 0; i < footPrintLength; i++) {
			try {
				List<String> footprintCoord = footprintCoords.get(i);

				UserWalkingFootprint footprint = new UserWalkingFootprint();
				footprint.setStock(footprintStock);
				footprint.setLatitude(footprintCoord.get(0));
				footprint.setLongitude(footprintCoord.get(1));
				footprint.setUser(user);
				footprint.setUserWalkingHistory(walkingHistory);
				footprints.add(footprint);
			} catch (Exception e) {
			}
		}

		if (footprints.size() > 0) {
			userWalkingFootprintRepository.saveAll(footprints);
		}
	}

}
