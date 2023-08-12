package com.konnect.pet.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.UserWalkingHistory;

import jakarta.persistence.LockModeType;

public interface UserWalkingHistoryRepository extends JpaRepository<UserWalkingHistory, Long> {

	@Query("select u from UserWalkingHistory u where u.id = :id")
	Optional<UserWalkingHistory> findById(@Param("id") Long id);

	@Query("select u from UserWalkingHistory u "
			+ "where u.user.id = :userId and u.endDate is not null and u.startDate >= :startDate and u.startDate <= :endDate order by u.id desc")
	List<UserWalkingHistory> findByUserIdAndStartDate(@Param("userId") Long userId,
			@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

	@Query("select u from UserWalkingHistory u left join fetch u.rewardHistories rh where u.id = :id")
	Optional<UserWalkingHistory> findWithRewardHistById(@Param("id") Long id);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select u from UserWalkingHistory u where u.id = :id")
	Optional<UserWalkingHistory> findByIdForUpdate(@Param("id") Long id);
}
