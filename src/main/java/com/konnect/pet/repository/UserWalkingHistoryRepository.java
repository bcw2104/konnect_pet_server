package com.konnect.pet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.UserWalkingHistory;

public interface UserWalkingHistoryRepository extends JpaRepository<UserWalkingHistory, Long>{

	@Query("select u from UserWalkingHistory u where u.id = :id")
	Optional<UserWalkingHistory> findById(@Param("id") Long id);
}
