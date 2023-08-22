package com.konnect.pet.repository.query;

import static com.konnect.pet.entity.QUserNotification.userNotification;
import static com.konnect.pet.entity.QUserNotificationLog.userNotificationLog;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.konnect.pet.dto.UserNotificationDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserNotificationQueryRepository {

	private final JPAQueryFactory queryFactory;

	public List<UserNotificationDto> findUserNotifications(Long userId,LocalDateTime afterDate, int limit,int offset) {
		return queryFactory
				.select(Projections.constructor(UserNotificationDto.class
						,userNotificationLog.id
						,userNotificationLog.userNotification.category
						,userNotificationLog.userNotification.title
						,userNotificationLog.userNotification.content
						,userNotificationLog.userNotification.notiType
						,userNotificationLog.userNotification.landingType
						,userNotificationLog.userNotification.landingUrl
						,userNotificationLog.visitedYn
						,userNotificationLog.createdDate
						))
				.from(userNotificationLog).join(userNotificationLog.userNotification ,userNotification)
				.where(userNotificationLog.user.id.eq(userId), userNotificationLog.createdDate.goe(afterDate))
				.orderBy(userNotificationLog.id.desc())
				.limit(limit)
				.offset(offset)
				.fetch();
	}
}
