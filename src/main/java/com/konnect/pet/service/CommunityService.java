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

import com.konnect.pet.dto.PageRequestDto;
import com.konnect.pet.dto.UserFriendDto;
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
import com.konnect.pet.enums.code.UserStatusCode;
import com.konnect.pet.ex.CustomResponseException;
import com.konnect.pet.repository.UserFriendRepository;
import com.konnect.pet.repository.UserPetRepository;
import com.konnect.pet.repository.UserPointRepository;
import com.konnect.pet.repository.UserProfileRepository;
import com.konnect.pet.repository.UserRemovedRepository;
import com.konnect.pet.repository.UserRepository;
import com.konnect.pet.repository.query.UserFriendQueryRepository;
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
	private final UserFriendQueryRepository userFriendQueryRepository;
	private final UserProfileRepository userProfileRepository;
	private final UserFriendRepository userFriendRepository;

	private final NotificationService notificationService;

	@Transactional
	public ResponseDto requestFriend(User user, Long toUserId) {
		User toUser = userRepository.findById(toUserId)
				.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));

		if (toUser.getStatus().equals(UserStatusCode.REMOVED.getCode())) {
			return new ResponseDto(ResponseType.LEAVED_USER);
		}

		UserFriend friend = userFriendRepository.findByFromUserIdAndToUserId(user.getId(), toUserId).orElse(null);
		UserFriend toFriend = userFriendRepository.findByFromUserIdAndToUserId(toUserId, user.getId()).orElse(null);

		if (toFriend != null && friend != null) {
			if (toFriend.getStatus().equals(ProcessStatusCode.PERMITTED.getCode())
					&& friend.getStatus().equals(ProcessStatusCode.PERMITTED.getCode())) {
				return new ResponseDto(ResponseType.SUCCESS, ProcessStatusCode.PERMITTED.getCode());
			} else if (toFriend.getStatus().equals(ProcessStatusCode.PENDING.getCode())) {
				friend.setStatus(ProcessStatusCode.PERMITTED.getCode());
				toFriend.setStatus(ProcessStatusCode.PERMITTED.getCode());
				notificationService.createMacroUserNotificationLog(toUser, NotificationTypeCode.ACCEPT_FRIEND);
				return new ResponseDto(ResponseType.SUCCESS, ProcessStatusCode.PERMITTED.getCode());
			} else {
				friend.setStatus(ProcessStatusCode.PENDING.getCode());
				toFriend.setStatus(ProcessStatusCode.NONE.getCode());
			}
		} else if (toFriend != null) {
			toFriend.setStatus(ProcessStatusCode.NONE.getCode());
			UserFriend newFriend = new UserFriend();
			newFriend.setFromUser(user);
			newFriend.setToUser(toUser);
			newFriend.setStatus(ProcessStatusCode.PENDING.getCode());
			userFriendRepository.save(newFriend);
		} else if (friend != null) {
			friend.setStatus(ProcessStatusCode.PENDING.getCode());

			UserFriend toNewFriend = new UserFriend();
			toNewFriend.setFromUser(toUser);
			toNewFriend.setToUser(user);
			toNewFriend.setStatus(ProcessStatusCode.NONE.getCode());
			userFriendRepository.save(toNewFriend);
		} else {
			UserFriend newFriend = new UserFriend();
			newFriend.setFromUser(user);
			newFriend.setToUser(toUser);
			newFriend.setStatus(ProcessStatusCode.PENDING.getCode());
			userFriendRepository.save(newFriend);

			UserFriend toNewFriend = new UserFriend();
			toNewFriend.setFromUser(toUser);
			toNewFriend.setToUser(user);
			toNewFriend.setStatus(ProcessStatusCode.NONE.getCode());
			userFriendRepository.save(toNewFriend);
		}
		notificationService.createMacroUserNotificationLog(toUser, NotificationTypeCode.REQUEST_FRIEND);

		return new ResponseDto(ResponseType.SUCCESS, ProcessStatusCode.PENDING.getCode());

	}

	@Transactional(readOnly = true)
	public ResponseDto getFriends(User user) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		List<UserFriendDto> friends = userFriendQueryRepository.findUserFriends(user.getId());

		resultMap.put("friends", friends);

		return new ResponseDto(ResponseType.SUCCESS, resultMap);
	}

	@Transactional(readOnly = true)
	public ResponseDto getPendingFriends(User user) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		List<UserFriendDto> requestFriends = userFriendQueryRepository.findRequestFriends(user.getId());
		List<UserFriendDto> requestedFriends = userFriendQueryRepository.findRequestedFriends(user.getId());

		resultMap.put("request", requestFriends);
		resultMap.put("requested", requestedFriends);

		return new ResponseDto(ResponseType.SUCCESS, resultMap);
	}

	@Transactional
	public ResponseDto replyFriend(User user, Long toUserId, ProcessStatusCode code) {
		if (!code.equals(ProcessStatusCode.PERMITTED) && !code.equals(ProcessStatusCode.CANCELED)
				&& !code.equals(ProcessStatusCode.DENIED)) {
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
		}

		User toUser = userRepository.findById(toUserId)
				.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));

		if (toUser.getStatus().equals(UserStatusCode.REMOVED.getCode())) {
			return new ResponseDto(ResponseType.LEAVED_USER);
		}

		UserFriend friend = userFriendRepository.findByFromUserIdAndToUserId(user.getId(), toUserId).orElse(null);
		UserFriend toFriend = userFriendRepository.findByFromUserIdAndToUserId(toUserId, user.getId()).orElse(null);

		if (code.equals(ProcessStatusCode.CANCELED)) {
			if (friend == null || toFriend == null
					|| (!friend.getStatus().equals(ProcessStatusCode.PERMITTED.getCode())
							|| !toFriend.getStatus().equals(ProcessStatusCode.PERMITTED.getCode()))
							&& !friend.getStatus().equals(ProcessStatusCode.PENDING.getCode())
							&& !toFriend.getStatus().equals(ProcessStatusCode.PENDING.getCode())) {

				return new ResponseDto(ResponseType.DUPLICATED_REQUEST, code.getCode());
			}
		} else {
			if (friend == null || toFriend == null || (!friend.getStatus().equals(ProcessStatusCode.PENDING.getCode())
					&& !toFriend.getStatus().equals(ProcessStatusCode.PENDING.getCode()))) {
				return new ResponseDto(ResponseType.DUPLICATED_REQUEST, code.getCode());
			}
		}
		toFriend.setStatus(code.getCode());
		friend.setStatus(code.getCode());

		if (code.getCode().equals(ProcessStatusCode.PERMITTED.getCode())) {
			notificationService.createMacroUserNotificationLog(toUser, NotificationTypeCode.ACCEPT_FRIEND);
		}
		return new ResponseDto(ResponseType.SUCCESS, code.getCode());
	}

}
