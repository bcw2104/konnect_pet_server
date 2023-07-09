package com.konnect.pet.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.konnect.pet.entity.User;
import com.konnect.pet.entity.UserWalkingHistory;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.ex.CustomResponseException;
import com.konnect.pet.repository.UserRepository;
import com.konnect.pet.repository.UserWalkingHistoryRepository;
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

	@Value("${application.aes.service.key}")
	private String SERVICE_AES_KEY;
	@Value("${application.aes.service.iv}")
	private String SERVICE_AES_IV;

	@Transactional
	public ResponseDto generateStartWalkingKey(User user) {
		LocalDateTime now = LocalDateTime.now();

		UserWalkingHistory walkingHistory = new UserWalkingHistory(user, now);

		userWalkingHistoryRepository.save(walkingHistory);

		String keyText = walkingHistory.getId() + "_" + now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		try {
			String encKey = Aes256Utils.encrypt(keyText, SERVICE_AES_KEY, SERVICE_AES_IV);
			Map<String, Object> result = new HashMap<>();
			result.put("key", encKey);

			return new ResponseDto(ResponseType.SUCCESS, result);
		} catch (Exception e) {
			throw new CustomResponseException(ResponseType.SERVER_ERROR);
		}
	}
}
