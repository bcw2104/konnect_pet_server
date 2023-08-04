package com.konnect.pet.repository.query;

import static com.konnect.pet.entity.QTerms.terms;
import static com.konnect.pet.entity.QTermsGroup.termsGroup;
import static com.konnect.pet.entity.QUserNotification.userNotification;
import static com.konnect.pet.entity.QUserNotificationLog.userNotificationLog;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.konnect.pet.dto.TermsDto;
import com.konnect.pet.dto.TermsGroupDto;
import com.konnect.pet.dto.UserNotificationDto;
import com.konnect.pet.entity.QUserNotification;
import com.konnect.pet.entity.QUserNotificationLog;
import com.konnect.pet.entity.UserNotification;
import com.konnect.pet.entity.UserNotificationLog;
import com.konnect.pet.enums.code.LocationCode;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserNotificationQueryRepository {

	private final JPAQueryFactory queryFactory;

	public List<UserNotificationDto> findUserNotifications(Long userId, int limit) {
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
						))
				.from(userNotificationLog).join(userNotificationLog.userNotification ,userNotification)
				.where(userNotificationLog.user.id.eq(userId))
				.orderBy(userNotificationLog.id.desc())
				.limit(limit)
				.fetch();
	}
}
