package com.konnect.pet.repository.query;

import static com.konnect.pet.entity.QUserFriend.userFriend;
import static com.konnect.pet.entity.QUserProfile.userProfile;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.konnect.pet.dto.UserFriendDto;
import com.konnect.pet.enums.code.ProcessStatusCode;
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
						userProfile.nickname, userProfile.birthDate, userProfile.gender, userProfile.profileImgPath,
						userFriend.status, userFriend.createdDate))
				.from(userFriend).join(userProfile).on(userFriend.toUser.id.eq(userProfile.user.id))
				.where(userFriend.fromUser.id.eq(userId), userFriend.status.eq(ProcessStatusCode.PERMITTED.getCode()))
				.orderBy(userProfile.nickname.desc())
				.fetch();
	}

	public List<UserFriendDto> findRequestFriends(Long userId) {

		return queryFactory
				.select(Projections.constructor(UserFriendDto.class, userFriend.id, userFriend.toUser.id,
						userProfile.nickname, userProfile.birthDate, userProfile.gender, userProfile.profileImgPath,
						userFriend.status, userFriend.createdDate))
				.from(userFriend).join(userProfile).on(userFriend.toUser.id.eq(userProfile.user.id))
				.where(userFriend.fromUser.id.eq(userId), userFriend.status.eq(ProcessStatusCode.PENDING.getCode()))
				.orderBy(userProfile.nickname.desc()).fetch();

	}

	public List<UserFriendDto> findRequestedFriends(Long userId) {

		return queryFactory
				.select(Projections.constructor(UserFriendDto.class, userFriend.id, userFriend.fromUser.id,
						userProfile.nickname, userProfile.birthDate, userProfile.gender, userProfile.profileImgPath,
						userFriend.status, userFriend.createdDate))
				.from(userFriend).join(userProfile).on(userFriend.fromUser.id.eq(userProfile.user.id))
				.where(userFriend.toUser.id.eq(userId), userFriend.status.eq(ProcessStatusCode.PENDING.getCode()))
				.orderBy(userProfile.nickname.desc()).fetch();

	}

}
