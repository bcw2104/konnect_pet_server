package com.konnect.pet.repository.query;

import static com.konnect.pet.entity.QUser.user;
import static com.konnect.pet.entity.QUserFriend.userFriend;
import static com.konnect.pet.entity.QUserProfile.userProfile;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.konnect.pet.dto.UserFriendDto;
import com.konnect.pet.entity.QUser;
import com.konnect.pet.enums.code.ProcessStatusCode;
import com.konnect.pet.enums.code.UserStatusCode;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserFriendQueryRepository {
	private final JPAQueryFactory queryFactory;

	public List<UserFriendDto> findUserFriends(Long userId) {
		return queryFactory
				.select(Projections.constructor(UserFriendDto.class, userFriend.id, userFriend.toUser.id,
						userProfile.nickname, userProfile.birthDate, userProfile.gender, userProfile.imgPath,
						userFriend.status, userFriend.createdDate))
				.from(userFriend).join(userProfile).on(userFriend.toUser.id.eq(userProfile.user.id))
				.where(userFriend.fromUser.id.eq(userId), userFriend.status.eq(ProcessStatusCode.PERMITTED.getCode()))
				.orderBy(userProfile.nickname.desc()).fetch();
	}

	public List<UserFriendDto> findRequestFriends(Long userId) {

		return queryFactory
				.select(Projections.constructor(UserFriendDto.class, userFriend.id, userFriend.toUser.id,
						userProfile.nickname, userProfile.birthDate, userProfile.gender, userProfile.imgPath,
						userFriend.status, userFriend.createdDate))
				.from(userFriend).join(userProfile).on(userFriend.toUser.id.eq(userProfile.user.id))
				.where(userFriend.fromUser.id.eq(userId), userFriend.status.eq(ProcessStatusCode.PENDING.getCode()))
				.orderBy(userProfile.nickname.desc()).fetch();

	}

	public List<UserFriendDto> findRequestedFriends(Long userId) {

		return queryFactory
				.select(Projections.constructor(UserFriendDto.class, userFriend.id, userFriend.fromUser.id,
						userProfile.nickname, userProfile.birthDate, userProfile.gender, userProfile.imgPath,
						userFriend.status, userFriend.createdDate))
				.from(userFriend).join(userProfile).on(userFriend.fromUser.id.eq(userProfile.user.id))
				.where(userFriend.toUser.id.eq(userId), userFriend.status.eq(ProcessStatusCode.PENDING.getCode()))
				.orderBy(userProfile.nickname.desc()).fetch();

	}

	public List<UserFriendDto> findRecommendFriends(Long userId, int limit, int offset) {

		return queryFactory
				.select(Projections.constructor(UserFriendDto.class, user.id, userProfile.nickname,
						userProfile.birthDate, userProfile.gender, userProfile.imgPath))
				.from(user).join(userProfile).on(user.id.eq(userProfile.user.id)).leftJoin(userFriend)
				.on(userFriend.fromUser.id.eq(userId), userFriend.toUser.id.eq(user.id))
				.where(user.status.eq(UserStatusCode.ACTIVE.getCode()), userFriend.id.isNull())
				.orderBy(user.id.asc()).limit(limit).offset(offset).fetch();

	}

}
