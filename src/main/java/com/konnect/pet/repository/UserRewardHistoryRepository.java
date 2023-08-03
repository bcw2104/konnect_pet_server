package com.konnect.pet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.UserRewardHistory;
import com.konnect.pet.entity.UserWalkingHistory;
import com.konnect.pet.entity.UserWalkingRewardHistory;

public interface UserRewardHistoryRepository extends JpaRepository<UserRewardHistory, Long>{

	@Query("select u from UserRewardHistory u where u.id = :id")
	Optional<UserRewardHistory> findById(@Param("id") Long id);

	@Query("select u from UserRewardHistory u where u.user.id = :userId")
	List<UserRewardHistory> findByUserId(@Param("userId") Long userId);
}
