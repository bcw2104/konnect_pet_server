package com.konnect.pet.repository.query;

import static com.konnect.pet.entity.QUser.user;
import static com.konnect.pet.entity.QUserFriend.userFriend;
import static com.konnect.pet.entity.QUserProfile.userProfile;
import static com.konnect.pet.entity.QUserWalkingFootprint.userWalkingFootprint;
import static com.konnect.pet.entity.QUserWalkingHistory.userWalkingHistory;
import static com.konnect.pet.entity.QUserWalkingRewardHistory.userWalkingRewardHistory;
import static com.konnect.pet.entity.QWalkingRewardPolicy.walkingRewardPolicy;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.konnect.pet.dto.UserFriendDto;
import com.konnect.pet.dto.UserWalkingFootprintDetailDto;
import com.konnect.pet.entity.QUser;
import com.konnect.pet.entity.QUserFriend;
import com.konnect.pet.entity.QUserProfile;
import com.konnect.pet.entity.QUserWalkingFootprint;
import com.konnect.pet.entity.QUserWalkingHistory;
import com.konnect.pet.entity.UserWalkingHistory;
import com.konnect.pet.enums.code.PeriodCode;
import com.konnect.pet.enums.code.ProcessStatusCode;
import com.querydsl.core.group.Group;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserFriendQueryRepository {
	private final JPAQueryFactory queryFactory;

	public List<UserFriendDto> findUserFriends(Long userId) {

		return queryFactory
				.select(Projections.constructor(UserFriendDto.class, userFriend.id, userFriend.fromUser.id,
						userProfile.nickname, userProfile.birthDate, userProfile.gender, userProfile.profileImgUrl,
						userFriend.status, userFriend.createdDate))
				.from(userFriend).join(userProfile).on(userFriend.toUser.id.eq(userProfile.user.id))
				.where(userFriend.fromUser.id.eq(userId), userFriend.status.eq(ProcessStatusCode.PERMITTED.getCode()))
				.orderBy(userFriend.id.desc()).fetch();

	}

	public List<UserFriendDto> findRequestFriends(Long userId) {

		return queryFactory
				.select(Projections.constructor(UserFriendDto.class, userFriend.id, userFriend.fromUser.id,
						userProfile.nickname, userProfile.birthDate, userProfile.gender, userProfile.profileImgUrl,
						userFriend.status, userFriend.createdDate))
				.from(userFriend).join(userProfile).on(userFriend.toUser.id.eq(userProfile.user.id))
				.where(userFriend.fromUser.id.eq(userId), userFriend.status.eq(ProcessStatusCode.PENDING.getCode()))
				.orderBy(userFriend.id.desc()).fetch();

	}

	public List<UserFriendDto> findRequestedFriends(Long userId) {

		return queryFactory
				.select(Projections.constructor(UserFriendDto.class, userFriend.id, userFriend.fromUser.id,
						userProfile.nickname, userProfile.birthDate, userProfile.gender, userProfile.profileImgUrl,
						userFriend.status, userFriend.createdDate))
				.from(userFriend).join(userProfile).on(userFriend.fromUser.id.eq(userProfile.user.id))
				.where(userFriend.toUser.id.eq(userId), userFriend.status.eq(ProcessStatusCode.PENDING.getCode()))
				.orderBy(userFriend.id.desc()).fetch();

	}

}
