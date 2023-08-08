package com.konnect.pet.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.konnect.pet.dto.UserPetDto;
import com.konnect.pet.dto.UserPointDto;
import com.konnect.pet.dto.UserProfileDto;
import com.konnect.pet.dto.UserSimpleDto;
import com.konnect.pet.dto.VerifyFormat;
import com.konnect.pet.entity.User;
import com.konnect.pet.entity.UserFriend;
import com.konnect.pet.entity.UserPet;
import com.konnect.pet.entity.UserPoint;
import com.konnect.pet.entity.UserProfile;
import com.konnect.pet.entity.UserRemoved;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.enums.VerifyType;
import com.konnect.pet.enums.code.LocationCode;
import com.konnect.pet.enums.code.NotificationTypeCode;
import com.konnect.pet.enums.code.PointTypeCode;
import com.konnect.pet.enums.code.ProcessStatusCode;
import com.konnect.pet.ex.CustomResponseException;
import com.konnect.pet.repository.UserFriendRepository;
import com.konnect.pet.repository.UserPetRepository;
import com.konnect.pet.repository.UserPointRepository;
import com.konnect.pet.repository.UserProfileRepository;
import com.konnect.pet.repository.UserRemovedRepository;
import com.konnect.pet.repository.UserRepository;
import com.konnect.pet.repository.redis.RefreshTokenRepository;
import com.konnect.pet.response.ResponseDto;
import com.konnect.pet.utils.Aes256Utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityService {

	private final UserRepository userRepository;
	private final UserPetRepository userPetRepository;
	private final UserProfileRepository userProfileRepository;
	private final UserFriendRepository userFriendRepository;

	private final NotificationService notificationService;

	@Transactional
	public ResponseDto requestFriend(User user, Long toUserId) {
		User toUser = userRepository.findById(toUserId)
				.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));
		UserFriend friend = new UserFriend();
		friend.setFromUser(user);
		friend.setToUser(toUser);
		friend.setStatus(ProcessStatusCode.PENDING.getCode());

		try {
			userFriendRepository.save(friend);

		} catch (Exception e) {
			throw new CustomResponseException(ResponseType.DUPLICATED_REQUEST);
		}
		notificationService.createMacroUserNotificationLog(toUser, NotificationTypeCode.REQUEST_FRIEND);

		return new ResponseDto(ResponseType.SUCCESS);

	}

}
