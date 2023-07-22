package com.konnect.pet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.UserWalkingHistory;

import jakarta.persistence.LockModeType;

public interface UserWalkingHistoryRepository extends JpaRepository<UserWalkingHistory, Long>{

	@Query("select u from UserWalkingHistory u where u.id = :id")
	Optional<UserWalkingHistory> findById(@Param("id") Long id);

	@Query("select u from UserWalkingHistory u "
			+ "join fetch u.rewardHistories rh "
			+ "where u.id = :id")
	Optional<UserWalkingHistory> findWithRewardHistById(@Param("id") Long id);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select u from UserWalkingHistory u where u.id = :id")
	Optional<UserWalkingHistory> findByIdForUpdate(@Param("id") Long id);
}
