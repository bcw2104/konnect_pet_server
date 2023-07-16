package com.konnect.pet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.UserWalkingFootprint;
import com.konnect.pet.entity.UserWalkingHistory;

public interface UserWalkingFootprintRepository extends JpaRepository<UserWalkingFootprint, Long>{

	@Query("select u from UserWalkingFootprint u where u.id = :id")
	Optional<UserWalkingFootprint> findById(@Param("id") Long id);

	@Query("select u from UserWalkingFootprint u where u.user.id = :userId")
	List<UserWalkingFootprint> findByUserId(@Param("userId") Long userId);

	@Query("select u from UserWalkingFootprint u where u.userWalkingHistory.id = :walkingId")
	List<UserWalkingFootprint> findByWalkingId(@Param("walkingId") Long walkingId);
}
