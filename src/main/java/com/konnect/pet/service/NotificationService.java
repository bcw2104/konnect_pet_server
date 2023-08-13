package com.konnect.pet.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.konnect.pet.dto.PageRequestDto;
import com.konnect.pet.dto.UserNotificationDto;
import com.konnect.pet.entity.User;
import com.konnect.pet.entity.UserNotification;
import com.konnect.pet.entity.UserNotificationLog;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.enums.code.NotificationTypeCode;
import com.konnect.pet.repository.UserNotificationLogRepository;
import com.konnect.pet.repository.UserNotificationRepository;
import com.konnect.pet.repository.query.UserNotificationQueryRepository;
import com.konnect.pet.response.ResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

	private final UserNotificationRepository userNotificationRepository;
	private final UserNotificationLogRepository userNotificationLogRepository;
	private final UserNotificationQueryRepository userNotificationQueryRepository;

	@Transactional
	public ResponseDto getRecentUserNotifications(User user, PageRequestDto pageDto) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		int limit = pageDto.getSize() + 1;
		int offset = (pageDto.getPage() - 1) * pageDto.getSize();

		List<UserNotificationDto> notifications = userNotificationQueryRepository.findUserNotifications(user.getId(),
				limit, offset);
		userNotificationLogRepository.updateVisitedYn(user.getId());

		boolean hasNext = false;
		if (notifications.size() == limit) {
			notifications.remove(limit - 1);
			hasNext = true;
		}
		resultMap.put("notifications", notifications);
		resultMap.put("hasNext", hasNext);

		return new ResponseDto(ResponseType.SUCCESS, resultMap);
	}

	@Transactional
	public UserNotificationLog createMacroUserNotificationLog(User user, NotificationTypeCode type) {

		try {
			List<UserNotification> notifications = userNotificationRepository.findActiveByNotiType(type.getCode());

			if (notifications.isEmpty()) {
				return null;
			}

			UserNotificationLog notiLog = new UserNotificationLog();
			notiLog.setUser(user);
			notiLog.setUserNotification(notifications.get(0));
			notiLog.setVisitedYn(false);

			userNotificationLogRepository.save(notiLog);
			log.info("create notification - user: {}, notiCode: {}", user.getId(), type.getCode());

			return notiLog;
		} catch (Exception e) {
			log.error("failed to create notification - user: {}, notiCode: {}", user.getId(), type.getCode());
			return null;
		}
	}
}
