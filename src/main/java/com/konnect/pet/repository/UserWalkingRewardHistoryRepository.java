package com.konnect.pet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.UserWalkingHistory;
import com.konnect.pet.entity.UserWalkingRewardHistory;

public interface UserWalkingRewardHistoryRepository extends JpaRepository<UserWalkingRewardHistory, Long>{

	@Query("select u from UserWalkingRewardHistory u where u.id = :id")
	Optional<UserWalkingRewardHistory> findById(@Param("id") Long id);

	@Query("select u from UserWalkingRewardHistory u where u.user.id = :userId")
	List<UserWalkingRewardHistory> findByUserId(@Param("userId") Long userId);

	@Query("select u from UserWalkingRewardHistory u where u.walkingRewardPolicy.id = :policyId")
	List<UserWalkingRewardHistory> findByWalkingId(@Param("policyId") Long policyId);
}
