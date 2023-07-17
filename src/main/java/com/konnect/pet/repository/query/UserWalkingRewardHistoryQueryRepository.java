package com.konnect.pet.repository.query;

import static com.konnect.pet.entity.QUserWalkingRewardHistory.userWalkingRewardHistory;
import static com.konnect.pet.entity.QWalkingRewardPolicy.walkingRewardPolicy;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.konnect.pet.dto.TermsGroupDto;
import com.konnect.pet.entity.QUserWalkingRewardHistory;
import com.konnect.pet.entity.QWalkingRewardPolicy;
import com.konnect.pet.entity.User;
import com.konnect.pet.entity.UserWalkingHistory;
import com.konnect.pet.entity.UserWalkingRewardHistory;
import com.konnect.pet.enums.code.LocationCode;
import com.konnect.pet.enums.code.PeriodCode;
import com.querydsl.core.group.Group;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserWalkingRewardHistoryQueryRepository {

	private final JPAQueryFactory queryFactory;

	public Map<Long, Integer> findUserCurrentRewardTotalAmount(Long userId) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime startOfDay = now.with(LocalTime.MIDNIGHT);
		LocalDateTime startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
				.with(LocalTime.MIDNIGHT);
		LocalDateTime startOfMonth = now.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIDNIGHT);

		return queryFactory.from(userWalkingRewardHistory)
				.join(userWalkingRewardHistory.walkingRewardPolicy, walkingRewardPolicy)
				.on(userWalkingRewardHistory.walkingRewardPolicy.activeYn.eq(true),
						userWalkingRewardHistory.walkingRewardPolicy.startDate.loe(now),
						userWalkingRewardHistory.walkingRewardPolicy.endDate.goe(now))
				.where(userWalkingRewardHistory.user.id.eq(userId)
						.and(userWalkingRewardHistory.walkingRewardPolicy.peroidType.eq(PeriodCode.DAY.getCode())
								.and(userWalkingRewardHistory.createdDate.goe(startOfDay))
								.or(userWalkingRewardHistory.walkingRewardPolicy.peroidType
										.eq(PeriodCode.WEEK.getCode())
										.and(userWalkingRewardHistory.createdDate.goe(startOfWeek))
										.or(userWalkingRewardHistory.walkingRewardPolicy.peroidType
												.eq(PeriodCode.MONTH.getCode())
												.and(userWalkingRewardHistory.createdDate
														.goe(startOfMonth))
										.or(userWalkingRewardHistory.walkingRewardPolicy.peroidType
												.eq(PeriodCode.INFINITY.getCode()))))))
				.transform(GroupBy.groupBy(userWalkingRewardHistory.walkingRewardPolicy.id).as(
						userWalkingRewardHistory.amount.sum()));

	}
}
