package com.konnect.pet.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.UserWalkingFootprintCatchHistory;
import com.konnect.pet.entity.UserWalkingHistory;

import jakarta.persistence.LockModeType;

public interface UserWalkingFootprintCatchHistoryRepository extends JpaRepository<UserWalkingFootprintCatchHistory, Long>{

	@Query("select u from UserWalkingHistory u where u.id = :id")
	Optional<UserWalkingFootprintCatchHistory> findById(@Param("id") Long id);

	@Query("select u from UserWalkingFootprintCatchHistory u where u.user.id = :userId")
	List<UserWalkingFootprintCatchHistory> findByUserId(@Param("userId") Long userId);

	@Query("select u.userWalkingFootprint.id from UserWalkingFootprintCatchHistory u "
			+ "where u.createdDate >= :createdDate and u.user.id = :userId")
	List<Long> findFootprintIdByCreatedDateAndUserId(@Param("createdDate") LocalDateTime createdDate,@Param("userId") Long userId);

	@Query("select u from UserWalkingFootprintCatchHistory u "
			+ "join fetch u.userWalkingFootprint f "
			+ "join fetch f.user fu "
			+ "where u.userWalkingHistory.id = :walkingId")
	List<UserWalkingFootprintCatchHistory> findWithUserByWalkingId(@Param("walkingId") Long walkingId);
}
