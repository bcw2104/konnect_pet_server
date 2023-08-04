package com.konnect.pet.service;

import java.util.List;

import org.springframework.stereotype.Service;

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
	
	public ResponseDto getRecentUserNotifications(User user){
		List<UserNotificationDto> notifications = userNotificationQueryRepository.findUserNotifications(user.getId(),10);
		
		return new ResponseDto(ResponseType.SUCCESS,notifications);
	}
	
	public UserNotificationLog createMacroUserNotificationLog(User user,NotificationTypeCode type) {
		List<UserNotification> notifications = userNotificationRepository.findActiveByNotiType(type.getCode());
		
		if(notifications.isEmpty()) {
			return null;
		}
		
		UserNotificationLog log = new UserNotificationLog();
		log.setUser(user);
		log.setUserNotification(notifications.get(0));
		log.setVisitedYn(false);
		
		userNotificationLogRepository.save(log);
		
		return log;
	}
}
