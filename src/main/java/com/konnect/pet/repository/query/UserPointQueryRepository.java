package com.konnect.pet.repository.query;

import static com.konnect.pet.entity.QUserPointHistory.userPointHistory;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.konnect.pet.dto.UserPointHistoryDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserPointQueryRepository {

	private final JPAQueryFactory queryFactory;

	public List<UserPointHistoryDto> findUserPointHistories(Long userId, String pointType, String type, int limit,
			int offset) {
		return queryFactory
				.select(Projections.constructor(UserPointHistoryDto.class, userPointHistory.id,
						userPointHistory.balance, userPointHistory.pointType, userPointHistory.historyType,
						userPointHistory.createdDate))
				.from(userPointHistory)
				.where(userPointHistory.user.id.eq(userId), userPointHistory.pointType.eq(pointType),
						(type.equals("plus") ? userPointHistory.balance.gt(0) : userPointHistory.balance.lt(0)))
				.orderBy(userPointHistory.id.desc()).limit(limit).offset(offset).fetch();
	}
}
